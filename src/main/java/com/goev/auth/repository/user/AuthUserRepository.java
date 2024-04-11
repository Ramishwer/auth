package com.goev.auth.repository.user;

import com.goev.auth.dao.user.AuthUserDao;

import java.util.List;

public interface AuthUserRepository {
    AuthUserDao save(AuthUserDao user);
    AuthUserDao update(AuthUserDao user);
    void delete(Integer id);
    AuthUserDao findByUUID(String uuid);
    AuthUserDao findById(Integer id);
    List<AuthUserDao> findAllByIds(Integer... ids);
    List<AuthUserDao> findAll();
    AuthUserDao findByPhoneNumber(String phoneNumber);
}
