package com.example.ecoinformationapi.service;

import com.example.ecoinformationapi.repository.ProgramRegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProgramRegionService {

  private ProgramRegionRepository programRegionRepository;

  public ProgramRegionService(
      ProgramRegionRepository programRegionRepository) {
    this.programRegionRepository = programRegionRepository;
  }

  @Transactional
  public void deleteAllByProgramId(Long programId) {
    programRegionRepository.deleteAllByProgramId(programId);
  }
}
