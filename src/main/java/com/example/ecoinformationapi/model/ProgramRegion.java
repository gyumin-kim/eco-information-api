package com.example.ecoinformationapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class ProgramRegion {

  @Id
  @GeneratedValue
  @Column(name = "PROGRAM_REGION_ID")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "PROGRAM_ID")
  private Program program;

  @ManyToOne
  @JoinColumn(name = "REGION_CODE")
  private Region region;


}
