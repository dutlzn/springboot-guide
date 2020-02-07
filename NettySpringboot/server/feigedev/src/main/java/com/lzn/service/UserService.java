package com.lzn.service;

import com.lzn.netty.ChatMsg;
import com.lzn.pojo.Users;
import com.lzn.pojo.vo.FriendRequestVO;
import com.lzn.pojo.vo.MyFriendsVO;

import java.util.List;

public interface UserService {
    /**
     * 判断用户名是否存在
     *
     * @param username
     * @return
     */
    public boolean queryUsernameIsExist(String username);

    /**
     * 查询用户是否存在
     *
     * @param username
     * @param pwd
     * @return
     */
    public Users queryUserForLogin(String username, String pwd);

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    public Users saveUser(Users user);

    /**
     * @Description: 修改用户记录
     */
    public Users updateUserInfo(Users user);

    /**
     * 搜索朋友的前置条件
     */
    public Integer preconditionSearchFriends(
            String myUserId,
            String friendUsername);

    /**
     * 根据用户名查询用户对象
     */
    public Users queryUserInfoByUsername(String username);

    /**
     * 添加好友请求记录，保存到数据库
     */
    public void sendFriendRequest(String myUserId,String friendUsername);

    /**
     *  查询好友申请
     */
    public List<FriendRequestVO> queryFriendRequestList(String acceptUserId);

    /**
     * @Description: 删除好友请求记录
     */
    public void deleteFriendRequest(String sendUserId, String acceptUserId);

    /**
     * @Description: 通过好友请求
     * 				1. 保存好友
     * 				2. 逆向保存好友
     * 				3. 删除好友请求记录
     */
    public void passFriendRequest(String sendUserId, String acceptUserId);

    /**
     * @Description: 查询好友列表
     */
    public List<MyFriendsVO> queryMyFriends(String userId);

    /**
     * @Description: 保存聊天消息到数据库
     */
    public String saveMsg(ChatMsg chatMsg);


    /**
     * @Description: 批量签收消息
     */
    public void updateMsgSigned(List<String> msgIdList);

}
