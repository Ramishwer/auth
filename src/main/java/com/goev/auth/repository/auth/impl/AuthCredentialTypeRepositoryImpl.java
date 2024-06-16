package com.goev.auth.repository.auth.impl;

import com.goev.auth.dao.client.AuthCredentialTypeDao;
import com.goev.auth.repository.auth.AuthCredentialTypeRepository;
import com.goev.lib.enums.RecordState;
import com.goev.record.auth.tables.records.AuthCredentialTypesRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.goev.record.auth.tables.AuthCredentialTypes.AUTH_CREDENTIAL_TYPES;

@Slf4j
@Repository
@AllArgsConstructor
public class AuthCredentialTypeRepositoryImpl implements AuthCredentialTypeRepository {
    private final DSLContext context;


    @Override
    public AuthCredentialTypeDao save(AuthCredentialTypeDao credentialType) {
        AuthCredentialTypesRecord authCredentialTypesRecord = context.newRecord(AUTH_CREDENTIAL_TYPES, credentialType);
        authCredentialTypesRecord.store();
        credentialType.setId(authCredentialTypesRecord.getId());
        credentialType.setUuid(authCredentialTypesRecord.getUuid());
        return credentialType;
    }

    @Override
    public AuthCredentialTypeDao update(AuthCredentialTypeDao credentialType) {
        AuthCredentialTypesRecord authCredentialTypesRecord = context.newRecord(AUTH_CREDENTIAL_TYPES, credentialType);
        authCredentialTypesRecord.update();


        credentialType.setCreatedBy(authCredentialTypesRecord.getCreatedBy());
        credentialType.setUpdatedBy(authCredentialTypesRecord.getUpdatedBy());
        credentialType.setCreatedOn(authCredentialTypesRecord.getCreatedOn());
        credentialType.setUpdatedOn(authCredentialTypesRecord.getUpdatedOn());
        credentialType.setIsActive(authCredentialTypesRecord.getIsActive());
        credentialType.setState(authCredentialTypesRecord.getState());
        credentialType.setApiSource(authCredentialTypesRecord.getApiSource());
        credentialType.setNotes(authCredentialTypesRecord.getNotes());
        return credentialType;
    }

    @Override
    public void delete(Integer id) {
        context.update(AUTH_CREDENTIAL_TYPES)
                .set(AUTH_CREDENTIAL_TYPES.STATE, RecordState.DELETED.name())
                .where(AUTH_CREDENTIAL_TYPES.ID.eq(id))
                .and(AUTH_CREDENTIAL_TYPES.STATE.eq(RecordState.ACTIVE.name()))
                .and(AUTH_CREDENTIAL_TYPES.IS_ACTIVE.eq(true))
                .execute();
    }

    @Override
    public AuthCredentialTypeDao findByUUID(String uuid) {
        return context.selectFrom(AUTH_CREDENTIAL_TYPES).where(AUTH_CREDENTIAL_TYPES.UUID.eq(uuid))
                .and(AUTH_CREDENTIAL_TYPES.IS_ACTIVE.eq(true))
                .fetchAnyInto(AuthCredentialTypeDao.class);
    }

    @Override
    public AuthCredentialTypeDao findById(Integer id) {
        return context.selectFrom(AUTH_CREDENTIAL_TYPES).where(AUTH_CREDENTIAL_TYPES.ID.eq(id))
                .and(AUTH_CREDENTIAL_TYPES.IS_ACTIVE.eq(true))
                .fetchAnyInto(AuthCredentialTypeDao.class);
    }

    @Override
    public List<AuthCredentialTypeDao> findAllByIds(Integer... ids) {
        return context.selectFrom(AUTH_CREDENTIAL_TYPES).where(AUTH_CREDENTIAL_TYPES.ID.in(ids)).fetchInto(AuthCredentialTypeDao.class);
    }

    @Override
    public List<AuthCredentialTypeDao> findAll() {
        return context.selectFrom(AUTH_CREDENTIAL_TYPES).fetchInto(AuthCredentialTypeDao.class);
    }
}