package com.abigdreamer.icequeen;

import java.io.File;

import org.lst.trading.lib.csv.SeriesCsvReader;
import org.lst.trading.lib.series.DoubleSeries;
import org.lst.trading.lib.util.yahoo.YahooFinance;

import com.abigdreamer.infinity.common.util.FileUtil;
import com.abigdreamer.infinity.common.util.TimeWatch;

/**
 *  
 * @author Darkness
 * @date 2016年12月2日 下午1:35:45
 * @version V1.0
 */
public class CsvReaderTest {

	public static void main(String[] args) {
		String csvPath = System.getProperty("user.dir") + File.separator + "testData" + File.separator + "YahooFinance"+File.separator+"GLD.csv";
		String csv = FileUtil.readText(csvPath);
		
		TimeWatch timeWatch = TimeWatch.create();
		timeWatch.startWithTaskName("stream read");
        DoubleSeries prices = SeriesCsvReader.parse(csv, YahooFinance.SEP, YahooFinance.DATE_COLUMN, YahooFinance.ADJ_COLUMN);
        timeWatch.stopAndPrint();
	}
}
