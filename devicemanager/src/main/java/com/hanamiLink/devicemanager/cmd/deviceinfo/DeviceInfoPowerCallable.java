package com.hanamiLink.devicemanager.cmd.deviceinfo;

import androidx.annotation.NonNull;

import com.hanamiLink.devicemanager.models.DevicePower;

/**
 * @deprecated
 * Use class inheriting from {@link com.hanamiLink.devicemanager.cmd.payloadhandler.PayloadHandler}.
 */
@Deprecated
public final class DeviceInfoPowerCallable extends DeviceInfoCallable<DevicePower> {

    public DeviceInfoPowerCallable(@NonNull final byte[] payload) {
        super(payload);
    }

    @Override
    public DevicePower call() throws Exception {
        final byte[] payload = getPayload();
        if (payload.length != 0) {
            return new DevicePower(payload);
        }
        return null;
    }

}
