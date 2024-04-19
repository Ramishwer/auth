package com.goev.auth.dao.auth;

import com.goev.lib.dao.BaseDao;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthClientCredentialTypeMappingDao extends BaseDao {
    private Integer authClientId;
    private Integer authCredentialTypeId;
    private Integer organizationId;
}
