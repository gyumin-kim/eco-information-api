package com.example.ecoinformationapi.service.load;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
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
			reader = new CSVReader(new FileReader(fileName));
			String[] line;
			while ((line = reader.readNext()) != null) {
				list.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}

//	public List<String[]> readAll(Reader reader) throws Exception {
////		CSVReader csvReader = new CSVReaderBuilder(reader)
////				.withSkipLines(0)
////				.withCSVParser(new CSVParser())
////				.build();
//		CSVReader csvReader = new CSVReader(reader);
//		List<String[]> list = csvReader.readAll();
//		reader.close();
//		csvReader.close();
//
//		return list;
//	}
}
