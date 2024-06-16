package com.goev.auth.dao.client;

import com.goev.lib.dao.BaseDao;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthCredentialTypeDao extends BaseDao {
    private String name;
    private String description;
    private Integer organizationId;
}
