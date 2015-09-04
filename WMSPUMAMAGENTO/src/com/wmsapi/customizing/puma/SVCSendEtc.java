package com.wmsapi.customizing.puma;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

public class SVCSendEtc {
	
	
	public static void main(String args[]) {
				
		FileInputStream fis = null;
		BufferedReader br = null;
		StringBuilder xml = new StringBuilder();
		try {
			fis = new FileInputStream(new File("D:\\pumaSample\\O_ZMKR_M_1503_0000000000195135.xml"));
			br = new BufferedReader(new InputStreamReader(fis, "utf-8"));
			String line = "";
			while ((line=br.readLine()) != null) {
				xml.append(line);
			}
			
			JSONObject jsonObj = (JSONObject)new XMLSerializer().read(xml.toString());
			System.out.println(jsonObj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(fis!=null) {
				try {
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(br!=null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}	
}
