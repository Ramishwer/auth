package com.goev.auth.repository.system.property.impl;

import com.goev.auth.dao.system.property.SystemPropertyDao;
import com.goev.auth.repository.system.property.SystemPropertyRepository;
import com.goev.lib.enums.RecordState;
import com.goev.record.auth.tables.records.SystemPropertiesRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.goev.record.auth.tables.SystemProperties.SYSTEM_PROPERTIES;

@Slf4j
@Repository
@AllArgsConstructor
public class SystemPropertyRepositoryImpl implements SystemPropertyRepository {
    private final DSLContext context;

    @Override
    public Map<String, SystemPropertyDao> getPropertyMap() {
        return context.select().from(SYSTEM_PROPERTIES)
                .where(SYSTEM_PROPERTIES.IS_ACTIVE.eq(true))
                .fetchMap(SYSTEM_PROPERTIES.PROPERTY_NAME, SystemPropertyDao.class);
    }

    @Override
    public SystemPropertyDao save(SystemPropertyDao property) {
        SystemPropertiesRecord systemPropertiesRecord = context.newRecord(SYSTEM_PROPERTIES, property);
        systemPropertiesRecord.store();
        property.setId(systemPropertiesRecord.getId());
        return property;
    }

    @Override
    public SystemPropertyDao update(SystemPropertyDao property) {
        SystemPropertiesRecord systemPropertiesRecord = context.newRecord(SYSTEM_PROPERTIES, property);
        systemPropertiesRecord.update();


        property.setCreatedBy(systemPropertiesRecord.getCreatedBy());
        property.setUpdatedBy(systemPropertiesRecord.getUpdatedBy());
        property.setCreatedOn(systemPropertiesRecord.getCreatedOn());
        property.setUpdatedOn(systemPropertiesRecord.getUpdatedOn());
        property.setIsActive(systemPropertiesRecord.getIsActive());
        property.setState(systemPropertiesRecord.getState());
        property.setApiSource(systemPropertiesRecord.getApiSource());
        property.setNotes(systemPropertiesRecord.getNotes());
        return property;
    }

    @Override
    public void delete(Integer id) {
        context.update(SYSTEM_PROPERTIES)
                .set(SYSTEM_PROPERTIES.STATE, RecordState.DELETED.name())
                .where(SYSTEM_PROPERTIES.ID.eq(id))
                .and(SYSTEM_PROPERTIES.STATE.eq(RecordState.ACTIVE.name()))
                .and(SYSTEM_PROPERTIES.IS_ACTIVE.eq(true))
                .execute();
    }

    @Override
    public SystemPropertyDao findByUUID(String uuid) {
        return context.selectFrom(SYSTEM_PROPERTIES).where(SYSTEM_PROPERTIES.UUID.eq(uuid))
                .and(SYSTEM_PROPERTIES.IS_ACTIVE.eq(true))
                .fetchAnyInto(SystemPropertyDao.class);
    }

    @Override
    public SystemPropertyDao findById(Integer id) {
        return context.selectFrom(SYSTEM_PROPERTIES).where(SYSTEM_PROPERTIES.ID.eq(id))
                .and(SYSTEM_PROPERTIES.IS_ACTIVE.eq(true))
                .fetchAnyInto(SystemPropertyDao.class);
    }

    @Override
    public List<SystemPropertyDao> findAllByIds(Integer... ids) {
        return context.selectFrom(SYSTEM_PROPERTIES).where(SYSTEM_PROPERTIES.ID.in(ids)).fetchInto(SystemPropertyDao.class);
    }

    @Override
    public List<SystemPropertyDao> findAll() {
        return context.selectFrom(SYSTEM_PROPERTIES).fetchInto(SystemPropertyDao.class);
    }
}