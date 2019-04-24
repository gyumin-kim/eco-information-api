package com.example.ecoinformationapi.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "REGION")
public class Region {

	// 지역코드 (ex. "reg3726")
	@Id
	@Column(name = "CODE")
	private String code;

	// 지역
	@Column(name = "NAME")
	private String name;

	// 이 지역에서 서비스되는 프로그램들
	// TODO: lazy loading
//	@ManyToMany(mappedBy = "regions")
//	private Set<Program> programs = new HashSet<>();
	@OneToMany(mappedBy = "region")
	private Set<ProgramRegion> programRegions = new HashSet<>();

	public Region() {
	}

	public Region(String code, String name) {
		this.code = code;
		this.name = name;
	}
}
