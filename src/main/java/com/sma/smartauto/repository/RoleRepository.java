package com.sma.smartauto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sma.smartauto.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    @Override
    void delete(Role role);

}
