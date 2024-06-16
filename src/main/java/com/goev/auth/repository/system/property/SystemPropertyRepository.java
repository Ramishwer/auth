package com.goev.auth.repository.system.property;

import com.goev.auth.dao.system.property.SystemPropertyDao;

import java.util.List;
import java.util.Map;

public interface SystemPropertyRepository {
    Map<String, SystemPropertyDao> getPropertyMap();

    SystemPropertyDao save(SystemPropertyDao property);

    SystemPropertyDao update(SystemPropertyDao property);

    void delete(Integer id);

    SystemPropertyDao findByUUID(String uuid);

    SystemPropertyDao findById(Integer id);

    List<SystemPropertyDao> findAllByIds(Integer... ids);

    List<SystemPropertyDao> findAll();
}

