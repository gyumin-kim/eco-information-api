package com.example.ecoinformationapi.service.save;

import com.example.ecoinformationapi.model.Program;
import com.example.ecoinformationapi.model.Region;
import com.example.ecoinformationapi.repository.ProgramRepository;
import com.example.ecoinformationapi.repository.RegionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SaveService {

  private ProgramRepository programRepository;
  private RegionRepository regionRepository;

  public SaveService(ProgramRepository programRepository,
      RegionRepository regionRepository) {
    this.programRepository = programRepository;
    this.regionRepository = regionRepository;
  }

  public void saveProgram(List<String[]> list) {
    String currPrgmCode = "0520";
    int currRegionCode = 3700;

    for (String[] arr : list) {
      Long id = Long.parseLong(arr[0]);
      String prgmName = arr[1];
      String theme = arr[2];
      String intro = arr[4];
      String detailedIntro = arr[5];

      String tmpPrgmCode = String
          .format("%0" + currPrgmCode.length() + "d", Integer.parseInt(currPrgmCode) + 1);
      String code = "prg" + tmpPrgmCode;
      currPrgmCode = tmpPrgmCode;

      Program program = new Program(id, prgmName, theme, intro, detailedIntro, code);
      programRepository.save(program);

      String regions = arr[3];
      String[] regionsArr = regions.split(", ");
      for (String regionFull : regionsArr) {

        // 전체 통으로 하나의 Region
        currRegionCode = saveRegion(currRegionCode, program, regionFull);

        // 공백으로 split한 개별 region들
        String[] regionPartArr = regionFull.split(" ");
        for (String regionPart : regionPartArr) {
          currRegionCode = saveRegion(currRegionCode, program, regionPart);
        }
        programRepository.save(program);
      }
    }
  }

  private int saveRegion(int currRegionCode, Program program, String regionName) {
    Region region;
    if (!regionRepository.existsByName(regionName)) {
      String regionCode = "reg" + currRegionCode++;
      region = new Region(regionCode, regionName);
    } else {
      region = regionRepository.findByName(regionName);
    }
    program.addRegion(region);  // 양방향 연관관계 설정
    regionRepository.save(region);
    return currRegionCode;
  }
}
