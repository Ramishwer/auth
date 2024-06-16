package com.goev.auth.repository.user;

import com.goev.auth.dao.user.AuthUserSessionDao;

import java.util.List;

public interface AuthUserSessionRepository {
    AuthUserSessionDao save(AuthUserSessionDao session);

    AuthUserSessionDao update(AuthUserSessionDao session);

    void delete(Integer id);

    AuthUserSessionDao findByUUID(String uuid);

    AuthUserSessionDao findById(Integer id);

    List<AuthUserSessionDao> findAllByIds(Integer... ids);

    List<AuthUserSessionDao> findAll();
}
