package com.womandroid.we.chatSDK.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.LayoutRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.womandroid.we.chatSDK.core.dao.User;
import com.womandroid.we.chatSDK.core.dao.Thread;
import com.womandroid.we.chatSDK.core.events.EventType;
import com.womandroid.we.chatSDK.core.events.NetworkEvent;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.core.types.ConnectionType;
import com.womandroid.we.chatSDK.core.utils.DisposableList;
import com.womandroid.we.chatSDK.core.utils.StringChecker;
import com.womandroid.we.R;
import com.womandroid.we.chatSDK.ui.main.BaseFragment;
import com.womandroid.we.chatSDK.ui.utils.AvailabilityHelper;
import com.womandroid.we.chatSDK.ui.utils.ToastHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by ben on 8/15/17.
 */

public class ProfileFragment extends BaseFragment {

    public static int ProfileDetailRowHeight = 25;
    public static int ProfileDetailMargin = 8;

    protected SimpleDraweeView avatarImageView;
    protected ImageView flagImageView;
    protected ImageView availabilityImageView;
    protected TextView nameTextView;
    protected TextView emailTextView;
    protected TextView statusTextView;
    protected TextView locationTextView;
    protected TextView phoneTextView;
    protected TextView followsTextView;
    protected TextView followedTextView;
    protected Button blockOrUnblockButton;
    protected Button addOrDeleteButton;
    protected ImageView followsImageView;
    protected ImageView followedImageView;

    protected ImageView locationImageView;
    protected ImageView phoneImageView;
    protected ImageView emailImageView;

    private DisposableList disposableList = new DisposableList();

    protected User user;

    public static com.womandroid.we.chatSDK.ui.profile.ProfileFragment newInstance() {
        return com.womandroid.we.chatSDK.ui.profile.ProfileFragment.newInstance(null);
    }

    public static com.womandroid.we.chatSDK.ui.profile.ProfileFragment newInstance(User user) {
        com.womandroid.we.chatSDK.ui.profile.ProfileFragment f = new com.womandroid.we.chatSDK.ui.profile.ProfileFragment();
        f.user = user;

        Bundle b = new Bundle();
        f.setArguments(b);
        f.setRetainInstance(true);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        disposableList.add(ChatSDK.events().sourceOnMain().filter(NetworkEvent.filterType(EventType.UserMetaUpdated, EventType.UserPresenceUpdated))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(networkEvent -> {
                    if (networkEvent.user.equals(getUser())) {
                        reloadData();
                    }
                }));

        mainView = inflater.inflate(activityLayout(), null);

        setupTouchUIToDismissKeyboard(mainView, com.womandroid.we.R.id.ivAvatar);

        initViews();

        return mainView;
    }

    protected @LayoutRes int activityLayout() {
        return com.womandroid.we.R.layout.chat_sdk_profile_fragment;
    }

    public void initViews() {
        avatarImageView = mainView.findViewById(com.womandroid.we.R.id.ivAvatar);
        flagImageView = mainView.findViewById(com.womandroid.we.R.id.ivFlag);
        availabilityImageView = mainView.findViewById(com.womandroid.we.R.id.ivAvailability);
        nameTextView = mainView.findViewById(com.womandroid.we.R.id.tvName);
        statusTextView = mainView.findViewById(com.womandroid.we.R.id.tvStatus);

        locationTextView = mainView.findViewById(com.womandroid.we.R.id.tvLocation);
        phoneTextView = mainView.findViewById(com.womandroid.we.R.id.tvPhone);
        emailTextView = mainView.findViewById(com.womandroid.we.R.id.tvEmail);
        followsTextView = mainView.findViewById(com.womandroid.we.R.id.tvFollows);
        followedTextView = mainView.findViewById(com.womandroid.we.R.id.tvFollowed);
        blockOrUnblockButton = mainView.findViewById(com.womandroid.we.R.id.btnBlockOrUnblock);
        addOrDeleteButton = mainView.findViewById(com.womandroid.we.R.id.btnAddOrDelete);

//        followsHeight = followsTextView.getHeight();
//        followedHeight = followedTextView.getHeight();

        locationImageView = mainView.findViewById(com.womandroid.we.R.id.ivLocation);
        phoneImageView = mainView.findViewById(com.womandroid.we.R.id.ivPhone);
        emailImageView = mainView.findViewById(com.womandroid.we.R.id.ivEmail);

        followsImageView = mainView.findViewById(com.womandroid.we.R.id.ivFollows);
        followedImageView = mainView.findViewById(com.womandroid.we.R.id.ivFollowed);

        if (ChatSDK.profilePictures() != null) {
            avatarImageView.setOnClickListener(v -> {
                ChatSDK.profilePictures().startProfilePicturesActivity(getContext(), getUser().getEntityID());
            });
        }

        reloadData();

        addUserMetaUpdatedEventListener();
    }

    protected void addUserMetaUpdatedEventListener() {
        disposableList.add(ChatSDK.events().sourceOnMain()
                .filter(NetworkEvent.filterType(EventType.UserMetaUpdated))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(networkEvent -> {
                    if (networkEvent.user.equals(getUser())) {
                        reloadData();
                    }
                }));
    }

    protected void setViewVisibility(View view, int visibility) {
        if (view != null) view.setVisibility(visibility);
    }

    protected void setViewVisibility(View view, boolean visible) {
        setViewVisibility(view, visible ? View.VISIBLE : View.INVISIBLE);
    }

    protected void setViewText(TextView textView, String text) {
        if (textView != null) textView.setText(text);
    }

    protected void setRowVisible (int textViewID, int imageViewID, boolean visible) {
        setViewVisibility(mainView.findViewById(textViewID), visible);
        setViewVisibility(mainView.findViewById(imageViewID), visible);
    }

    protected void updateBlockedButton(boolean blocked) {
        if (blocked) {
            setViewText(blockOrUnblockButton, getString(com.womandroid.we.R.string.unblock));
        } else {
            setViewText(blockOrUnblockButton, getString(com.womandroid.we.R.string.block));
        }
    }

    protected void updateFriendsButton(boolean friend) {
        if (friend) {
            setViewText(addOrDeleteButton, getString(com.womandroid.we.R.string.delete_contact));
        } else {
            setViewText(addOrDeleteButton, getString(com.womandroid.we.R.string.add_contacts));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        updateInterface();
    }

    protected void block() {
        if (getUser().isMe()) return;

        disposableList.add(ChatSDK.blocking().blockUser(getUser().getEntityID())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    updateBlockedButton(true);
                    updateInterface();
                    ToastHelper.show(getContext(), getString(com.womandroid.we.R.string.user_blocked));
                }, throwable1 -> {
                    ChatSDK.logError(throwable1);
                    Toast.makeText(com.womandroid.we.chatSDK.ui.profile.ProfileFragment.this.getContext(), throwable1.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }));
    }

    protected void unblock() {
        if (getUser().isMe()) return;

        disposableList.add(ChatSDK.blocking().unblockUser(getUser().getEntityID())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    updateBlockedButton(false);
                    updateInterface();
                    ToastHelper.show(getContext(), com.womandroid.we.R.string.user_unblocked);
                }, throwable12 -> {
                    ChatSDK.logError(throwable12);
                    Toast.makeText(com.womandroid.we.chatSDK.ui.profile.ProfileFragment.this.getContext(), throwable12.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }));
    }

    protected void toggleBlocked() {
        if (getUser().isMe()) return;

        boolean blocked = ChatSDK.blocking().isBlocked(getUser().getEntityID());
        if (blocked) unblock();
        else block();
    }

    protected void add() {
        if (getUser().isMe()) return;

        disposableList.add(ChatSDK.contact().addContact(getUser(), ConnectionType.Contact)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    updateFriendsButton(true);
                    ToastHelper.show(getContext(), getString(com.womandroid.we.R.string.contact_added));
                }, throwable -> {
                    ChatSDK.logError(throwable);
                    Toast.makeText(com.womandroid.we.chatSDK.ui.profile.ProfileFragment.this.getContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }));
    }

    protected void delete() {
        if (getUser().isMe()) return;

        disposableList.add(ChatSDK.contact().deleteContact(getUser(), ConnectionType.Contact)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    updateFriendsButton(false);
                    ToastHelper.show(getContext(), getString(com.womandroid.we.R.string.contact_deleted));
                    getActivity().finish();
                }, throwable -> {
                    ChatSDK.logError(throwable);
                    Toast.makeText(com.womandroid.we.chatSDK.ui.profile.ProfileFragment.this.getContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }));
    }

    protected void toggleFriends() {
        if (getUser().isMe()) return;

        boolean friends = ChatSDK.contact().exists(getUser());
        if (friends) delete();
        else add();
    }

    public void updateInterface() {

        User user = getUser();

        if (user == null) return;
        //this.user = user;

        boolean isCurrentUser = user.isMe();
        setHasOptionsMenu(isCurrentUser);

        boolean visible = !isCurrentUser;

        setViewVisibility(followsImageView, visible);
        setViewVisibility(followedImageView, visible);
        setViewVisibility(followsTextView, visible);
        setViewVisibility(followedTextView, visible);
        setViewVisibility(blockOrUnblockButton, visible);
        setViewVisibility(addOrDeleteButton, visible);

        setRowVisible(com.womandroid.we.R.id.ivLocation, com.womandroid.we.R.id.tvLocation, !StringChecker.isNullOrEmpty(user.getLocation()));
        setRowVisible(com.womandroid.we.R.id.ivPhone, com.womandroid.we.R.id.tvPhone, !StringChecker.isNullOrEmpty(user.getPhoneNumber()));
        setRowVisible(com.womandroid.we.R.id.ivEmail, com.womandroid.we.R.id.tvEmail, !StringChecker.isNullOrEmpty(user.getEmail()));
        setRowVisible(com.womandroid.we.R.id.ivFollows, com.womandroid.we.R.id.tvFollows, !StringChecker.isNullOrEmpty(user.getPresenceSubscription()));
        setRowVisible(com.womandroid.we.R.id.ivFollowed, com.womandroid.we.R.id.tvFollowed, !StringChecker.isNullOrEmpty(user.getPresenceSubscription()));

        if (!isCurrentUser) {
            // Find out if the user is blocked already?
            if (ChatSDK.blocking() != null && ChatSDK.blocking().blockingSupported()) {
                updateBlockedButton(ChatSDK.blocking().isBlocked(getUser().getEntityID()));
                if (blockOrUnblockButton != null) blockOrUnblockButton.setOnClickListener(v -> toggleBlocked());
            }
            else {
                // TODO: Set height to zero
                setViewVisibility(blockOrUnblockButton, false);
            }

            updateFriendsButton(ChatSDK.contact().exists(getUser()));
            if (addOrDeleteButton != null) addOrDeleteButton.setOnClickListener(view -> toggleFriends());
        }

        // Country Flag
        String countryCode = getUser().getCountryCode();
        setViewVisibility(flagImageView, false);

        if (countryCode != null && !countryCode.isEmpty()) {
            int flagResourceId = getFlagResId(countryCode);
            if (flagImageView != null && flagResourceId >= 0) {
                flagImageView.setImageResource(flagResourceId);
                setViewVisibility(flagImageView, true);
            }
        }

        // Profile Image
        if (avatarImageView != null) avatarImageView.setImageURI(getUser().getAvatarURL());

        String status = getUser().getStatus();
        if (!StringChecker.isNullOrEmpty(status)) {
            setViewText(statusTextView, status);
        } else {
            setViewText(statusTextView, "");
        }

        // Name
        setViewText(nameTextView, getUser().getName());

        String availability = getUser().getAvailability();

        // Availability
        if (availability != null && !isCurrentUser && availabilityImageView != null) {
            availabilityImageView.setImageResource(AvailabilityHelper.imageResourceIdForAvailability(availability));
            setViewVisibility(availabilityImageView, true);
        } else {
            setViewVisibility(availabilityImageView, false);
        }

        // Location
        setViewText(locationTextView, getUser().getLocation());

        // Phone
        setViewText(phoneTextView, getUser().getPhoneNumber());

        // Email
        setViewText(emailTextView, getUser().getEmail());

//        String presenceSubscription = getUser().getPresenceSubscription();
//
//        boolean follows = false;
//        boolean followed = false;
//        if (presenceSubscription != null) {
//            follows = presenceSubscription.equals("from") || presenceSubscription.equals("both");
//            followed = presenceSubscription.equals("to") || presenceSubscription.equals("both");
//        }
//
//        if (follows) {
//            followsImageView.setMaxHeight(followsHeight);
//            followsTextView.setMaxHeight(followsHeight);
//        }
//        else {
//            followsImageView.setMaxHeight(0);
//            followsTextView.setMaxHeight(0);
//        }
//        if (followed) {
//            followedImageView.setMaxHeight(followedHeight);
//            followedTextView.setMaxHeight(followedHeight);
//        }
//        else {
//            followedImageView.setMaxHeight(0);
//            followedTextView.setMaxHeight(0);
//        }


        ConstraintLayout layout = mainView.findViewById(com.womandroid.we.R.id.mainConstraintLayout);
        ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        ArrayList<Integer> imageViewIds = new ArrayList<>();
        imageViewIds.add(com.womandroid.we.R.id.ivLocation);
        imageViewIds.add(com.womandroid.we.R.id.ivPhone);
        imageViewIds.add(com.womandroid.we.R.id.ivEmail);
        imageViewIds.add(com.womandroid.we.R.id.ivFollows);
        imageViewIds.add(com.womandroid.we.R.id.ivFollowed);

        stackViews(imageViewIds, com.womandroid.we.R.id.tvStatus, set);

        ArrayList<Integer> textViewIds = new ArrayList<>();
        textViewIds.add(com.womandroid.we.R.id.tvLocation);
        textViewIds.add(com.womandroid.we.R.id.tvPhone);
        textViewIds.add(com.womandroid.we.R.id.tvEmail);
        textViewIds.add(com.womandroid.we.R.id.tvFollows);
        textViewIds.add(com.womandroid.we.R.id.tvFollowed);
        textViewIds.add(com.womandroid.we.R.id.btnAddOrDelete);
        textViewIds.add(com.womandroid.we.R.id.btnBlockOrUnblock);

        stackViews(textViewIds, com.womandroid.we.R.id.tvStatus, set);

        set.applyTo(layout);
    }

    protected void stackViews (ArrayList<Integer> viewIds, Integer firstViewId, ConstraintSet set) {
        int lastViewId = firstViewId;
        final float density = getContext().getResources().getDisplayMetrics().density;
        for (int viewId : viewIds) {
            View view = mainView.findViewById(viewId);
            if (view != null && view.getVisibility() == View.VISIBLE) {
                set.connect(viewId, ConstraintSet.TOP, lastViewId, ConstraintSet.BOTTOM, (int) (ProfileDetailMargin * density));
                //set.constrainHeight(viewId, ProfileDetailRowHeight * density);
                lastViewId = viewId;
            }
        }
    }

    protected User getUser () {
        return user != null ? user : ChatSDK.currentUser();
    }

    /**
     * The drawable image name has the format "flag_$countryCode". We need to
     * load the drawable dynamically from country code. Code from
     * http://stackoverflow.com/
     * questions/3042961/how-can-i-get-the-resource-id-of
     * -an-image-if-i-know-its-name
     *
     * @param countryCode
     * @return
     */
    public static int getFlagResId(String countryCode) {
        String drawableName = "flag_"
                + countryCode.toLowerCase(Locale.ENGLISH);

        try {
            Class<R.drawable> res = com.womandroid.we.R.drawable.class;
            Field field = res.getField(drawableName);
            return field.getInt(null);
        } catch (Exception e) {
            ChatSDK.logError(e);
        }
        return -1;
    }

    public void showSettings() {
        ChatSDK.ui().startEditProfileActivity(getContext(), ChatSDK.currentUserID());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (!getUser().isMe())
            return;

        MenuItem item =
                menu.add(Menu.NONE, R.id.action_chat_sdk_settings, 12, "Settings");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setIcon(com.womandroid.we.R.drawable.icn_24_settings);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /* Cant use switch in the library*/
        int id = item.getItemId();

        if (id == com.womandroid.we.R.id.action_chat_sdk_settings)
        {
            showSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposableList.dispose();
    }

    @Override
    public void clearData() {

    }

    @Override
    public void reloadData() {
        updateInterface();
    }

    public void setUser (User user) {
        this.user = user;
    }
}
