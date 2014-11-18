package org.fightteam.leeln.service.impl;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import org.fightteam.leeln.core.User;
import org.fightteam.leeln.proto.UserServiceProto;
import org.fightteam.leeln.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户业务逻辑实现类
 * <p/>
 * 默认不开启事务
 *
 * @author oych
 * @since 0.0.1
 */
//@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
public class UserServiceImpl implements UserServiceProto.UserService.Interface, UserServiceProto.UserService.BlockingInterface {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    private UserRepository userRepository;


    @Override
    public UserServiceProto.UserMsg findByUsername(RpcController controller, UserServiceProto.UsernameMsg request) throws ServiceException {
        String username = request.getUsername();
        User user = userRepository.findByUsername(username);

        UserServiceProto.UserMsg userMsg = UserServiceProto.UserMsg.newBuilder()
                .setUsername(user.getUsername())
                .setNickname(user.getNickname())
                .setId(user.getId())
                .build();

        return userMsg;
    }

    @Override
    public UserServiceProto.UserMsg findByNickname(RpcController controller, UserServiceProto.NicknameMsg request) throws ServiceException {
        String nickname = request.getNickname();
        User user = userRepository.findByUsername(nickname);

        UserServiceProto.UserMsg userMsg = UserServiceProto.UserMsg.newBuilder()
                .setUsername(user.getUsername())
                .setNickname(user.getNickname())
                .setId(user.getId())
                .build();

        return userMsg;
    }

    @Override
    public UserServiceProto.UsersMsg findAll(RpcController controller, UserServiceProto.EmptyMsg request) throws ServiceException {

        List<User> users = userRepository.findAll();

        List<UserServiceProto.UserMsg> userMsgs = new ArrayList<UserServiceProto.UserMsg>(users.size());

        for (User user : users){
            UserServiceProto.UserMsg userMsg = UserServiceProto.UserMsg.newBuilder()
                    .setUsername(user.getUsername())
                    .setNickname(user.getNickname())
                    .setId(user.getId())
                    .build();
            userMsgs.add(userMsg);
        }

        UserServiceProto.UsersMsg usersMsg = UserServiceProto.UsersMsg.newBuilder().addAllUsers(userMsgs).build();

        return usersMsg;
    }

    @Override
    public UserServiceProto.UserMsg findById(RpcController controller, UserServiceProto.IdMsg request) throws ServiceException {
        long id = request.getId();
        User user = userRepository.findById(id);

        UserServiceProto.UserMsg userMsg = UserServiceProto.UserMsg.newBuilder()
                .setUsername(user.getUsername())
                .setNickname(user.getNickname())
                .setId(user.getId())
                .build();

        return userMsg;
    }

    @Override
    public UserServiceProto.EmptyMsg insert(RpcController controller, UserServiceProto.InsertMsg request) throws ServiceException {
        User user = new User();

        user.setNickname(request.getNickname());
        user.setUsername(request.getUsername());

        userRepository.save(user);
        UserServiceProto.EmptyMsg emptyMsg = UserServiceProto.EmptyMsg.getDefaultInstance();
        return emptyMsg;
    }

    @Override
    public UserServiceProto.EmptyMsg update(RpcController controller, UserServiceProto.UserMsg request) throws ServiceException {
        User user = new User();

        user.setNickname(request.getNickname());
        user.setUsername(request.getUsername());
        user.setId(request.getId());
        userRepository.update(user);

        UserServiceProto.EmptyMsg emptyMsg = UserServiceProto.EmptyMsg.getDefaultInstance();
        return emptyMsg;
    }

    @Override
    public UserServiceProto.EmptyMsg delete(RpcController controller, UserServiceProto.IdMsg request) throws ServiceException {
        long id = request.getId();

        userRepository.delete(id);

        UserServiceProto.EmptyMsg emptyMsg = UserServiceProto.EmptyMsg.getDefaultInstance();
        return emptyMsg;
    }

    @Override
    public void findByUsername(RpcController controller, UserServiceProto.UsernameMsg request, RpcCallback<UserServiceProto.UserMsg> done) {

        UserServiceProto.UserMsg userMsg = null;
        try {
            userMsg = findByUsername(controller, request);
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

        if (done != null) {
            done.run(userMsg);
        } else {
            if (userMsg == null) {
                log.error("error occured");
            } else {
                log.info("response user is success");
            }
        }
    }

    @Override
    public void findByNickname(RpcController controller, UserServiceProto.NicknameMsg request, RpcCallback<UserServiceProto.UserMsg> done) {
        UserServiceProto.UserMsg userMsg = null;
        try {
            userMsg = findByNickname(controller, request);
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

        if (done != null) {
            done.run(userMsg);
        } else {
            if (userMsg == null) {
                log.error("error occured");
            } else {
                log.info("response user is success");
            }
        }
    }

    @Override
    public void findAll(RpcController controller, UserServiceProto.EmptyMsg request, RpcCallback<UserServiceProto.UsersMsg> done) {
        UserServiceProto.UsersMsg usersMsg = null;
        try {
            usersMsg = findAll(controller, request);
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

        if (done != null) {
            done.run(usersMsg);
        } else {
            if (usersMsg == null) {
                log.error("error occured");
            } else {
                log.info("response user is success");
            }
        }
    }

    @Override
    public void findById(RpcController controller, UserServiceProto.IdMsg request, RpcCallback<UserServiceProto.UserMsg> done) {
        UserServiceProto.UserMsg userMsg = null;
        try {
            userMsg = findById(controller, request);
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

        if (done != null) {
            done.run(userMsg);
        } else {
            if (userMsg == null) {
                log.error("error occured");
            } else {
                log.info("response user is success");
            }
        }
    }

    @Override
    public void insert(RpcController controller, UserServiceProto.InsertMsg request, RpcCallback<UserServiceProto.EmptyMsg> done) {
        UserServiceProto.EmptyMsg emptyMsg = null;
        try {
            emptyMsg = insert(controller, request);
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

        if (done != null) {
            done.run(emptyMsg);
        } else {
            if (emptyMsg == null) {
                log.error("error occured");
            } else {
                log.info("response user is success");
            }
        }
    }

    @Override
    public void update(RpcController controller, UserServiceProto.UserMsg request, RpcCallback<UserServiceProto.EmptyMsg> done) {
        UserServiceProto.EmptyMsg emptyMsg = null;
        try {
            emptyMsg = update(controller, request);
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

        if (done != null) {
            done.run(emptyMsg);
        } else {
            if (emptyMsg == null) {
                log.error("error occured");
            } else {
                log.info("response user is success");
            }
        }
    }

    @Override
    public void delete(RpcController controller, UserServiceProto.IdMsg request, RpcCallback<UserServiceProto.EmptyMsg> done) {
        UserServiceProto.EmptyMsg emptyMsg = null;
        try {
            emptyMsg = delete(controller, request);
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

        if (done != null) {
            done.run(emptyMsg);
        } else {
            if (emptyMsg == null) {
                log.error("error occured");
            } else {
                log.info("response user is success");
            }
        }
    }
}
