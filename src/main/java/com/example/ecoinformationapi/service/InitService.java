package com.example.ecoinformationapi.service;

import com.example.ecoinformationapi.model.Program;
import com.example.ecoinformationapi.model.ProgramRegion;
import com.example.ecoinformationapi.model.Region;
import com.example.ecoinformationapi.repository.ProgramRegionRepository;
import com.example.ecoinformationapi.repository.ProgramRepository;
import com.example.ecoinformationapi.repository.RegionRepository;
import com.opencsv.CSVReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InitService {

  private ProgramRepository programRepository;
  private RegionRepository regionRepository;
  private ProgramRegionRepository programRegionRepository;

  @Value("${csv.path}")
  private String csvPath;

  public InitService(ProgramRepository programRepository,
      RegionRepository regionRepository,
      ProgramRegionRepository programRegionRepository) {
    this.programRepository = programRepository;
    this.regionRepository = regionRepository;
    this.programRegionRepository = programRegionRepository;
  }

  /**
   * CSV 파일을 읽어들여 각 line을 List 형태로 리턴한다.
   */
  public List<String[]> readAll() {
    CSVReader reader;
    List<String[]> list = new ArrayList<>();

    try {
      FileInputStream fis = new FileInputStream(csvPath);
      InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
      reader = new CSVReader(isr);
      reader.skip(1);
      list = reader.readAll();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return list;
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
    programRepository.save(program);
//    program.addRegion(region);  // 양방향 연관관계 설정
    regionRepository.save(region);

    ProgramRegion programRegion = new ProgramRegion();
    programRegion.setProgram(program);
    programRegion.setRegion(region);
    programRegionRepository.save(programRegion);

    return currRegionCode;
  }

  //  public List<String[]> readAll(String fileName) {
//    List<String[]> list = new ArrayList<>();
//    CsvToBean<Program> csvToBean = new CsvToBean<>();
////    HeaderColumnNameTranslateMappingStrategy<Program> strategy =
////        new HeaderColumnNameTranslateMappingStrategy<>();
//    ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
//    strategy.setType(Program.class);
////    String[] columns = new String[]{"id", "prgmName", "code", "theme", "intro", "detailedIntro",
//    String[] columns = new String[]{"id", "prgmName", "theme", "intro",
//        "detailedIntro"}; // the fields to bind to in your bean
//    strategy.setColumnMapping(columns);
//
//    CSVReader reader;
//    try {
//      FileInputStream fis = new FileInputStream(fileName);
//      InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
//      reader = new CSVReader(isr);
//      reader.skip(1);  // header row 생략
//
////      List<Program> programs = csvToBean.parse(strategy, reader);
//      List<Program> programs = new CsvToBeanBuilder(reader)
//          .withType(Program.class)
//          .withMappingStrategy(strategy)
//          .build()
//          .parse();
//      for (Program program : programs) {
//        log.info(program.getId() + "");
//        log.info(program.getPrgmName());
//        log.info(program.getTheme());
//        log.info(program.getRegions().toString());
//        log.info(program.getIntro());
//        log.info(program.getDetailedIntro());
//      }
//
//      String[] line;
//      while ((line = reader.readNext()) != null) {
//        list.add(line);
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//
//    return list;
//  }
}
