package com.lzn.service.impl;

import com.lzn.enums.SearchFriendsStatusEnum;
import com.lzn.mapper.MyFriendsMapper;
import com.lzn.mapper.UsersMapper;
import com.lzn.org.n3r.idworker.Sid;
import com.lzn.pojo.MyFriends;
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
    @Autowired
    private MyFriendsMapper myFriendsMapper;

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
    @Transactional(propagation = Propagation.SUPPORTS)
    private Users queryUserById(String userId) {
        return usersMapper.selectByPrimaryKey(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(Users user) {
        usersMapper.updateByPrimaryKeySelective(user);
        return queryUserById(user.getId());
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer preconditionSearchFriends(
            String myUserId, String friendUsername) {
        // 前置条件-1.搜索的用户如果不存在 返回无此用户
        Users user = queryUserInfoByUsername(friendUsername);
        if(user == null){
            return SearchFriendsStatusEnum.USER_NOT_EXIST.status;
        }
        // 前置条件-2.搜索的用户如果就是自己 返回不能添加自己
        if(user.getId() == myUserId){
            return SearchFriendsStatusEnum.NOT_YOURSELF.status;
        }
        // 前置条件-3.搜索的用户如果已经添加 返回该用户已经是你的好友
        Example mfe = new Example(MyFriends.class);
        Criteria mfc = mfe.createCriteria();//创建查询条件
        mfc.andEqualTo("myUserId",myUserId);
        mfc.andEqualTo("myFriendUserId",user.getId());
        MyFriends myFriends1 = myFriendsMapper.selectOneByExample(mfe);
        if(myFriends1 != null){
            return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
        }
        return SearchFriendsStatusEnum.SUCCESS.status;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfoByUsername(String username){
        Example ue = new Example(Users.class);
        Criteria uc = ue.createCriteria();//创建查询条件
        uc.andEqualTo("username", username);
        return usersMapper.selectOneByExample(ue);
    }

}
