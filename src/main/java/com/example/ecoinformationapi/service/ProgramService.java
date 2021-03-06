package com.example.ecoinformationapi.service;

import com.example.ecoinformationapi.dto.CreateDataDto;
import com.example.ecoinformationapi.dto.ProgramInfoDto;
import com.example.ecoinformationapi.dto.SearchProgramByRegionCodeDto;
import com.example.ecoinformationapi.dto.UpdateDataDto;
import com.example.ecoinformationapi.model.Program;
import com.example.ecoinformationapi.repository.ProgramRepository;
import com.example.ecoinformationapi.repository.RegionRepository;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProgramService {

  private InitService initService;
  private ProgramRepository programRepository;
  private RegionRepository regionRepository;

  public ProgramService(InitService initService,
      ProgramRepository programRepository,
      RegionRepository regionRepository) {
    this.initService = initService;
    this.programRepository = programRepository;
    this.regionRepository = regionRepository;
  }

  /**
   * regionCode로 Program의 집합을 구한다.
   *
   * @param regionCode 지역코드 (= Region 고유코드)
   * @return {@param regionCode}에 해당하는 지역에서 서비스 되는 Program들의 집합
   */
  @Transactional(readOnly = true)
  public SearchProgramByRegionCodeDto getProgramsByRegionCode(String regionCode) {
    SearchProgramByRegionCodeDto resultDto = new SearchProgramByRegionCodeDto();

    resultDto.setRegion_code(regionCode);

    Set<ProgramInfoDto> programDtos = resultDto.getPrograms();
    String regionName = regionRepository.findNameByCode(regionCode);

    Set<Program> programs = programRepository.findByRegionCode(regionCode);
    for (Program program : programs) {
      ProgramInfoDto programInfoDto = new ProgramInfoDto();

      programInfoDto.setId(program.getId());
      programInfoDto.setPrgm_name(program.getName());
      programInfoDto.setPrgm_code(program.getCode());
      programInfoDto.setTheme(program.getTheme());
      programInfoDto.setRegion_name(regionName); // TODO: 지역명 여러개일 경우 여러개 그대로 담아주기?? 아니면 하나만?
      programInfoDto.setIntro(program.getIntro());
      programInfoDto.setDetailed_intro(program.getDetailedIntro());

      programDtos.add(programInfoDto);
    }

    return resultDto;
  }

  /**
   * 입력으로 들어온 {@param dto}의 정보들을 활용하여 Program 추가.
   * '서비스 지역' 활용하여 Region 객체도 추가
   */
  @Transactional
  public Program addProgram(CreateDataDto dto) {
    Long id = getNextProgramId();
    String prgmName = dto.getPrgm_name();
    String theme = dto.getTheme();
    String intro = dto.getIntro();
    String detailedIntro = dto.getDetailed_intro();
    String code = InitService.incrementPrgmCode();

    Program program = new Program(id, prgmName, theme, intro, detailedIntro, code);
    programRepository.save(program);

//    String regions = dto.getRegions();
//    initService.generateRegion(regions, program);
    return program;
  }

  private Long getNextProgramId() {
    return programRepository.findMaxId() + 1;
  }

  @Transactional(readOnly = true)
  public Program getProgramByCode(String prgmCode) {
    return programRepository.findByCode(prgmCode);
  }

  @Transactional
  public Program modifyProgram(Program target, UpdateDataDto update) {
    if (update.getPrgm_name() != null) {
      target.setName(update.getPrgm_name());
    }
    if (update.getTheme() != null) {
      target.setTheme(update.getTheme());
    }
    if (update.getIntro() != null) {
      target.setIntro(update.getIntro());
    }
    if (update.getDetailed_intro() != null) {
      target.setDetailedIntro(update.getDetailed_intro());
    }

    programRepository.save(target);

    return target;
  }
}
