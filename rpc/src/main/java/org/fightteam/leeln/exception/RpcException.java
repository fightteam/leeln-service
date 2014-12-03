package org.fightteam.leeln.exception;

import org.fightteam.leeln.core.exception.BaseException;
import org.fightteam.leeln.proto.RpcProto;

/**
 * description
 *
 * @author oych
 * @since 0.0.1
 */
public class RpcException extends BaseException {

    private final RpcProto.RpcRequest request;

    public RpcException(Throwable t, RpcProto.RpcRequest request, String message) {
        this(request, message);
        initCause(t);
    }

    public RpcException(RpcProto.RpcRequest request, String message) {
        super(message);
        this.request = request;
    }

    public RpcProto.RpcRequest getRpcRequest() {
        return request;
    }

}

