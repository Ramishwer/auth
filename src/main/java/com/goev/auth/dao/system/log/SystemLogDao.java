package com.goev.auth.dao.system.log;


import com.goev.lib.dao.BaseDao;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SystemLogDao extends BaseDao {
    private Integer sessionId;
    private Integer deviceId;
    private Integer entityId;
    private String api;
    private String requestData;
    private String queryParams;
    private String applicationSource;
    private String locationDetails;
    private String requestHeaders;
    private String responseStatus;
    private String log;
}



