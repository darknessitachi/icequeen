package com.abigdreamer.icequeen.marketdata;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import com.abigdreamer.infinity.common.util.FileUtil;

public class CsvDataSource {

	public static CsvDataSource CreateFromFiles(String csvDirectory, String... symbolList) {
		CsvDataSource res = new CsvDataSource();

		for (String symbol : symbolList) {
			String csvPath = csvDirectory + File.separator + symbol + ".csv";
			res.init(symbol, csvPath);
		}

		return res;
	}
	
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");

	public Map<String, Map<LocalDateTime, Bar>> bars;

	protected CsvDataSource() {
		this.bars = new LinkedHashMap<>();
	}
	
	private void init(String symbol, String csvPath) {
		Map<LocalDateTime, Bar> bars = this.loadBarsFromFile(csvPath);
		this.put(symbol, bars);
	}
	
	private Map<LocalDateTime, Bar> loadBarsFromFile(String filePath) {
		String text = FileUtil.readText(filePath);

		Map<LocalDateTime, Bar> res = new LinkedHashMap<>();

		String[] datas = text.split("\n");
		// String[] header = datas[0].split(",");
		for (int i = 1; i < datas.length; i++) {
			// <TICKER>,<PER>,<DATE>,<TIME>,<OPEN>,<HIGH>,<LOW>,<CLOSE>,<VOL>
			String line = datas[i];
			String[] info = line.split(",");
			Bar bar = new Bar();

			// <DATE> <TIME> 2007-12-03 10:15:30
			String timeString = info[2] + " " + info[3];
			LocalDateTime dateTime = LocalDateTime.parse(timeString, dateFormatter);
			bar.setDateTime(dateTime);
			bar.setSymbol(info[0]);// <TICKER>
			bar.setPeriod(info[1]);// <PER>
			bar.setOpen(Double.valueOf(info[4]));// <OPEN>
			bar.setHigh(Double.valueOf(info[5]));// <HIGH>
			bar.setLow(Double.valueOf(info[6]));// <LOW>
			bar.setClose(Double.valueOf(info[7]));// <CLOSE>
			bar.setVolume(Long.valueOf(info[8]));// <VOL>

			res.put(bar.getDateTime(), bar);
		}

		return res;
	}

	public void put(String symbol, Map<LocalDateTime, Bar> bars) {
		this.bars.put(symbol, bars);
	}
	
	public Map<String, Map<LocalDateTime, Bar>> getBars() {
		return bars;
	}

	public static void main(String[] args) {
		String filePath = System.getProperty("user.dir") + File.separator + "testData" + File.separator + "Min1" + File.separator + "sber.csv";
		CsvDataSource res = new CsvDataSource();
		Map<LocalDateTime, Bar> bars = res.loadBarsFromFile(filePath);
		for (Map.Entry<LocalDateTime, Bar> entity : bars.entrySet()) {
			System.out.println(entity.getKey() + "==>" + entity.getValue());
		}

		System.out.println("bar size:" + bars.size());
	}

}
