package com.goev.auth.dao.system.instance;


import com.goev.lib.dao.BaseDao;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SystemInstanceDao extends BaseDao {
    private String name;
    private String hostname;
    private Integer organizationId;
}



