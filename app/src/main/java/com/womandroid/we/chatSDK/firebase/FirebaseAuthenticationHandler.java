package com.womandroid.we.chatSDK.firebase;

import android.os.AsyncTask;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;

import java.util.HashMap;
import java.util.Map;

import com.womandroid.we.chatSDK.core.base.AbstractAuthenticationHandler;
import com.womandroid.we.chatSDK.core.dao.User;
import com.womandroid.we.chatSDK.core.enums.AuthStatus;
import com.womandroid.we.chatSDK.core.events.NetworkEvent;
import com.womandroid.we.chatSDK.core.hook.HookEvent;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.core.types.AccountDetails;
import com.womandroid.we.chatSDK.core.types.AuthKeys;
import com.womandroid.we.chatSDK.core.types.ChatError;
import com.womandroid.we.chatSDK.core.utils.CrashReportingCompletableObserver;
import com.womandroid.we.chatSDK.firebase.FirebaseEventHandler;
import com.womandroid.we.chatSDK.firebase.wrappers.UserWrapper;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;

import static com.womandroid.we.chatSDK.firebase.FirebaseErrors.getFirebaseError;

/**
 * Created by benjaminsmiley-andrews on 03/05/2017.
 */

public class FirebaseAuthenticationHandler extends AbstractAuthenticationHandler {

    public Completable authenticateWithCachedToken() {
        return Single.create((SingleOnSubscribe<FirebaseUser>) emitter-> {
                    if (isAuthenticating()) {
                        emitter.onError(ChatError.getError(ChatError.Code.AUTH_IN_PROCESS, "Cant execute two auth in parallel"));
                    } else {
                        setAuthStatus(AuthStatus.CHECKING_IF_AUTH);

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (user != null) {
                            emitter.onSuccess(user);

                        } else {
                            emitter.onError(ChatError.getError(ChatError.Code.NO_AUTH_DATA, "No auth bundle found"));
                        }
                    }
                })
                .flatMapCompletable(this::authenticateWithUser)
                .doOnTerminate(this::setAuthStateToIdle) // Whether we complete successfully or not, we set the status to idle
                .subscribeOn(Schedulers.single());
    }

    @Override
    public Completable authenticate(final AccountDetails details) {
        return Single.create((SingleOnSubscribe<FirebaseUser>)
                emitter->{
                    if (isAuthenticating()) {
                        emitter.onError(ChatError.getError(ChatError.Code.AUTH_IN_PROCESS, "Can't execute two auth in parallel"));
                        return;
                    }

                    setAuthStatus(AuthStatus.AUTH_WITH_MAP);

                    OnCompleteListener<AuthResult> resultHandler = task->AsyncTask.execute(()->{
                        if (task.isComplete() && task.isSuccessful()) {
                            emitter.onSuccess(task.getResult().getUser());
                        } else {
                            emitter.onError(task.getException());
                        }
                    });

                    switch ( details.type ) {
                        case Username:
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(details.username, details.password).addOnCompleteListener(resultHandler);
                            break;
                        case Register:
                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(details.username, details.password).addOnCompleteListener(resultHandler);
                            break;
                        case Anonymous:
                            FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(resultHandler);
                            break;
                        case Custom:
                            FirebaseAuth.getInstance().signInWithCustomToken(details.token).addOnCompleteListener(resultHandler);
                            break;
                        // Should be handled by Social Login Module
                        case Facebook:
                        case Twitter:
                        default:
                            emitter.onError(ChatError.getError(ChatError.Code.NO_LOGIN_TYPE, "No matching login type was found"));
                            break;
                    }
                })
                .flatMapCompletable(this::authenticateWithUser)
                .doOnTerminate(this::setAuthStateToIdle)
                .subscribeOn(Schedulers.single());
    }

    public Completable authenticateWithUser(final FirebaseUser user) {
        return Completable.create(
                e->{
                    final Map<String, Object> loginInfoMap = new HashMap<>();
                    // Save the authentication ID for the current user
                    // Set the current user

                    String uid = user.getUid();

                    loginInfoMap.put(AuthKeys.CurrentUserID, uid);

                    setLoginInfo(loginInfoMap);

                    setAuthStatus(AuthStatus.HANDLING_F_USER);

                    // Do a once() on the user to push its details to firebase.
                    final UserWrapper userWrapper = UserWrapper.initWithAuthData(user);

                    userWrapper.once().subscribe(()->{
                        userWrapper.getModel().update();

                        FirebaseEventHandler.shared().currentUserOn(userWrapper.getModel().getEntityID());

//                        if (ChatSDK.push() != null) {
//                            ChatSDK.push().subscribeToPushChannel(userWrapper.getModel().getPushChannel());
//                        }

                        if (ChatSDK.hook() != null) {
                            HashMap<String, Object> data = new HashMap<>();
                            data.put(HookEvent.User, userWrapper.getModel());
                            ChatSDK.hook().executeHook(HookEvent.DidAuthenticate, data).subscribe(new CrashReportingCompletableObserver());
                        }

                        ChatSDK.core().setUserOnline().subscribe(new CrashReportingCompletableObserver());

                        authenticatedThisSession = true;

                        userWrapper.push().subscribe(e::onComplete, e::onError);
                    }, e::onError);
                })
                .subscribeOn(Schedulers.single());
    }

    public Boolean userAuthenticated() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    @Override
    public Completable changePassword(String email, String oldPassword, final String newPassword) {
        return Completable.create(
                emitter->{
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    OnCompleteListener<Void> resultHandler = task->{
                        if (task.isSuccessful()) {
                            emitter.onComplete();
                        } else {
                            emitter.onError(getFirebaseError(DatabaseError.fromException(task.getException())));
                        }
                    };

                    user.updatePassword(newPassword).addOnCompleteListener(resultHandler);
                })
                .subscribeOn(Schedulers.single());

    }

    public Completable logout() {
        return Completable.create(
                emitter-> {
                    final User user = ChatSDK.currentUser();

                    // Stop listening to user related alerts. (added message or thread.)
                    FirebaseEventHandler.shared().userOff(user.getEntityID());

                    // Removing the push channel
//                    if (ChatSDK.push() != null) {
//                        ChatSDK.push().unsubscribeToPushChannel(user.getPushChannel());
//                    }

                    ChatSDK.hook().executeHook(HookEvent.WillLogout, new HashMap<>()).concatWith(ChatSDK.core().setUserOffline()).subscribe(()->{

                        FirebaseAuth.getInstance().signOut();

                        removeLoginInfo(AuthKeys.CurrentUserID);

                        ChatSDK.events().source().onNext(NetworkEvent.logout());

                        if (ChatSDK.socialLogin() != null) {
                            ChatSDK.socialLogin().logout();
                        }

                        if (ChatSDK.hook() != null) {
                            HashMap<String, Object> data = new HashMap<>();
                            data.put(HookEvent.User, user);
                            ChatSDK.hook().executeHook(HookEvent.DidLogout, data).subscribe(new CrashReportingCompletableObserver());;
                        }

                        authenticatedThisSession = false;

                        emitter.onComplete();
                    }, emitter::onError);

                })
                .subscribeOn(Schedulers.single());
    }

    public Completable sendPasswordResetMail(final String email) {
        return Completable.create(
                emitter->{
                    OnCompleteListener<Void> resultHandler = task->{
                        if (task.isSuccessful()) {
                            emitter.onComplete();
                        } else {
                            emitter.onError(getFirebaseError(DatabaseError.fromException(task.getException())));
                        }
                    };

                    FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(resultHandler);

                }).subscribeOn(Schedulers.single());
    }

    // TODO: Allow users to turn anonymous login off or on in settings
    public Boolean accountTypeEnabled(AccountDetails.Type type) {
        if (type == AccountDetails.Type.Anonymous) {
            return ChatSDK.config().anonymousLoginEnabled;
        } else if (type == AccountDetails.Type.Username || type == AccountDetails.Type.Register) {
            return true;
        } else if (ChatSDK.socialLogin() != null) {
            return ChatSDK.socialLogin().accountTypeEnabled(type);
        } else {
            return false;
        }
    }


}
