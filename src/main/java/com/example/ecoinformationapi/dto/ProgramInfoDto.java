package com.example.ecoinformationapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgramInfoDto {

  private Long id;
  private String prgm_code;
  private String prgm_name;
  private String theme;
  private String region_name;
  private String intro;
  private String detailed_intro;
}
