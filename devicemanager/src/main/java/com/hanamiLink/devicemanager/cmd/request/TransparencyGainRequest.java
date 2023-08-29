package com.hanamiLink.devicemanager.cmd.request;

import com.hanamiLink.devicemanager.cmd.Request;

public class TransparencyGainRequest extends Request {

    private byte gain;

    public TransparencyGainRequest(byte gain) {
        super(COMMAND_TRANSPARENCY_GAIN);
        this.gain = gain;
    }

    @Override
    public byte[] getPayload() {
        return new byte[] { gain };
    }

}
