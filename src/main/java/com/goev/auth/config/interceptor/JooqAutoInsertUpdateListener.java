package com.goev.auth.config.interceptor;

import com.goev.lib.utilities.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordContext;
import org.jooq.RecordListener;

@Slf4j
public class JooqAutoInsertUpdateListener implements RecordListener {

    public static final String CREATED_TIMESTAMP = "created_timestamp";
    public static final String UPDATED_TIMESTAMP = "updated_timestamp";
    public static final String CREATED_ON = "created_on";
    public static final String UPDATED_ON = "updated_on";
    public static final String CREATED_BY = "created_by";
    public static final String UPDATED_BY = "updated_by";
    public static final String API_SOURCE = "api_source";

    @Override
    public void insertStart(RecordContext ctx) {

        String ssoId = RequestContext.getAuthId();
        Record record = ctx.record();

        if (record.field(CREATED_TIMESTAMP) != null && record.get(record.field(CREATED_TIMESTAMP))==null)
            record.set((Field<? super DateTime>) record.field(CREATED_TIMESTAMP), DateTime.now());
        else if (record.field(CREATED_ON) != null && record.get(record.field(CREATED_ON))==null)
            record.set((Field<? super DateTime>) record.field(CREATED_ON), DateTime.now());

        if (record.field(UPDATED_TIMESTAMP) != null)
            record.set((Field<? super DateTime>) record.field(UPDATED_TIMESTAMP), DateTime.now());
        else if (record.field(UPDATED_ON) != null)
            record.set((Field<? super DateTime>) record.field(UPDATED_ON), DateTime.now());

        if (record.field(CREATED_BY) != null && record.get(record.field(CREATED_BY))==null)
            record.set((Field<? super String>) record.field(CREATED_BY), ssoId);

        if (record.field(UPDATED_BY) != null)
            record.set((Field<? super String>) record.field(UPDATED_BY), ssoId);
        if (record.field(API_SOURCE) != null)
            record.set((Field<? super String>) record.field(API_SOURCE), RequestContext.getApplicationSource());
    }

    @Override
    public void updateStart(RecordContext ctx) {
        String ssoId = RequestContext.getAuthId();
        Record record = ctx.record();
        if (record.field(UPDATED_TIMESTAMP) != null)
            record.set((Field<? super DateTime>) record.field(UPDATED_TIMESTAMP), DateTime.now());
        else if (record.field(UPDATED_ON) != null)
            record.set((Field<? super DateTime>) record.field(UPDATED_ON), DateTime.now());

        if (record.field(UPDATED_BY) != null)
            record.set((Field<? super String>) record.field(UPDATED_BY), ssoId);
        if (record.field(API_SOURCE) != null)
            record.set((Field<? super String>) record.field(API_SOURCE), RequestContext.getApplicationSource());
    }
}
