package com.example.ecoinformationapi.service.load;

import com.opencsv.CSVReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoadService {

	public List<String[]> readAll(String fileName) {
		List<String[]> list = new ArrayList<>();

		CSVReader reader;
		try {
			FileInputStream fis = new FileInputStream(fileName);
			InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
			reader = new CSVReader(isr);
			String[] line;
			while ((line = reader.readNext()) != null) {
				list.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}
}
