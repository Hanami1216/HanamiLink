package com.hanamiLink.cmd.request;

import com.hanamiLink.cmd.Request;

public final class FindDeviceRequest extends Request {

    private final boolean enable;

    public FindDeviceRequest(boolean enable) {
        super(COMMAND_FIND_DEVICE);
        this.enable = enable;
    }

    @Override
    public byte[] getPayload() {
        byte payload = (byte) (enable ? 0x01 : 0x00);
        return new byte[] { payload };
    }
}
