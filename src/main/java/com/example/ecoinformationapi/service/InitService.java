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
import org.springframework.transaction.annotation.Transactional;

@Service
public class InitService {

  private ProgramRepository programRepository;
  private RegionRepository regionRepository;
  private ProgramRegionRepository programRegionRepository;

  @Value("${csv.path}")
  private String csvPath;

  //TODO: application.properties에 설정해 놓는 방식
  // (https://jiyeonseo.github.io/2016/08/27/spring-inject-static-variables/)
  private static String currPrgmCode = "0522";
  private static int currRegionCode = 3700;

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
  @Transactional(readOnly = true)
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

  @Transactional
  public void saveProgram(List<String[]> list) {
//    String currPrgmCode = "0520";
//    int currRegionCode = 3700;
    for (String[] arr : list) {
      Long id = Long.parseLong(arr[0]);
      String prgmName = arr[1];
      String theme = arr[2];
      String intro = arr[4];
      String detailedIntro = arr[5];

      String code = incrementPrgmCode();

      Program program = new Program(id, prgmName, theme, intro, detailedIntro, code);
      programRepository.save(program);  // Program에 save 완료
    }
  }

  @Transactional
  public void resolveRegions(List<String[]> list) {
    for (String[] arr : list) {
      Long id = Long.parseLong(arr[0]);

      Program program = programRepository.getOne(id);
      String regions = arr[3];

      String[] regionsArr = regions.split(", ");

      for (String regionFull : regionsArr) {
        // 전체 하나의 Region을 저장 (공백 고려하지 않음)
        saveRegion(regionFull);
        mapProgramRegion(program, regionRepository.findByName(regionFull));

        // 공백으로 split한 개별 region들을 각각 저장
        String[] regionPartArr = regionFull.split(" ");
        for (String regionPart : regionPartArr) {
          saveRegion(regionPart);
          mapProgramRegion(program, regionRepository.findByName(regionPart));
        }
        programRepository.save(program);
      }
    }
  }

  @Transactional
  public List<Region> generateRegion(String regions) {
    List<Region> regionList = new ArrayList<>();

    String[] regionsArr = regions.split(", ");
    for (String regionFull : regionsArr) {

      // 전체 하나의 Region을 저장 (공백 고려하지 않음)
      regionList.add(saveRegion(regionFull));
//      mapProgramRegion(program, regionRepository.findByName(regionFull));

      // 공백으로 split한 개별 region들을 각각 저장
      String[] regionPartArr = regionFull.split(" ");
      for (String regionPart : regionPartArr) {
        regionList.add(saveRegion(regionPart));
//        mapProgramRegion(program, regionRepository.findByName(regionPart));
      }
    }

    return regionList;
  }

  @Transactional
  public Region saveRegion(String regionName) {
    Region region;
    if (!regionRepository.existsByName(regionName)) {
      String regionCode = "reg" + currRegionCode++;
      region = new Region(regionCode, regionName);
    } else {
      region = regionRepository.findByName(regionName);
    }
    regionRepository.save(region);  // Region에 save 완료
    //TODO: 수정 시 매핑 안되는 이유 => region이 아직 저장 안된 상태라서?

    return region;
  }

  /**
   * Program_Region 테이블 데이터 생성
   */
  @Transactional
  public void mapProgramRegion(Program program, Region region) {
    ProgramRegion programRegion = new ProgramRegion();
    programRegion.setProgram(program);
    programRegion.setRegion(region);
    programRegionRepository.save(programRegion);
  }

  static String incrementPrgmCode() {
    String tmpPrgmCode = String
        .format("%0" + currPrgmCode.length() + "d", Integer.parseInt(currPrgmCode) + 1);
    String code = "prg" + tmpPrgmCode;
    currPrgmCode = tmpPrgmCode;

    return code;
  }
}
