package com.goev.auth.repository.auth.impl;

import com.goev.auth.dao.client.AuthClientCredentialTypeMappingDao;
import com.goev.auth.repository.auth.AuthClientCredentialTypeMappingRepository;
import com.goev.lib.enums.RecordState;
import com.goev.record.auth.tables.records.AuthClientCredentialTypeMappingsRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
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
        AuthClientCredentialTypeMappingsRecord authClientCredentialMappingsRecord = context.newRecord(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS, clientCredentialMapping);
        authClientCredentialMappingsRecord.store();
        clientCredentialMapping.setId(authClientCredentialMappingsRecord.getId());
        clientCredentialMapping.setUuid(authClientCredentialMappingsRecord.getUuid());
        return clientCredentialMapping;
    }

    @Override
    public AuthClientCredentialTypeMappingDao update(AuthClientCredentialTypeMappingDao clientCredentialMapping) {
        AuthClientCredentialTypeMappingsRecord authClientCredentialMappingsRecord = context.newRecord(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS, clientCredentialMapping);
        authClientCredentialMappingsRecord.update();


        clientCredentialMapping.setCreatedBy(authClientCredentialMappingsRecord.getCreatedBy());
        clientCredentialMapping.setUpdatedBy(authClientCredentialMappingsRecord.getUpdatedBy());
        clientCredentialMapping.setCreatedOn(authClientCredentialMappingsRecord.getCreatedOn());
        clientCredentialMapping.setUpdatedOn(authClientCredentialMappingsRecord.getUpdatedOn());
        clientCredentialMapping.setIsActive(authClientCredentialMappingsRecord.getIsActive());
        clientCredentialMapping.setState(authClientCredentialMappingsRecord.getState());
        clientCredentialMapping.setApiSource(authClientCredentialMappingsRecord.getApiSource());
        clientCredentialMapping.setNotes(authClientCredentialMappingsRecord.getNotes());
        return clientCredentialMapping;
    }

    @Override
    public void delete(Integer id) {
        context.update(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS)
                .set(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS.STATE, RecordState.DELETED.name())
                .where(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS.ID.eq(id))
                .and(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS.STATE.eq(RecordState.ACTIVE.name()))
                .and(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS.IS_ACTIVE.eq(true))
                .execute();
    }

    @Override
    public AuthClientCredentialTypeMappingDao findByUUID(String uuid) {
        return context.selectFrom(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS).where(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS.UUID.eq(uuid))
                .and(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS.IS_ACTIVE.eq(true))
                .fetchAnyInto(AuthClientCredentialTypeMappingDao.class);
    }

    @Override
    public AuthClientCredentialTypeMappingDao findById(Integer id) {
        return context.selectFrom(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS).where(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS.ID.eq(id))
                .and(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS.IS_ACTIVE.eq(true))
                .fetchAnyInto(AuthClientCredentialTypeMappingDao.class);
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

    @Override
    public List<AuthClientCredentialTypeMappingDao> findByClientId(Integer clientId) {
        return context.selectFrom(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS)
                .where(AUTH_CLIENT_CREDENTIAL_TYPE_MAPPINGS.AUTH_CLIENT_ID.eq(clientId))
                .fetchInto(AuthClientCredentialTypeMappingDao.class);
    }
}