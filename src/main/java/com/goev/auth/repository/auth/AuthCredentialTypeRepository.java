package com.goev.auth.repository.auth;

import com.goev.auth.dao.client.AuthCredentialTypeDao;

import java.util.List;

public interface AuthCredentialTypeRepository {
    AuthCredentialTypeDao save(AuthCredentialTypeDao type);

    AuthCredentialTypeDao update(AuthCredentialTypeDao type);

    void delete(Integer id);

    AuthCredentialTypeDao findByUUID(String uuid);

    AuthCredentialTypeDao findById(Integer id);

    List<AuthCredentialTypeDao> findAllByIds(Integer... ids);

    List<AuthCredentialTypeDao> findAll();
}
