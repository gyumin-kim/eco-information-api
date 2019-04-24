package com.example.ecoinformationapi.service;

import com.example.ecoinformationapi.dto.ProgramInfoDto;
import com.example.ecoinformationapi.dto.SearchProgramByRegionCodeDto;
import com.example.ecoinformationapi.model.Program;
import com.example.ecoinformationapi.repository.ProgramRepository;
import com.example.ecoinformationapi.repository.RegionRepository;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class ProgramService {

  private ProgramRepository programRepository;
  private RegionRepository regionRepository;

  public ProgramService(ProgramRepository programRepository,
      RegionRepository regionRepository) {
    this.programRepository = programRepository;
    this.regionRepository = regionRepository;
  }

  /**
   * regionCode로 Program의 집합을 구한다.
   *
   * @param regionCode 지역코드 (= Region 고유코드)
   * @return {@param regionCode}에 해당하는 지역에서 서비스 되는 Program들의 집합
   */
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
      programInfoDto.setTheme(program.getTheme());
      programInfoDto.setRegion_name(regionName); // TODO: 지역명 여러개일 경우 여러개 그대로 담아주기?? 아니면 하나만?
      programInfoDto.setIntro(program.getIntro());
      programInfoDto.setDetailed_intro(program.getDetailedIntro());

      programDtos.add(programInfoDto);
    }

    return resultDto;
  }
}
