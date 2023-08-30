package com.hanamiLink.cmd.response;

import androidx.annotation.NonNull;

import com.hanamiLink.cmd.payloadhandler.PayloadHandler;

/**
 * @deprecated
 * Use class inheriting from {@link com.hanamiLink.cmd.payloadhandler.PayloadHandler}.
 */
@Deprecated
public abstract class ResponseCallable<T> extends PayloadHandler<T> {

    public ResponseCallable(@NonNull byte[] payload) {
        super(payload);
    }

}
