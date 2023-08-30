package com.hanamiLink.cmd.deviceinfo;

import androidx.annotation.NonNull;

import com.hanamiLink.cmd.payloadhandler.PayloadHandler;

/**
 * @deprecated
 * Use class inheriting from {@link com.hanamiLink.cmd.payloadhandler.PayloadHandler}.
 */
@Deprecated
public abstract class DeviceInfoCallable<T> extends PayloadHandler<T> {

    public DeviceInfoCallable(@NonNull final byte[] payload) {
        super(payload);
    }

}
