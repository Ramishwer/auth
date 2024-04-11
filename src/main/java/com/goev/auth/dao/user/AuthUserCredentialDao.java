package com.goev.auth.dao.user;


import com.goev.lib.dao.BaseDao;
import lombok.*;
import org.joda.time.DateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthUserCredentialDao extends BaseDao {
    private Integer authUserId;
    private String authKey;
    private String authSecret;
    private String keycloakUuid;
    private Integer authClientId;
    private DateTime expiryTime;
    private Integer authCredentialTypeId;
}



