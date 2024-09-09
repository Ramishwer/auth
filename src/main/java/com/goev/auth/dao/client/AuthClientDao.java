package com.goev.auth.dao.client;

import com.goev.lib.dao.BaseDao;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthClientDao extends BaseDao {
    private String clientKey;
    private String clientAuthSecret;
    private String realm;
    private String keycloakUrl;
    private String keycloakAdminKey;
    private String keycloakAdminSecret;
    private Boolean isUserRegistrationAllowed;
    private Integer organizationId;
    private String communicationChannelConfig;
}
