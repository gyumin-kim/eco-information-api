package com.example.ecoinformationapi.controller;

import com.example.ecoinformationapi.dto.CreateDataDto;
import com.example.ecoinformationapi.dto.RegionCodeDto;
import com.example.ecoinformationapi.dto.SearchProgramByRegionCodeDto;
import com.example.ecoinformationapi.service.ProgramService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
  @PostMapping(path = "program/regioncode", consumes = "application/json", produces = "application/json")
  public SearchProgramByRegionCodeDto programByRegionCode(
      @RequestBody RegionCodeDto regionCodeDto) {

    String regionCode = regionCodeDto.getRegionCode();
    return programService.getProgramsByRegionCode(regionCode);
  }

  /**
   * 새로운 데이터를 추가
   */
  @PostMapping(path = "program/new", consumes = "application/json", produces = "application/json")
  @ResponseStatus(HttpStatus.CREATED)
  public void createProgram(@RequestBody CreateDataDto createDataDto) {
    programService.addProgram(createDataDto);
  }
}
