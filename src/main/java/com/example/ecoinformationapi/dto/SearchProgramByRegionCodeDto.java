package com.example.ecoinformationapi.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchProgramByRegionCodeDto {

  private String regionCode;

  private Set<ProgramInfoDto> programs = new HashSet<>();
}
