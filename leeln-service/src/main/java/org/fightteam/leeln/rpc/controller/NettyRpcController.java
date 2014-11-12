package org.fightteam.leeln.rpc.controller;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

/**
 * description
 *
 * @author oych
 * @since 0.0.1
 */
public class NettyRpcController implements RpcController {

    private String reason;
    private boolean failed;
    private boolean canceled;
    private RpcCallback<Object> callback;

    public String errorText() {
        return reason;
    }

    public boolean failed() {
        return failed;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void notifyOnCancel(RpcCallback<Object> callback) {
        this.callback = callback;
    }

    public void reset() {
        reason = null;
        failed = false;
        canceled = false;
        callback = null;
    }

    public void setFailed(String reason) {
        this.reason = reason;
        this.failed = true;
    }

    public void startCancel() {
        canceled = true;
    }

}

