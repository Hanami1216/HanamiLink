package com.hanamiLink.devicemanager.cmd.request;

import com.hanamiLink.devicemanager.cmd.Request;

public class AncGainRequest extends Request {

    private byte gain;

    public AncGainRequest(byte gain) {
        super(COMMAND_ANC_GAIN);
        this.gain = gain;
    }

    @Override
    public byte[] getPayload() {
        return new byte[] { gain };
    }
}
