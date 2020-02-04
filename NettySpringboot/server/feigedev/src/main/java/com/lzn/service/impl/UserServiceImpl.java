package com.lzn.service.impl;

import com.lzn.mapper.UsersMapper;
import com.lzn.org.n3r.idworker.Sid;
import com.lzn.pojo.Users;
import com.lzn.service.UserService;

import com.lzn.utils.FastDFSClient;
import com.lzn.utils.FileUtils;
import com.lzn.utils.QRCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.*;
import tk.mybatis.mapper.util.Sqls;

import java.io.IOException;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private Sid sid;
    @Autowired
    private QRCodeUtils qrCodeUtils;
    @Autowired
    private FastDFSClient fastDFSClient;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Users user = new Users();
        user.setUsername(username);

        Users result = usersMapper.selectOne(user);

        return result != null ? true : false;
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
        // feige_qrcode:[username] 复杂的扫码是有加密
        String userId = sid.nextShort();// 生成每个用户的唯一id
        String qrCodePath = "D://var//user"+userId+"qrcode.png";
        qrCodeUtils.createQRCode(qrCodePath, "feige_qrcode:"+user.getUsername());
        MultipartFile qrcCdeFile = FileUtils.fileToMultipart(qrCodePath);
        String qrCodeUrl = "";
        try {
            qrCodeUrl = fastDFSClient.uploadQRCode(qrcCdeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        user.setQrcode(qrCodeUrl);
        user.setId(userId);
        usersMapper.insert(user);
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(Users user) {
        usersMapper.updateByPrimaryKeySelective(user);
        return queryUserById(user.getId());
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    private Users queryUserById(String userId) {
        return usersMapper.selectByPrimaryKey(userId);
    }

}
