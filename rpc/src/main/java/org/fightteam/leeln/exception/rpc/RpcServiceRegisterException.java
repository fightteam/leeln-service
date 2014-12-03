package org.fightteam.leeln.exception.rpc;

import com.google.protobuf.ServiceException;
import org.fightteam.leeln.exception.RpcServiceException;
import org.fightteam.leeln.proto.RpcProto;

/**
 * description
 *
 * @author oyach
 * @since 0.0.1
 */
public class RpcServiceRegisterException extends RpcServiceException {

    public RpcServiceRegisterException(ServiceException serviceException, RpcProto.RpcRequest request, String message) {
        super(serviceException, request, message);
    }
}
