package com.goev.auth.repository.auth;

import com.goev.auth.dao.client.AuthClientCredentialTypeMappingDao;

import java.util.List;

public interface AuthClientCredentialTypeMappingRepository {
    AuthClientCredentialTypeMappingDao save(AuthClientCredentialTypeMappingDao mapping);

    AuthClientCredentialTypeMappingDao update(AuthClientCredentialTypeMappingDao mapping);

    void delete(Integer id);

    AuthClientCredentialTypeMappingDao findByUUID(String uuid);

    AuthClientCredentialTypeMappingDao findById(Integer id);

    List<AuthClientCredentialTypeMappingDao> findAllByIds(Integer... ids);

    List<AuthClientCredentialTypeMappingDao> findAll();

    AuthClientCredentialTypeMappingDao findByClientIdAndCredentialTypeId(Integer clientId, Integer credentialTypeId);

    List<AuthClientCredentialTypeMappingDao> findByClientId(Integer clientId);
}
