package com.hanamiLink.devicemanager.cmd.notification;

import androidx.annotation.NonNull;

import com.hanamiLink.devicemanager.cmd.payloadhandler.PayloadHandler;

/**
 * @deprecated
 * Use class inheriting from {@link com.hanamiLink.devicemanager.cmd.payloadhandler.PayloadHandler}.
 */
@Deprecated
public abstract class NotificationCallable<T> extends PayloadHandler<T> {

    public NotificationCallable(@NonNull final byte[] payload) {
        super(payload);
    }

}
