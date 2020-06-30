package com.yale.persistence.test.dao;

import com.yale.persistence.test.entity.User;

import java.util.List;

public interface UserDao {

    List<User> selectList();

    User selectOne(User user);

    int insert(User user);

    int update(User user);

    int deleteByUser(User user);
}
