package com.solo.controller;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.core.logger.SLog;
import com.core.util.qrcode.SQRCode;
import com.core.util.qrcode.SQRCodeToByte;
@RestController
@RequestMapping("/qrcode")
public class QRCodeController {
	
	@RequestMapping(value = "/redirectly", method = RequestMethod.GET)
	public ResponseEntity<Resource> QRCodeWithBG() throws Exception {
		
		//Write log
		SLog.info.print("start generate QRCode");
		
		//Generate QRCode
		String originalLogo = System.getProperty("user.dir") +"/"+ "logo.PNG";
		String newLogo = System.getProperty("user.dir") +"/"+ "logo_new.png";
		SQRCode.GenerateQRCodeWithBG("www.google.com", 900, 900, originalLogo, newLogo, Color.BLUE);
		
		HttpHeaders headers = new HttpHeaders();
		File file = new File(newLogo);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		SLog.info.print("end generate QRCode");
		headers.setContentType(MediaType.IMAGE_PNG);
		return ResponseEntity.ok().headers(headers).contentLength(file.length()).body(resource);
	}
	
	@RequestMapping(value = "/logo/{url:.+}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> QRCodeWithLogo(@PathVariable("url") String url) throws Exception {
		
		//Write log
		SLog.info.print("start generate QRCode");
		//Generate QRCode
		String originalLogo = System.getProperty("user.dir") +"/"+ "logo.png";
		return SQRCodeToByte.GenerateQRCodeWithLogo(url, originalLogo, 1000, 1000);
	}

	@RequestMapping(value = "/qrcodeWifi/{wifiName:.+}/{wifiPass:.+}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> QRCode_wifi(@PathVariable("wifiName") String wifiName, @PathVariable("wifiPass") String wifiPass, HttpServletResponse response) throws Exception {
		
		//Write log
		SLog.info.print("start generate QRCode Wifi");
		return SQRCodeToByte.generateQRCodeWifi_WPA_WPA2_WPA3(wifiName, wifiPass, 1000, 1000);
	}
}
