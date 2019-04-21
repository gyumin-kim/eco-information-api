package com.example.ecoinformationapi.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PROGRAM")
public class Program {

	@Id	@GeneratedValue
	@Column(name = "PROGRAM_ID")
	private Long id;

	// 프로그램명
	@Column(name = "PRGM_NAME")
	private String prgmName;

	// 프로그램 코드
	@Column(name = "CODE")
	private String code;

	// 테마
	@Column(name = "THEME")
	private String theme;

	// 소개
	@Column(name = "INTRO")
	private String intro;

	// 상세 소개
	@Column(name = "DETAILED_INTRO")
	private String detailedIntro;

	// 서비스 지역
	@OneToMany(mappedBy = "program")
	private Set<Region> regions = new HashSet<>();

	public void addRegion(Region region) {
		this.regions.add(region);

		// 무한루프 체크
		if (region.getProgram() != this) {
			region.setProgram(this);
		}
	}
}
