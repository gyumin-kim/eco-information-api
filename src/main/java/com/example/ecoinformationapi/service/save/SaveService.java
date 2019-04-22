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

  /**
   * list - entity mapping하여 DB에 저장
   */
  public void save(List<String[]> list) {
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
      String[] regionArr = regions.split(", ");
      for (String regionName : regionArr) {

        //TODO: Program - Region: ManyToMany로 변경해야 함.
        Region region;
        if (regionRepository.existsByName(regionName)) {
          region = regionRepository.findByName(regionName);
        } else {
          String regionCode = "reg" + currRegionCode++;
          region = new Region(regionCode, regionName);
          region.setProgram(program);
        }

        regionRepository.save(region);
      }
    }
  }
}
