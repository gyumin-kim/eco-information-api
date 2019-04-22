package com.example.ecoinformationapi.service.load;

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
public class LoadService {

  @Value("${csv.path}")
  private String csvPath;

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
