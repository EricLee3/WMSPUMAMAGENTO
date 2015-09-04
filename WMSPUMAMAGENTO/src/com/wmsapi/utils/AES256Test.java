package com.wmsapi.utils; 
import java.io.UnsupportedEncodingException; 
import java.security.InvalidAlgorithmParameterException; 
import java.security.InvalidKeyException; 
import java.security.NoSuchAlgorithmException; 
import javax.crypto.BadPaddingException; 
import javax.crypto.IllegalBlockSizeException; 
import javax.crypto.NoSuchPaddingException; 
import static org.hamcrest.CoreMatchers.is; 
import static org.junit.Assert.assertThat; 
import org.junit.Test;  

import sun.security.krb5.internal.crypto.Aes256;

public class AES256Test {  

String id = "testid";  
String custrnmNo = "111111";  
String custNm = "�׽�Ʈ";         

	public void encDesTest() throws InvalidKeyException, UnsupportedEncodingException, 
					NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, 
					IllegalBlockSizeException, BadPaddingException {      
//		AES256Util a256 = AES256Util.getInstance();        
		String enId = AES256Util.AES_Encode(id);      
		String enYyyymmdd = AES256Util.AES_Encode(custrnmNo);      
		String enCustNm = AES256Util.AES_Encode(custNm);        
		String desId = AES256Util.AES_Decode(enId);      
		String desYyyymmdd = AES256Util.AES_Decode(enYyyymmdd);      
		String desCustNm = AES256Util.AES_Decode(enCustNm);        
		assertThat(id, is(desId));      
		assertThat(custrnmNo, is(desYyyymmdd));      
		assertThat(custNm, is(desCustNm));  
		
		System.out.println("��ȣȭid:"+enId);
		System.out.println("��ȣȭyyyymmdd:"+enYyyymmdd);
		System.out.println("��ȣȭnm:"+enCustNm);
		
		System.out.println("��ȣȭid:"+desId);
		System.out.println("��ȣȭyyyymmdd:"+desYyyymmdd);
		System.out.println("��ȣȭnm:"+desCustNm);
	}

	public static void main(String args[]) {
		AES256Test test = new AES256Test();
		try {
			test.encDesTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}