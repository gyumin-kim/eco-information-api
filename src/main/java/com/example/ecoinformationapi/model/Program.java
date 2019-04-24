package com.example.ecoinformationapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PROGRAM")
public class Program {

  @Id
  @Column(name = "PROGRAM_ID")
  private Long id;

  // 프로그램명
  @Column(name = "NAME")
  private String name;

  // 테마
  @Column(name = "THEME")
  private String theme;

  // 서비스 지역
  @OneToMany(mappedBy = "program", fetch = FetchType.LAZY)
  @JsonIgnore
  private Set<ProgramRegion> programRegions = new HashSet<>();

  // 소개
  @Column(name = "INTRO")
  private String intro;

  // 상세 소개
  @Column(name = "DETAILED_INTRO")
  @Lob
  private String detailedIntro;

  // 프로그램 코드
  @Column(name = "CODE")
  private String code;

  public Program() {
  }

  public Program(Long id, String name, String theme, String intro, String detailedIntro,
      String code) {
    this.id = id;
    this.name = name;
    this.theme = theme;
    this.intro = intro;
    this.detailedIntro = detailedIntro;
    this.code = code;
  }
}
