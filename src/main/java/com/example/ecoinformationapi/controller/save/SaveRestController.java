package com.example.ecoinformationapi.controller.save;

import com.example.ecoinformationapi.service.load.LoadService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@Slf4j
public class SaveRestController {

	private final LoadService loadService;

	@Value("${csv.path}")
	private String csvPath;

	public SaveRestController(LoadService loadService) {
		this.loadService = loadService;
	}

	@GetMapping(path = "save")
	public void readAndSave() {
		// CSV 파일로부터 데이터를 읽어온다.
		List<String[]> list = loadService.readAll(csvPath);

		log.info("-------------------------------------------------------");
		for (String[] arr : list) {
			log.info("번호: " + arr[0]);
			log.info("프로그램명: " + arr[1]);
			log.info("테마: " + arr[2]);
			log.info("지역: " + arr[3]);
			log.info("-------------------------------------------------------");
		}

		// 각 레코드를 Database에 저장한다.

	}
}
