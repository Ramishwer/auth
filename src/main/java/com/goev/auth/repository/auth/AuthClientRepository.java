package com.goev.auth.repository.auth;

import com.goev.auth.dao.auth.AuthClientDao;

import java.util.List;

public interface AuthClientRepository {
    AuthClientDao save(AuthClientDao client);
    AuthClientDao update(AuthClientDao client);
    void delete(Integer id);
    AuthClientDao findByUUID(String uuid);
    AuthClientDao findById(Integer id);
    List<AuthClientDao> findAllByIds(Integer... ids);
    List<AuthClientDao> findAll();
    AuthClientDao findByClientIdAndClientSecret(String clientId, String clientSecret);
}
