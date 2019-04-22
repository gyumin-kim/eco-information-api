package com.example.ecoinformationapi.controller.save;

import com.example.ecoinformationapi.service.load.LoadService;
import com.example.ecoinformationapi.service.save.SaveService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class SaveRestController {

	private LoadService loadService;
	private SaveService saveService;

	public SaveRestController(LoadService loadService,
			SaveService saveService) {
		this.loadService = loadService;
		this.saveService = saveService;
	}

	@GetMapping(path = "save")
	public void readAndSave() {
		// CSV 파일로부터 데이터를 읽어온다.
		List<String[]> list = loadService.readAll();

		// 각 레코드를 Database에 저장한다.
		saveService.save(list);
	}
}
