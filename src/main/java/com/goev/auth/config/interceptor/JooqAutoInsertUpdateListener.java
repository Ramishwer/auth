package com.goev.auth.config.interceptor;

import com.goev.auth.utilities.RequestContext;
import com.goev.lib.utilities.ApplicationContext;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordContext;
import org.jooq.RecordListener;

import java.util.UUID;

@Slf4j
public class JooqAutoInsertUpdateListener implements RecordListener {

    public static final String CREATED_TIMESTAMP = "created_timestamp";
    public static final String UPDATED_TIMESTAMP = "updated_timestamp";
    public static final String CREATED_ON = "created_on";
    public static final String UPDATED_ON = "updated_on";
    public static final String CREATED_BY = "created_by";
    public static final String UPDATED_BY = "updated_by";
    public static final String API_SOURCE = "api_source";
    public static final String UUID_KEY = "uuid";
    public static final String ORGANIZATION_ID_KEY = "organization_id";

    @Override
    public void insertStart(RecordContext ctx) {

        String authUUID = ApplicationContext.getAuthUUID();
        Record rowData = ctx.record();

        if (rowData.field(CREATED_TIMESTAMP) != null && rowData.get(rowData.field(CREATED_TIMESTAMP)) == null)
            rowData.set((Field<? super DateTime>) rowData.field(CREATED_TIMESTAMP), DateTime.now());

        if (rowData.field(CREATED_ON) != null && rowData.get(rowData.field(CREATED_ON)) == null)
            rowData.set((Field<? super DateTime>) rowData.field(CREATED_ON), DateTime.now());

        if (rowData.field(UPDATED_TIMESTAMP) != null)
            rowData.set((Field<? super DateTime>) rowData.field(UPDATED_TIMESTAMP), DateTime.now());

        if (rowData.field(UPDATED_ON) != null)
            rowData.set((Field<? super DateTime>) rowData.field(UPDATED_ON), DateTime.now());

        if (rowData.field(CREATED_BY) != null && rowData.get(rowData.field(CREATED_BY)) == null)
            rowData.set((Field<? super String>) rowData.field(CREATED_BY), authUUID);

        if (rowData.field(UPDATED_BY) != null)
            rowData.set((Field<? super String>) rowData.field(UPDATED_BY), authUUID);

        if (rowData.field(API_SOURCE) != null)
            rowData.set((Field<? super String>) rowData.field(API_SOURCE), ApplicationContext.getApplicationSource());

        if (rowData.field(UUID_KEY) != null)
            rowData.set((Field<? super String>) rowData.field(UUID_KEY), UUID.randomUUID().toString());

        if (rowData.field(ORGANIZATION_ID_KEY) != null && RequestContext.getOrganizationId()!=null)
            rowData.set((Field<? super String>) rowData.field(ORGANIZATION_ID_KEY),RequestContext.getOrganizationId().toString());
    }

    @Override
    public void updateStart(RecordContext ctx) {
        String authUUID = ApplicationContext.getAuthUUID();
        Record rowData = ctx.record();
        if (rowData.field(UPDATED_TIMESTAMP) != null)
            rowData.set((Field<? super DateTime>) rowData.field(UPDATED_TIMESTAMP), DateTime.now());

        if (rowData.field(UPDATED_ON) != null)
            rowData.set((Field<? super DateTime>) rowData.field(UPDATED_ON), DateTime.now());

        if (rowData.field(UPDATED_BY) != null)
            rowData.set((Field<? super String>) rowData.field(UPDATED_BY), authUUID);

        if (rowData.field(API_SOURCE) != null)
            rowData.set((Field<? super String>) rowData.field(API_SOURCE), ApplicationContext.getApplicationSource());
    }
}
