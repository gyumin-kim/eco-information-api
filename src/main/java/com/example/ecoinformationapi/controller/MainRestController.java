package com.example.ecoinformationapi.controller;

import com.example.ecoinformationapi.service.InitService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class MainRestController {

	private InitService initService;

	public MainRestController(InitService initService) {
		this.initService = initService;
	}

	/**
	 * CSV 파일의 각 레코드를 Database에 저장
	 */
	@PostMapping(path = "init")
	@ResponseStatus(HttpStatus.CREATED)
	public void loadAndSave() {

		List<String[]> list = initService.readAll();
		initService.saveProgram(list);
	}
}
