package com.goev.auth.dao;

import com.goev.lib.dao.BaseDao;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrganizationDao extends BaseDao {
    private String name;
}
