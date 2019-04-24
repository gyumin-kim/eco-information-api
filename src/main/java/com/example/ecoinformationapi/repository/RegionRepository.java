package com.example.ecoinformationapi.repository;

import com.example.ecoinformationapi.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RegionRepository extends JpaRepository<Region, String> {

  Boolean existsByName(String name);

  Region findByName(String name);

  @Query("SELECT r.name FROM Region r WHERE r.code = :code")
  String findNameByCode(String code);
}
