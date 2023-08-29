package com.hanamiLink.devicemanager.cmd.request;

import com.hanamiLink.devicemanager.cmd.Request;

public final class FactoryResetRequest extends Request {

    public FactoryResetRequest() {
        super(COMMAND_FACTORY_RESET);
    }

    @Override
    public byte[] getPayload() {
        return new byte[0];
    }
}
