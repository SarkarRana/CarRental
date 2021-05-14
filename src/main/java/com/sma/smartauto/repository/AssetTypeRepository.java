package com.sma.smartauto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sma.smartauto.domain.AssetType;

@Repository
public interface AssetTypeRepository extends JpaRepository<AssetType, Integer> {

}
