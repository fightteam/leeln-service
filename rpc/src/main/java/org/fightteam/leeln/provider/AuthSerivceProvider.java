package org.fightteam.leeln.provider;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import org.fightteam.leeln.core.domain.User;
import org.fightteam.leeln.proto.AuthServiceProto;
import org.fightteam.leeln.proto.RpcProto;
import org.fightteam.leeln.proto.UserServiceProto;
import org.fightteam.leeln.rpc.annotation.RpcServiceProvider;
import org.fightteam.leeln.rpc.utils.BuilderUtils;
import org.fightteam.leeln.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * RPC权限认证提供
 *
 * 分2种权限
 * 第一种白名单，对应 不需要认证就可以通过
 * 第二种没在白名单中的需要认证
 * @author oyach
 * @since 0.0.1
 */
@RpcServiceProvider(service = AuthServiceProto.AuthService.class)
public class AuthSerivceProvider implements AuthServiceProto.AuthService.Interface, AuthServiceProto.AuthService.BlockingInterface{
    private static Map<String, Boolean> node = new HashMap<String, Boolean>();
    private static String[] whiteList = { "127.0.0.1"};

    @Override
    public RpcProto.EmptyMsg auth(RpcController controller, AuthServiceProto.TicketMsg request) throws ServiceException {

        return null;
    }

    @Override
    public void auth(RpcController controller, AuthServiceProto.TicketMsg request, RpcCallback<RpcProto.EmptyMsg> done) {

    }
}
