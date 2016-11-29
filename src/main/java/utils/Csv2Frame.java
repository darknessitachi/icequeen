//namespace BackTesting.Model.Utils
//{
//    using System;
//    using System.Collections.Generic;
//    using System.IO;
//    using System.Text;
//    using BackTesting.Model.MarketData;
//    using CsvHelper;
//    using CsvHelper.Configuration;
package utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import marketdata.Bar;

public  class Csv2Frame
    {
        public static Map<LocalDateTime, Bar> LoadBarsFromFile(String filePath)
        {
        	String text = readText(new File(filePath));
             return LoadBarsFromString(text);
        }

//        public static Map<DateTime, Bar> LoadBarsFromString(String csvString)
//        {
//            using (var ms = new MemoryStream(Encoding.Default.GetBytes(csvString)))
//            {
//                using (var textReader = new StreamReader(ms))
//                {
//                    return LoadFormTextReader(textReader);
//                }
//            }
//        }

        public static Map<LocalDateTime, Bar> LoadBarsFromString(String text)
        {
        	Map<LocalDateTime, Bar> res = new HashMap<>();
        	
        	String[] datas = text.split("\r\n");
            for (int i =1;i<datas.length;i++)
            {
            	String line = datas[i];
            	String[] info = line.split(",");
            	Bar bar = new Bar();
            	
            	 // <TICKER>,<PER>,<DATE>,<TIME>,<OPEN>,<HIGH>,<LOW>,<CLOSE>,<VOL>
//                public BarCsvMap()
//                {
//                    Map(m => m.DateTime).ConvertUsing(row => String2DateTime.Convert(row.GetField<string>("<DATE>"), row.GetField<string>("<TIME>")));
//                    Map(m => m.Symbol).Name("<TICKER>");
//                    Map(m => m.Period).Name("<PER>");
//                    Map(m => m.Open).Name("<OPEN>");
//                    Map(m => m.High).Name("<HIGH>");
//                    Map(m => m.Low).Name("<LOW>");
//                    Map(m => m.Close).Name("<CLOSE>");
//                    Map(m => m.Volume).Name("<VOL>");
//                }
//                
                if (res.containsKey(bar.getDateTime()))
                {
                    res.put(bar.getDateTime(), bar);
                }
                else
                {
                    res.put(bar.getDateTime(), bar);
                }
            }

            return res;
        }
        
        public static String readText(File f) {
    		try {
    			InputStream is = new FileInputStream(f);
    			String str = readText(is, "utf-8");
    			is.close();
    			return str;
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		return null;
    	}
        
        public static String readText(InputStream is, String encoding) {
    		try {
    			byte[] bs = readByte(is);

    			return new String(bs, encoding);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		return null;
    	}
        
        public static byte[] readByte(InputStream is) {
    		byte[] buffer = new byte[8192];
    		ByteArrayOutputStream os = new ByteArrayOutputStream();
    		while (true) {
    			int bytesRead = -1;
    			try {
    				bytesRead = is.read(buffer);
    			} catch (IOException e) {
    				throw new RuntimeException("File.readByte() failed");
    			}
    			if (bytesRead == -1)
    				break;
    			try {
    				os.write(buffer, 0, bytesRead);
    			} catch (Exception e) {
    				throw new RuntimeException("File.readByte() failed");
    			}
    		}
    		return os.toByteArray();
    	}
    }
