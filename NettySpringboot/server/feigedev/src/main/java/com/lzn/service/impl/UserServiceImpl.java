package com.lzn.service.impl;

import com.lzn.mapper.UsersMapper;
import com.lzn.pojo.Users;
import com.lzn.service.UserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.*;
import tk.mybatis.mapper.util.Sqls;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Users user = new Users();
        user.setUsername(username);
        Users result = usersMapper.selectOne(user); // 用户名是唯一的
        return result != null?true:false;
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String pwd) {
        Example userExample = new Example(Users.class);
        Criteria criteria = userExample.createCriteria();//条件查询
        criteria.andEqualTo("username", username);
        criteria.andEqualTo("password", pwd);
        Users result = usersMapper.selectOneByExample(userExample);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users saveUser(Users user) {
//        user.setFaceImageBig("");
//        user.setFaceImage("");
        // TODO:为每一个用户生成一个唯一的二维码
        user.setQrcode("");
        String userId = sid.nextShort();// 生成每个用户的唯一id
        user.setId(userId);
        usersMapper.insert(user);
        return user;
    }
}
