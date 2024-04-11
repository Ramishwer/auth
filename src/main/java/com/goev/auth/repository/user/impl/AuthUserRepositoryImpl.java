package com.goev.auth.repository.user.impl;

import com.goev.auth.dao.user.AuthUserDao;
import com.goev.auth.repository.user.AuthUserRepository;
import com.goev.lib.enums.RecordState;
import com.goev.record.auth.tables.records.AuthUsersRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.goev.record.auth.tables.AuthUsers.AUTH_USERS;

@Slf4j
@Repository
@AllArgsConstructor
public class AuthUserRepositoryImpl implements AuthUserRepository {
    private final DSLContext context;

    @Override
    public AuthUserDao save(AuthUserDao user) {
        AuthUsersRecord authUsersRecord =  context.newRecord(AUTH_USERS,user);
        authUsersRecord.store();
        user.setId(authUsersRecord.getId());
        user.setUuid(authUsersRecord.getUuid());
        return user;
    }

    @Override
    public AuthUserDao update(AuthUserDao user) {
        AuthUsersRecord authUsersRecord =  context.newRecord(AUTH_USERS,user);
        authUsersRecord.update();
        return user;
    }

    @Override
    public void delete(Integer id) {
        context.update(AUTH_USERS).set(AUTH_USERS.STATE, RecordState.DELETED.name()).where(AUTH_USERS.ID.eq(id)).execute();
    }

    @Override
    public AuthUserDao findByUUID(String uuid) {
        return context.selectFrom(AUTH_USERS).where(AUTH_USERS.UUID.eq(uuid)).fetchAnyInto(AuthUserDao.class);
    }

    @Override
    public AuthUserDao findById(Integer id) {
        return context.selectFrom(AUTH_USERS).where(AUTH_USERS.ID.eq(id)).fetchAnyInto(AuthUserDao.class);
    }

    @Override
    public List<AuthUserDao> findAllByIds(Integer... ids) {
        return context.selectFrom(AUTH_USERS).where(AUTH_USERS.ID.in(ids)).fetchInto(AuthUserDao.class);
    }

    @Override
    public List<AuthUserDao> findAll() {
        return context.selectFrom(AUTH_USERS).fetchInto(AuthUserDao.class);
    }

    @Override
    public AuthUserDao findByPhoneNumber(String phoneNumber) {
        return context.selectFrom(AUTH_USERS).where(AUTH_USERS.PHONE_NUMBER.eq(phoneNumber)).fetchOneInto(AuthUserDao.class);
    }
}