package com.goev.auth.repository.auth.impl;

import com.goev.auth.dao.auth.AuthClientCredentialTypeMappingDao;
import com.goev.auth.repository.auth.AuthClientCredentialTypeMappingRepository;
import com.goev.lib.enums.RecordState;
import com.goev.record.auth.tables.records.AuthClientCredentialTypeMappingsRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.goev.record.auth.tables.AuthClientCredentialTypeMappings.AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS;

@Slf4j
@Repository
@AllArgsConstructor
public class AuthClientCredentialTypeMappingRepositoryImpl implements AuthClientCredentialTypeMappingRepository {
    private final DSLContext context;

    @Override
    public AuthClientCredentialTypeMappingDao save(AuthClientCredentialTypeMappingDao clientCredentialMapping) {
        AuthClientCredentialTypeMappingsRecord authClientCredentialMappingsRecord =  context.newRecord(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS,clientCredentialMapping);
        authClientCredentialMappingsRecord.store();
        clientCredentialMapping.setId(authClientCredentialMappingsRecord.getId());
        clientCredentialMapping.setUuid(authClientCredentialMappingsRecord.getUuid());
        return clientCredentialMapping;
    }

    @Override
    public AuthClientCredentialTypeMappingDao update(AuthClientCredentialTypeMappingDao clientCredentialMapping) {
        AuthClientCredentialTypeMappingsRecord authClientCredentialMappingsRecord =  context.newRecord(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS,clientCredentialMapping);
        authClientCredentialMappingsRecord.update();
        return clientCredentialMapping;
    }

    @Override
    public void delete(Integer id) {
        context.update(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS).set(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS.STATE, RecordState.DELETED.name()).where(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS.ID.eq(id)).execute();
    }

    @Override
    public AuthClientCredentialTypeMappingDao findByUUID(String uuid) {
        return context.selectFrom(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS).where(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS.UUID.eq(uuid)).fetchAnyInto(AuthClientCredentialTypeMappingDao.class);
    }

    @Override
    public AuthClientCredentialTypeMappingDao findById(Integer id) {
        return context.selectFrom(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS).where(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS.ID.eq(id)).fetchAnyInto(AuthClientCredentialTypeMappingDao.class);
    }

    @Override
    public List<AuthClientCredentialTypeMappingDao> findAllByIds(Integer... ids) {
        return context.selectFrom(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS).where(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS.ID.in(ids)).fetchInto(AuthClientCredentialTypeMappingDao.class);
    }

    @Override
    public List<AuthClientCredentialTypeMappingDao> findAll() {
        return context.selectFrom(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS).fetchInto(AuthClientCredentialTypeMappingDao.class);
    }

    @Override
    public AuthClientCredentialTypeMappingDao findByClientIdAndCredentialTypeId(Integer clientId, Integer credentialTypeId) {
        return context.selectFrom(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS)
                .where(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS.AUTH_CLIENT_ID.eq(clientId))
                .and(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS.AUTH_CREDENTIAL_TYPE_ID.eq(credentialTypeId))
                .fetchOneInto(AuthClientCredentialTypeMappingDao.class);
    }
}