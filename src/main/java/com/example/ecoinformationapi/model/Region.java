package com.example.ecoinformationapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	//	@Column(name = "REGION_ID")
//	private Long id;

	@Column(name = "NAME")
	private String name;

	@ManyToOne
	@JoinColumn(name = "PROGRAM_ID")
	private Program program;

	public void setProgram(Program program) {
		this.program = program;

		// 무한루프 체크
		if (!program.getRegions().contains(this)) {
			program.getRegions().add(this);
		}
	}

	public Region() {
	}

	public Region(String code, String name) {
		this.code = code;
		this.name = name;
	}
}
