package com.example.ecoinformationapi.controller;

import com.example.ecoinformationapi.dto.RegionCodeDto;
import com.example.ecoinformationapi.dto.SearchProgramByRegionCodeDto;
import com.example.ecoinformationapi.service.ProgramService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class ProgramRestController {

  private ProgramService programService;

  public ProgramRestController(ProgramService programService) {
    this.programService = programService;
  }

  /**
   * 지역 코드를 입력받아 해당 지역에서 서비스되는 프로그램 정보를 리턴
   */
  @PostMapping(path = "program", consumes = "application/json", produces = "application/json")
  public SearchProgramByRegionCodeDto programByRegionCode(
      @RequestBody RegionCodeDto regionCodeDto) {

    // `PROGRAM_REGION`에서 해당 region_id를 가진 program_id를 구하고,
    // 각 program_id에 해당하는 Program 데이터를 리턴
    String regionCode = regionCodeDto.getRegionCode();

    return programService.getProgramsByRegionCode(regionCode);
  }
}
