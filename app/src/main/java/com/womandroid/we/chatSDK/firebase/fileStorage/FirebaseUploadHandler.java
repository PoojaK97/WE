package com.womandroid.we.chatSDK.firebase.fileStorage;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.womandroid.we.chatSDK.core.base.AbstractUploadHandler;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.core.types.ChatError;
import com.womandroid.we.chatSDK.core.types.FileUploadResult;
import com.womandroid.we.chatSDK.core.utils.StringChecker;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Erk on 26.07.2016.
 */
public class FirebaseUploadHandler extends AbstractUploadHandler {

    public Observable<FileUploadResult> uploadFile(final byte[] data, final String name, final String mimeType) {
        return Observable.create((ObservableOnSubscribe<FileUploadResult>) e -> {

            FirebaseStorage storage = null;
            if(!StringChecker.isNullOrEmpty(ChatSDK.config().firebaseStorageUrl)) {
                storage = FirebaseStorage.getInstance(ChatSDK.config().firebaseStorageUrl);
            }
            else {
                storage = FirebaseStorage.getInstance();
            }
            StorageReference filesRef = storage.getReference().child("files");
            final String fullName = getUUID() + "_" + name;
            StorageReference fileRef = filesRef.child(fullName);

            final FileUploadResult result = new FileUploadResult();

            UploadTask uploadTask = fileRef.putBytes(data);

            uploadTask.addOnProgressListener(taskSnapshot -> {
                result.progress.set(taskSnapshot.getTotalByteCount(), taskSnapshot.getBytesTransferred());

                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.print("Progress: " + progress);

                // TODO: With Firebase this appears to be brokenProfileFragment.newInstance
                e.onNext(result);
            }).addOnSuccessListener(taskSnapshot -> {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        result.name = name;
                        result.mimeType = mimeType;
                        result.url = uri.toString();
                        result.progress.set(taskSnapshot.getTotalByteCount(), taskSnapshot.getTotalByteCount());
                        e.onNext(result);
                        e.onComplete();
                    }
                });
            }).addOnFailureListener(error -> e.onError(ChatError.getError(ChatError.Code.FIREBASE_STORAGE_EXCEPTION, error.getMessage())));

        }).subscribeOn(Schedulers.single());
    }



}
