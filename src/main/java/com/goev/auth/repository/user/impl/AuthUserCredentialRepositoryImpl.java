package com.goev.auth.repository.user.impl;

import com.goev.auth.dao.user.AuthUserCredentialDao;
import com.goev.auth.repository.user.AuthUserCredentialRepository;
import com.goev.lib.enums.RecordState;
import com.goev.record.auth.tables.records.AuthUserCredentialsRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.goev.record.auth.tables.AuthUserCredentials.AUTH_USER_CREDENTIALS;

@Slf4j
@Repository
@AllArgsConstructor
public class AuthUserCredentialRepositoryImpl implements AuthUserCredentialRepository {

    private final DSLContext context;

    @Override
    public AuthUserCredentialDao save(AuthUserCredentialDao credential) {
        AuthUserCredentialsRecord authUserCredentialsRecord = context.newRecord(AUTH_USER_CREDENTIALS, credential);
        authUserCredentialsRecord.store();
        credential.setId(authUserCredentialsRecord.getId());
        credential.setUuid(authUserCredentialsRecord.getUuid());
        return credential;
    }

    @Override
    public AuthUserCredentialDao update(AuthUserCredentialDao credential) {
        AuthUserCredentialsRecord authUserCredentialsRecord = context.newRecord(AUTH_USER_CREDENTIALS, credential);
        authUserCredentialsRecord.update();


        credential.setCreatedBy(authUserCredentialsRecord.getCreatedBy());
        credential.setUpdatedBy(authUserCredentialsRecord.getUpdatedBy());
        credential.setCreatedOn(authUserCredentialsRecord.getCreatedOn());
        credential.setUpdatedOn(authUserCredentialsRecord.getUpdatedOn());
        credential.setIsActive(authUserCredentialsRecord.getIsActive());
        credential.setState(authUserCredentialsRecord.getState());
        credential.setApiSource(authUserCredentialsRecord.getApiSource());
        credential.setNotes(authUserCredentialsRecord.getNotes());
        return credential;
    }

    @Override
    public void delete(Integer id) {
        context.update(AUTH_USER_CREDENTIALS)
                .set(AUTH_USER_CREDENTIALS.STATE, RecordState.DELETED.name())
                .where(AUTH_USER_CREDENTIALS.ID.eq(id))
                .and(AUTH_USER_CREDENTIALS.STATE.eq(RecordState.ACTIVE.name()))
                .and(AUTH_USER_CREDENTIALS.IS_ACTIVE.eq(true))
                .execute();
    }

    @Override
    public AuthUserCredentialDao findByUUID(String uuid) {
        return context.selectFrom(AUTH_USER_CREDENTIALS).where(AUTH_USER_CREDENTIALS.UUID.eq(uuid))
                .and(AUTH_USER_CREDENTIALS.IS_ACTIVE.eq(true))
                .fetchAnyInto(AuthUserCredentialDao.class);
    }

    @Override
    public AuthUserCredentialDao findById(Integer id) {
        return context.selectFrom(AUTH_USER_CREDENTIALS).where(AUTH_USER_CREDENTIALS.ID.eq(id))
                .and(AUTH_USER_CREDENTIALS.IS_ACTIVE.eq(true))
                .fetchAnyInto(AuthUserCredentialDao.class);
    }

    @Override
    public List<AuthUserCredentialDao> findAllByIds(Integer... ids) {
        return context.selectFrom(AUTH_USER_CREDENTIALS).where(AUTH_USER_CREDENTIALS.ID.in(ids)).fetchInto(AuthUserCredentialDao.class);
    }

    @Override
    public List<AuthUserCredentialDao> findAll() {
        return context.selectFrom(AUTH_USER_CREDENTIALS).fetchInto(AuthUserCredentialDao.class);
    }

    @Override
    public AuthUserCredentialDao findByAuthUserIdAndCredentialTypeId(Integer authUserId, Integer credentialTypeId) {
        return context.selectFrom(AUTH_USER_CREDENTIALS)
                .where(AUTH_USER_CREDENTIALS.AUTH_CREDENTIAL_TYPE_ID.eq(credentialTypeId))
                .and(AUTH_USER_CREDENTIALS.AUTH_USER_ID.eq(authUserId))
                .fetchOneInto(AuthUserCredentialDao.class);
    }

    @Override
    public AuthUserCredentialDao findByKeycloakId(String sub) {
        return context.selectFrom(AUTH_USER_CREDENTIALS)
                .where(AUTH_USER_CREDENTIALS.KEYCLOAK_UUID.eq(sub))
                .fetchOneInto(AuthUserCredentialDao.class);
    }
}