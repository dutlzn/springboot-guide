package com.lzn.controller;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.lzn.enums.SearchFriendsStatusEnum;
import com.lzn.pojo.Users;
import com.lzn.pojo.bo.UsersBO;
import com.lzn.pojo.vo.UsersVO;
import com.lzn.service.UserService;
import com.lzn.utils.FastDFSClient;
import com.lzn.utils.FileUtils;
import com.lzn.utils.IMoocJSONResult;
import com.lzn.utils.MD5Utils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("u")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FastDFSClient fastDFSClient;

    /**
     * 用户登陆/注册
     *
     * @return
     */
    @PostMapping("/registOrLogin")
    public IMoocJSONResult registOrLogin(@RequestBody Users user) throws Exception {

        // 0. 判断用户名和密码不能为空
        if (StringUtils.isBlank(user.getUsername())
                || StringUtils.isBlank(user.getPassword())) {
            return IMoocJSONResult.errorMsg("用户名或密码不能为空...");
        }

        // 1. 判断用户名是否存在，如果存在就登录，如果不存在则注册
        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());
        Users userResult = null;
        if (usernameIsExist) {
            // 1.1 登录
            userResult = userService.queryUserForLogin(user.getUsername(),
                    MD5Utils.getMD5Str(user.getPassword()));
            if (userResult == null) {
                return IMoocJSONResult.errorMsg("用户名或密码不正确...");
            }
        } else {
            // 1.2 注册
            System.err.println(user.getUsername());
            user.setNickname(user.getUsername());
            user.setFaceImage("");
            user.setFaceImageBig("");
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            userResult = userService.saveUser(user);
        }

        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(userVO, userResult);

        return IMoocJSONResult.ok(userVO);
    }

    /**
     * 上传用户头像
     *
     * @param userBO
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadFaceBase64")
    public IMoocJSONResult uploadFaceBase64(@RequestBody UsersBO userBO) throws Exception {

        // 获取前端传过来的base64字符串, 然后转换为文件对象再上传
        String base64Data = userBO.getFaceData();
        String userFacePath = "D:\\var\\" + userBO.getUserId() + "userface64.png";
        FileUtils.base64ToFile(userFacePath, base64Data);

        // 上传文件到fastdfs
        MultipartFile faceFile = FileUtils.fileToMultipart(userFacePath);
        String url = fastDFSClient.uploadBase64(faceFile);
        System.out.println(url);

//		"dhawuidhwaiuh3u89u98432.png"
//		"dhawuidhwaiuh3u89u98432_80x80.png"

        // 获取缩略图的url
        String thump = "_80x80.";
        String arr[] = url.split("\\.");
        String thumpImgUrl = arr[0] + thump + arr[1];

        // 更细用户头像
        Users user = new Users();
        user.setId(userBO.getUserId());
        user.setFaceImage(thumpImgUrl);
        user.setFaceImageBig(url);

        Users result = userService.updateUserInfo(user);

        return IMoocJSONResult.ok(result);
    }

    /**
     *  修改昵称
     * @param userBO
     * @return
     * @throws Exception
     */
    @PostMapping("/setNickname")
    public IMoocJSONResult setNickname(@RequestBody UsersBO userBO) throws Exception {
        Users user = new Users();
        user.setId(userBO.getUserId());
        user.setNickname(userBO.getNickname());

        Users result = userService.updateUserInfo(user);

        return IMoocJSONResult.ok(result);
    }

    /**
     * 搜索好友接口，根据账号做匹配查询而不是模糊查询
     */
    @PostMapping("/search")
    public IMoocJSONResult searchUser(
            String myUserId,
            String friendUsername
    ) throws Exception {
        // 0.判断myUserId和 friendUsername不能为空
        if (StringUtils.isBlank(myUserId)
                || StringUtils.isBlank(friendUsername)) {
            return IMoocJSONResult.errorMsg("");
        }
        // 前置条件-1.搜索的用户如果不存在 返回无此用户
        // 前置条件-2.搜索的用户如果就是自己 返回不能添加自己
        // 前置条件-3.搜索的用户如果已经添加 返回该用户已经是你的好友
        // 前置条件-4.搜索的用户如果不存在 返回无此用户
        Integer status = userService.preconditionSearchFriends(myUserId, friendUsername);
        if(status == SearchFriendsStatusEnum.SUCCESS.status){
            Users user = userService.queryUserInfoByUsername(friendUsername);
            UsersVO usersVO = new UsersVO();
            BeanUtils.copyProperties(usersVO, user);
            return IMoocJSONResult.ok(usersVO);
        }else{
            String errorMsg =  SearchFriendsStatusEnum.getMsgByKey(status);
            return IMoocJSONResult.errorMsg(errorMsg);
        }
    }
}
