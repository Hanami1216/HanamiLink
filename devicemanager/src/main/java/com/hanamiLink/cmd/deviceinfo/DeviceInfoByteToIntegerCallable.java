package com.hanamiLink.cmd.deviceinfo;

import androidx.annotation.NonNull;

/**
 * @deprecated
 * Use class inheriting from {@link com.hanamiLink.cmd.payloadhandler.PayloadHandler}.
 */
@Deprecated
public final class DeviceInfoByteToIntegerCallable extends DeviceInfoCallable<Integer> {

    public DeviceInfoByteToIntegerCallable(@NonNull final byte[] payload) {
        super(payload);
    }

    @Override
    public Integer call() throws Exception {
        final byte[] payload = getPayload();
        if (payload.length == 1) {
            return payload[0] & 0xFF;
        }
        return null;
    }

}
