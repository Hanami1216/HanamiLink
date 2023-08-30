package com.hanamiLink.cmd.request;

import com.hanamiLink.cmd.Request;

public final class ClearPairRecordRequest extends Request {

    public ClearPairRecordRequest() {
        super(COMMAND_CLEAR_PAIR_RECORD);
    }

    @Override
    public byte[] getPayload() {
        return new byte[0];
    }
}
