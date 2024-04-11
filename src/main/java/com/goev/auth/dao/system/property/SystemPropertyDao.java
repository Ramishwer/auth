package com.goev.auth.dao.system.property;


import com.goev.lib.dao.BaseDao;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SystemPropertyDao extends BaseDao {
    private String propertyName;
    private String propertyDescription;
    private String propertyType;
    private String propertyValue;
}
