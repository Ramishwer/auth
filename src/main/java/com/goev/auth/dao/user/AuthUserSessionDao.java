package com.goev.auth.dao.user;


import com.goev.lib.dao.BaseDao;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthUserSessionDao extends BaseDao {
    private String accessToken;
    private String refreshToken;
    private Integer authUserCredentialId;
    private Integer authUserId;
    private Integer expiresIn;
    private Integer refreshExpiresIn;
    private String idToken;
    private Integer organizationId;

}



