package com.example.ecoinformationapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDataDto {

  private String prgm_name;
  private String theme;
  private String regions; // ex) "경상남도 산청군, 하동군등" (여러개일 경우 ', '로 구분)
  private String intro;
  private String detailed_intro;
}