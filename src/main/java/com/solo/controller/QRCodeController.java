package com.solo.controller;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;

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

@RestController
@RequestMapping("/qrcode")
public class QRCodeController {
	
	@RequestMapping(value = "/background/{url:.+}", method = RequestMethod.GET)
	public ResponseEntity<Resource> QRCodeWithBG(@PathVariable String url) throws Exception {
		SLog.info.print("start generate QRCode");
		SQRCode.GenerateQRCodeWithBG(url, 900, 900, System.getProperty("user.dir") +"/"+ "logo.png", System.getProperty("user.dir") +"/"+ "logo_new.png", Color.BLUE);
		HttpHeaders headers = new HttpHeaders();
		String path = System.getProperty("user.dir") +"/"+ "logo_new.png";
		File file = new File(path);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		SLog.info.print("end generate QRCode");
		headers.setContentType(MediaType.IMAGE_PNG);
		return ResponseEntity.ok().headers(headers).contentLength(file.length()).body(resource);
	}
	
	@RequestMapping(value = "/logo/{url:.+}", method = RequestMethod.GET)
	public ResponseEntity<Resource> QRCodeWithLogo(@PathVariable String url) throws Exception {
		SLog.info.print("start generate QRCode");
		
		SQRCode.GenerateQRCodeWithLogo(url, System.getProperty("user.dir") +"/"+ "logo.png", System.getProperty("user.dir") +"/"+ "qrcode.png", "png", 1000, 1000);
		
		String imageOriginal = System.getProperty("user.dir") + "/qrcode.png"; 
		String imageOverlay = System.getProperty("user.dir")+"/"+"scanme.PNG"; 
		String output_location = System.getProperty("user.dir")+"/"+"qrcode.png";
		int x = 360; 
		int y = 800;
		OverlayImage.ApplyOverlay(imageOriginal, imageOverlay, output_location, x, y);
		
		HttpHeaders headers = new HttpHeaders();
		String path = System.getProperty("user.dir") +"/"+ "qrcode.png";
		File file = new File(path);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		SLog.info.print("end generate QRCode");
		headers.setContentType(MediaType.IMAGE_PNG);
		return ResponseEntity.ok().headers(headers).contentLength(file.length()).body(resource);
	}

	@RequestMapping(value = "/qrcodeWifi/{wifiName:.+}/{wifiPass:.+}", method = RequestMethod.GET)
	public ResponseEntity<Resource> QRCode_wifi(@PathVariable String wifiName, @PathVariable String wifiPass) throws Exception {
		SLog.info.print("start generate QRCode Wifi");
		SQRCode.generateQRCodeWifi_WPA_WPA2_WPA3(wifiName, wifiPass, 500, 500, System.getProperty("user.dir") + "/wifi.png");

		HttpHeaders headers = new HttpHeaders();
		String path = System.getProperty("user.dir") +"/"+ "wifi.png";
		File file = new File(path);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		SLog.info.print("end generate QRCode");
		headers.setContentType(MediaType.IMAGE_PNG);
		return ResponseEntity.ok().headers(headers).contentLength(file.length()).body(resource);
	}
}
