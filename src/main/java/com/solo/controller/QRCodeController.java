package com.solo.controller;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.SystemUtils;
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
import com.core.util.image.OverlayImage;
import com.core.util.qrcode.SQRCode;
import com.core.util.qrcode.SQRCodeToByte;
@RestController
@RequestMapping("/qrcode")
public class QRCodeController {
	
	private static String getPathLocation() {
		String data;
		String operationSystem = System.getProperty("os.name");
		String os = SystemUtils.OS_NAME;
		if(operationSystem.contains("Windows")) {
			data = System.getProperty("user.dir");
		}else {
			data = "/opt/image";
		}
		return data;
	}
	
	@RequestMapping(value = "/redirectly/{text:.+}", method = RequestMethod.GET)
	public ResponseEntity<Resource> QRCodeWithBG(@PathVariable("text") String text) throws Exception {
		
		//Write log
		SLog.info.print("start generate QRCode");
		
		//Generate QRCode
		String originalLogo = getPathLocation() +"/"+ "logo.png";
		String newLogo = getPathLocation() +"/"+ "logo_new.png";
		
		SQRCode.GenerateQRCodeWithBG(text, 900, 900, originalLogo, newLogo, Color.BLUE);
		
		HttpHeaders headers = new HttpHeaders();
		File file = new File(newLogo);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		SLog.info.print("end generate QRCode");
		headers.setContentType(MediaType.IMAGE_PNG);
		return ResponseEntity.ok().headers(headers).contentLength(file.length()).body(resource);
	}
	
	@RequestMapping(value = "/logo/{text:.+}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> QRCodeWithLogo(@PathVariable("text") String text) throws Exception {
		
		//Write log
		SLog.info.print("start generate QRCode");
		//Generate QRCode
		String originalLogo = getPathLocation() +"/"+ "logo.png";
		return SQRCodeToByte.GenerateQRCodeWithLogo(text, originalLogo, 1000, 1000);
	}

	@RequestMapping(value = "/qrcodeWifi/{wifiName:.+}/{wifiPass:.+}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> QRCode_wifi(@PathVariable("wifiName") String wifiName, @PathVariable("wifiPass") String wifiPass, HttpServletResponse response) throws Exception {
		
		//Write log
		SLog.info.print("start generate QRCode Wifi");
		return SQRCodeToByte.generateQRCodeWifi_WPA_WPA2_WPA3(wifiName, wifiPass, 1000, 1000);
	}
	
	@RequestMapping(value = "/scanMe/{text:.+}", method = RequestMethod.GET)
	public ResponseEntity<Resource> QRCodeWithLogoScanMe(@PathVariable("text") String text) throws Exception {
		SLog.info.print("start generate QRCode");
		
		SQRCode.GenerateQRCodeWithLogo(text, getPathLocation() +"/"+ "logo.png", getPathLocation() +"/"+ "qrcode.png", "png", 1000, 1000);
		
		String imageOriginal = getPathLocation() + "/qrcode.png"; 
		String imageOverlay = getPathLocation()+"/"+"scanme.png"; 
		String output_location = getPathLocation()+"/"+"qrcode.png";
		
		int x = 420; 
		int y = 870;
		OverlayImage.ApplyOverlay(imageOriginal, imageOverlay, output_location, x, y);
		
		HttpHeaders headers = new HttpHeaders();
		String path = getPathLocation() +"/"+ "qrcode.png";
		File file = new File(path);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		SLog.info.print("end generate QRCode");
		headers.setContentType(MediaType.IMAGE_PNG);
		return ResponseEntity.ok().headers(headers).contentLength(file.length()).body(resource);
	}
}
