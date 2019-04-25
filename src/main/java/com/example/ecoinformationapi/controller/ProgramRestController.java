package com.example.ecoinformationapi.controller;

import com.example.ecoinformationapi.dto.CreateDataDto;
import com.example.ecoinformationapi.dto.RegionCodeDto;
import com.example.ecoinformationapi.dto.SearchProgramByRegionCodeDto;
import com.example.ecoinformationapi.dto.UpdateDataDto;
import com.example.ecoinformationapi.model.Program;
import com.example.ecoinformationapi.model.Region;
import com.example.ecoinformationapi.service.InitService;
import com.example.ecoinformationapi.service.ProgramRegionService;
import com.example.ecoinformationapi.service.ProgramService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class ProgramRestController {

  private InitService initService;
  private ProgramService programService;
  private ProgramRegionService programRegionService;

  public ProgramRestController(InitService initService,
      ProgramService programService,
      ProgramRegionService programRegionService) {
    this.initService = initService;
    this.programService = programService;
    this.programRegionService = programRegionService;
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
  @PostMapping(path = "program/new", consumes = "application/json")
  @ResponseStatus(HttpStatus.CREATED)
  public void createProgram(@RequestBody CreateDataDto createDataDto) {
    Program program = programService.addProgram(createDataDto);

    // Program - Region
    List<Region> regionList = initService.generateRegion(createDataDto.getRegions());
    for (Region region : regionList) {
      initService.mapProgramRegion(program, region);
    }
  }

  /**
   * 매개변수로 들어온 {@param updateDataDto}에 따라 기존 데이터 수정
   */
  @PutMapping(path = "program/update", consumes = "application/json", produces = "application/json")
  public Program updateProgram(@RequestBody UpdateDataDto updateDataDto) {
    Program program = programService.getProgramByCode(updateDataDto.getCode());

    // `Program_Region`에서 해당 program_id에 관련 레코드 모두 삭제
    programRegionService.deleteAllByProgramId(program.getId());

    program = programService.modifyProgram(program, updateDataDto); // 필드가 변경된 새로운 program

    // initService.generateRegion(regions, program);
    // 이 작업과 동일한 작업 수행
    List<Region> regionList = initService.generateRegion(updateDataDto.getRegions());

    for (Region region : regionList) {
      initService.mapProgramRegion(program, region);
    }

    return program;
  }
}
