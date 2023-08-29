package com.hanamiLink.devicemanager.cmd.request;

import com.hanamiLink.devicemanager.cmd.Request;

public final class ClearPairRecordRequest extends Request {

    public ClearPairRecordRequest() {
        super(COMMAND_CLEAR_PAIR_RECORD);
    }

    @Override
    public byte[] getPayload() {
        return new byte[0];
    }
}
