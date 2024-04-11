package com.goev.auth.repository.auth.impl;

import com.goev.auth.dao.auth.AuthClientDao;
import com.goev.auth.repository.auth.AuthClientRepository;
import com.goev.lib.enums.RecordState;
import com.goev.record.auth.tables.records.AuthClientsRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.goev.record.auth.tables.AuthClients.AUTH_CLIENTS;

@Slf4j
@Repository
@AllArgsConstructor
public class AuthClientRepositoryImpl implements AuthClientRepository {
    private final DSLContext context;


    @Override
    public AuthClientDao save(AuthClientDao client) {
        AuthClientsRecord authClientsRecord =  context.newRecord(AUTH_CLIENTS,client);
        authClientsRecord.store();
        client.setId(authClientsRecord.getId());
        client.setUuid(authClientsRecord.getUuid());
        return client;
    }

    @Override
    public AuthClientDao update(AuthClientDao client) {
        AuthClientsRecord authClientsRecord =  context.newRecord(AUTH_CLIENTS,client);
        authClientsRecord.update();
        return client;
    }

    @Override
    public void delete(Integer id) {
        context.update(AUTH_CLIENTS).set(AUTH_CLIENTS.STATE, RecordState.DELETED.name()).where(AUTH_CLIENTS.ID.eq(id)).execute();
    }

    @Override
    public AuthClientDao findByUUID(String uuid) {
        return context.selectFrom(AUTH_CLIENTS).where(AUTH_CLIENTS.UUID.eq(uuid)).fetchAnyInto(AuthClientDao.class);
    }

    @Override
    public AuthClientDao findById(Integer id) {
        return context.selectFrom(AUTH_CLIENTS).where(AUTH_CLIENTS.ID.eq(id)).fetchAnyInto(AuthClientDao.class);
    }

    @Override
    public List<AuthClientDao> findAllByIds(Integer... ids) {
        return context.selectFrom(AUTH_CLIENTS).where(AUTH_CLIENTS.ID.in(ids)).fetchInto(AuthClientDao.class);
    }

    @Override
    public List<AuthClientDao> findAll() {
        return context.selectFrom(AUTH_CLIENTS).fetchInto(AuthClientDao.class);
    }

    @Override
    public AuthClientDao findByClientIdAndClientSecret(String clientId,String clientSecret) {
        return context.selectFrom(AUTH_CLIENTS)
                .where(AUTH_CLIENTS.CLIENT_KEY.eq(clientId))
                .and(AUTH_CLIENTS.CLIENT_AUTH_SECRET.eq(clientSecret))
                .fetchOneInto(AuthClientDao.class);
    }
}