package com.hanamiLink.cmd.deviceinfo;

import androidx.annotation.NonNull;

/**
 * @deprecated
 * Use class inheriting from {@link com.hanamiLink.cmd.payloadhandler.PayloadHandler}.
 */
@Deprecated
public final class DeviceInfoByteArrayCallable extends DeviceInfoCallable<byte[]> {

    public DeviceInfoByteArrayCallable(@NonNull final byte[] payload) {
        super(payload);
    }

    @Override
    public byte[] call() throws Exception {
        return getPayload();
    }

}
