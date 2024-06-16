package com.goev.auth.repository.organization.impl;

import com.goev.auth.dao.OrganizationDao;
import com.goev.auth.repository.organization.OrganizationRepository;
import com.goev.lib.enums.RecordState;
import com.goev.record.auth.tables.records.OrganizationsRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.goev.record.auth.tables.Organizations.ORGANIZATIONS;

@Slf4j
@Repository
@AllArgsConstructor
public class OrganizationRepositoryImpl implements OrganizationRepository {

    private final DSLContext context;

    @Override
    public OrganizationDao save(OrganizationDao credential) {
        OrganizationsRecord organizationsRecord = context.newRecord(ORGANIZATIONS, credential);
        organizationsRecord.store();
        credential.setId(organizationsRecord.getId());
        credential.setUuid(organizationsRecord.getUuid());
        return credential;
    }

    @Override
    public OrganizationDao update(OrganizationDao credential) {
        OrganizationsRecord organizationsRecord = context.newRecord(ORGANIZATIONS, credential);
        organizationsRecord.update();


        credential.setCreatedBy(organizationsRecord.getCreatedBy());
        credential.setUpdatedBy(organizationsRecord.getUpdatedBy());
        credential.setCreatedOn(organizationsRecord.getCreatedOn());
        credential.setUpdatedOn(organizationsRecord.getUpdatedOn());
        credential.setIsActive(organizationsRecord.getIsActive());
        credential.setState(organizationsRecord.getState());
        credential.setApiSource(organizationsRecord.getApiSource());
        credential.setNotes(organizationsRecord.getNotes());
        return credential;
    }

    @Override
    public void delete(Integer id) {
        context.update(ORGANIZATIONS)
                .set(ORGANIZATIONS.STATE, RecordState.DELETED.name())
                .where(ORGANIZATIONS.ID.eq(id))
                .and(ORGANIZATIONS.STATE.eq(RecordState.ACTIVE.name()))
                .and(ORGANIZATIONS.IS_ACTIVE.eq(true))
                .execute();
    }

    @Override
    public OrganizationDao findByUUID(String uuid) {
        return context.selectFrom(ORGANIZATIONS).where(ORGANIZATIONS.UUID.eq(uuid))
                .and(ORGANIZATIONS.IS_ACTIVE.eq(true))
                .fetchAnyInto(OrganizationDao.class);
    }

    @Override
    public OrganizationDao findById(Integer id) {
        return context.selectFrom(ORGANIZATIONS).where(ORGANIZATIONS.ID.eq(id))
                .and(ORGANIZATIONS.IS_ACTIVE.eq(true))
                .fetchAnyInto(OrganizationDao.class);
    }
}
