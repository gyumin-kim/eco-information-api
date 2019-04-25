package com.example.ecoinformationapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDataDto {

  private String code;
  private String prgm_name;
  private String theme;
  private String regions;
  private String intro;
  private String detailed_intro;
}
