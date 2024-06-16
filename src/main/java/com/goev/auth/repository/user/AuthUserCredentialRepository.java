package com.goev.auth.repository.user;

import com.goev.auth.dao.user.AuthUserCredentialDao;

import java.util.List;

public interface AuthUserCredentialRepository {
    AuthUserCredentialDao save(AuthUserCredentialDao credential);

    AuthUserCredentialDao update(AuthUserCredentialDao credential);

    void delete(Integer id);

    AuthUserCredentialDao findByUUID(String uuid);

    AuthUserCredentialDao findById(Integer id);

    List<AuthUserCredentialDao> findAllByIds(Integer... ids);

    List<AuthUserCredentialDao> findAll();

    AuthUserCredentialDao findByAuthUserIdAndCredentialTypeId(Integer authUserId, Integer credentialTypeId);

    AuthUserCredentialDao findByKeycloakId(String sub);
}
