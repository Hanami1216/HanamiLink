package com.hanamiLink.cmd.notification;

import androidx.annotation.NonNull;

import com.hanamiLink.cmd.payloadhandler.PayloadHandler;

/**
 * @deprecated
 * Use class inheriting from {@link com.hanamiLink.cmd.payloadhandler.PayloadHandler}.
 */
@Deprecated
public abstract class NotificationCallable<T> extends PayloadHandler<T> {

    public NotificationCallable(@NonNull final byte[] payload) {
        super(payload);
    }

}
