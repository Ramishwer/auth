package com.goev.auth.repository.organization;

import com.goev.auth.dao.OrganizationDao;


public interface OrganizationRepository {
    OrganizationDao save(OrganizationDao user);

    OrganizationDao update(OrganizationDao user);

    void delete(Integer id);

    OrganizationDao findByUUID(String uuid);

    OrganizationDao findById(Integer id);
}
