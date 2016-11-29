//namespace BackTesting.Model.MarketData
//{
//    using System;
//    using System.Collections.Generic;
//    using System.IO;
//    using BackTesting.Model.Utils;
package marketdata;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Dictionary;
import java.util.LinkedHashMap;
import java.util.Map;

import utils.Csv2Frame;

public class CsvDataSource 
    {
    	public Map<String, Map<LocalDateTime, Bar>> Bars ;
    	public Map<String, Map<LocalDateTime, Bar>> getBars (){
    		return Bars;
    	};

        protected CsvDataSource()
        {
            this.Bars = new LinkedHashMap<>();
        }

        public static CsvDataSource CreateFromFiles(String csvDirectory, String[] symbolList)
        {
        	CsvDataSource res = new CsvDataSource();

            for (String symbol : symbolList)
            {
                String csvPath = csvDirectory + "/" + symbol + ".csv";
                res.AddOrReplaceBars(symbol, Csv2Frame.LoadBarsFromFile(csvPath));
            }

            return res;
        }

        public static CsvDataSource CreateFormStrings(Map<String, String> csvData)
        {
        	CsvDataSource res = new CsvDataSource();

            for (Map.Entry<String,String> kvp : csvData.entrySet())
            {
                res.AddOrReplaceBars(kvp.getKey(), Csv2Frame.LoadBarsFromString(kvp.getValue()));
            }

            return res;
        }

        private void AddOrReplaceBars(String symbol, Map<LocalDateTime, Bar> bars)
        {
            if (this.Bars.containsKey(symbol))
            {
                this.Bars.put(symbol, bars);
            }
            else
            {
                this.Bars.put(symbol, bars);
            }
        }
    }
