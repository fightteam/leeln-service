package org.fightteam.leeln.exception.rpc;

import org.fightteam.leeln.exception.RpcException;
import org.fightteam.leeln.proto.RpcProto;

/**
 * description
 *
 * @author oyach
 * @since 0.0.1
 */
public class RpcAuthenticationException extends RpcException {

    public RpcAuthenticationException(Throwable t, RpcProto.RpcRequest request, String message) {
        super(t, request, message);
    }

    public RpcAuthenticationException(RpcProto.RpcRequest request, String message) {
        super(request, message);
    }
}
