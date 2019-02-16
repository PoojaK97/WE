package com.womandroid.we.chatSDK.ui.chat.options;

import com.womandroid.we.chatSDK.core.rx.ObservableConnector;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.core.types.ChatOptionType;
import com.womandroid.we.chatSDK.core.types.MessageSendProgress;
import com.womandroid.we.chatSDK.ui.chat.LocationSelector;
import com.womandroid.we.chatSDK.ui.chat.options.BaseChatOption;
import com.womandroid.we.chatSDK.ui.utils.ActivityResultPushSubjectHolder;
import com.womandroid.we.chatSDK.ui.utils.ToastHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by ben on 10/11/17.
 */

public class LocationChatOption extends BaseChatOption {

    public LocationChatOption(String title, Integer iconResourceId) {
        super(title, iconResourceId, null, ChatOptionType.SendMessage);


        action = (activity, thread) -> Observable.create((ObservableOnSubscribe<MessageSendProgress>) e -> {
            try {
                final LocationSelector locationSelector = new LocationSelector();

                dispose();

                activityResultDisposable = ActivityResultPushSubjectHolder.shared().subscribe(result1 -> locationSelector.handleResult(activity, result1.requestCode, result1.resultCode, result1.data));

                LocationSelector.Result locationResult = (snapshotPath, latLng) -> {

                    dispose();

                    ObservableConnector<MessageSendProgress> connector = new ObservableConnector<>();
                    connector.connect(ChatSDK.locationMessage().sendMessageWithLocation(snapshotPath, latLng, thread),  e);
                };

                locationSelector.startChooseLocationActivity(activity, locationResult);

            } catch (Exception ex) {
                ToastHelper.show(activity, ex.getLocalizedMessage());
            }
        });
    }

    public LocationChatOption(String title) {
        this(title, null);
    }
}