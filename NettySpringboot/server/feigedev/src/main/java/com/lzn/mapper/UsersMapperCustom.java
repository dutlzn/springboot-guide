package com.lzn.mapper;


import com.lzn.pojo.Users;
import com.lzn.pojo.vo.FriendRequestVO;
import com.lzn.utils.MyMapper;

import java.util.List;

public interface UsersMapperCustom extends MyMapper<Users> {
    public List<FriendRequestVO> queryFriendRequestList(String acceptUserId);
}