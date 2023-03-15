package org.perscholas.epm.service;

import org.perscholas.epm.dao.RoleRepo;
import org.perscholas.epm.model.Role;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService {

    private RoleRepo roleRepo;

    public RoleService(RoleRepo roleRepo){

        this.roleRepo = roleRepo;
    }

    public Role loadRoleByName(String roleName){

        return roleRepo.findByName(roleName);
    }

    public Role createRole(String roleName){

        return roleRepo.save(new Role(roleName));
    }
}
