package com.goev.auth.dao.user;


import com.goev.lib.dao.BaseDao;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthUserDao extends BaseDao {
    private String phoneNumber;
    private String email;
    private Integer organizationId;
}



