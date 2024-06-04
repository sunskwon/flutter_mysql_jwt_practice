package com.nowhere.be.user.model.dao;

import com.nowhere.be.user.model.dto.LoginUserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    LoginUserDTO findByUsername(String username);
}
