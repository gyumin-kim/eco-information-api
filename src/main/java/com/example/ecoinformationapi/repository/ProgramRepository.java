package com.example.ecoinformationapi.repository;

import com.example.ecoinformationapi.model.Program;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProgramRepository extends JpaRepository<Program, Long> {

  @Query("SELECT p FROM Program p JOIN FETCH ProgramRegion pr ON pr.program.id = p.id WHERE pr.region.code = :regionCode")
  Set<Program> findByRegionCode(String regionCode);
}
