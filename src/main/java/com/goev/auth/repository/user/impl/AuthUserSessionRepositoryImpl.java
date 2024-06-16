package com.goev.auth.repository.user.impl;

import com.goev.auth.dao.user.AuthUserSessionDao;
import com.goev.auth.repository.user.AuthUserSessionRepository;
import com.goev.lib.enums.RecordState;
import com.goev.record.auth.tables.records.AuthUserSessionsRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.goev.record.auth.tables.AuthUserSessions.AUTH_USER_SESSIONS;

@Slf4j
@Repository
@AllArgsConstructor
public class AuthUserSessionRepositoryImpl implements AuthUserSessionRepository {

    private final DSLContext context;

    @Override
    public AuthUserSessionDao save(AuthUserSessionDao session) {
        AuthUserSessionsRecord authUserSessionsRecord = context.newRecord(AUTH_USER_SESSIONS, session);
        authUserSessionsRecord.store();
        session.setId(authUserSessionsRecord.getId());
        session.setUuid(authUserSessionsRecord.getUuid());
        return session;
    }

    @Override
    public AuthUserSessionDao update(AuthUserSessionDao session) {
        AuthUserSessionsRecord authUserSessionsRecord = context.newRecord(AUTH_USER_SESSIONS, session);
        authUserSessionsRecord.update();


        session.setCreatedBy(authUserSessionsRecord.getCreatedBy());
        session.setUpdatedBy(authUserSessionsRecord.getUpdatedBy());
        session.setCreatedOn(authUserSessionsRecord.getCreatedOn());
        session.setUpdatedOn(authUserSessionsRecord.getUpdatedOn());
        session.setIsActive(authUserSessionsRecord.getIsActive());
        session.setState(authUserSessionsRecord.getState());
        session.setApiSource(authUserSessionsRecord.getApiSource());
        session.setNotes(authUserSessionsRecord.getNotes());
        return session;
    }

    @Override
    public void delete(Integer id) {
        context.update(AUTH_USER_SESSIONS)
                .set(AUTH_USER_SESSIONS.STATE, RecordState.DELETED.name())
                .where(AUTH_USER_SESSIONS.ID.eq(id))
                .and(AUTH_USER_SESSIONS.STATE.eq(RecordState.ACTIVE.name()))
                .and(AUTH_USER_SESSIONS.IS_ACTIVE.eq(true))
                .execute();
    }

    @Override
    public AuthUserSessionDao findByUUID(String uuid) {
        return context.selectFrom(AUTH_USER_SESSIONS).where(AUTH_USER_SESSIONS.UUID.eq(uuid))
                .and(AUTH_USER_SESSIONS.IS_ACTIVE.eq(true))
                .fetchAnyInto(AuthUserSessionDao.class);
    }

    @Override
    public AuthUserSessionDao findById(Integer id) {
        return context.selectFrom(AUTH_USER_SESSIONS).where(AUTH_USER_SESSIONS.ID.eq(id))
                .and(AUTH_USER_SESSIONS.IS_ACTIVE.eq(true))
                .fetchAnyInto(AuthUserSessionDao.class);
    }

    @Override
    public List<AuthUserSessionDao> findAllByIds(Integer... ids) {
        return context.selectFrom(AUTH_USER_SESSIONS).where(AUTH_USER_SESSIONS.ID.in(ids)).fetchInto(AuthUserSessionDao.class);
    }

    @Override
    public List<AuthUserSessionDao> findAll() {
        return context.selectFrom(AUTH_USER_SESSIONS).fetchInto(AuthUserSessionDao.class);
    }
}