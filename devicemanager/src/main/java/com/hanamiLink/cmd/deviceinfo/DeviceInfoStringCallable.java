package com.hanamiLink.cmd.deviceinfo;

import androidx.annotation.NonNull;

/**
 * @deprecated
 * Use class inheriting from {@link com.hanamiLink.cmd.payloadhandler.PayloadHandler}.
 */
@Deprecated
public final class DeviceInfoStringCallable extends DeviceInfoCallable<String> {

    public DeviceInfoStringCallable(@NonNull final byte[] payload) {
        super(payload);
    }

    @Override
    public String call() throws Exception {
        final byte[] payload = getPayload();
        return new String(payload);
    }

}
