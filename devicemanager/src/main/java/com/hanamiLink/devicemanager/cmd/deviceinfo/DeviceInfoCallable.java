package com.hanamiLink.devicemanager.cmd.deviceinfo;

import androidx.annotation.NonNull;

import com.hanamiLink.devicemanager.cmd.payloadhandler.PayloadHandler;

/**
 * @deprecated
 * Use class inheriting from {@link com.hanamiLink.devicemanager.cmd.payloadhandler.PayloadHandler}.
 */
@Deprecated
public abstract class DeviceInfoCallable<T> extends PayloadHandler<T> {

    public DeviceInfoCallable(@NonNull final byte[] payload) {
        super(payload);
    }

}
