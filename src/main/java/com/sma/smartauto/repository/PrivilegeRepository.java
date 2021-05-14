package com.sma.smartauto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sma.smartauto.domain.AccessLevel;

public interface PrivilegeRepository extends JpaRepository<AccessLevel, Long> {

	AccessLevel findByAccessDesc(String name);

    @Override
    void delete(AccessLevel privilege);

}
