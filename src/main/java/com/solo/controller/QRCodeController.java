package com.solo.controller;

import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.SystemUtils;
import org.apache.tomcat.jni.File;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.core.collection.Solo;
import com.core.exception.business.SException;
import com.core.exception.business.SoloException;
import com.core.util.QrBarcode.QRCodeWiFiSecureTypeCode;
import com.core.util.QrBarcode.QRCodeWithLogoGenerator;
import com.core.util.QrBarcode.QRCodeWithTextUnderGenerator;
import com.core.util.QrBarcode.enums.ImgSize;
import com.core.util.qrcode.SQRCodeToByte;
@RestController
@RequestMapping("/qrcode")
public class QRCodeController {
	
	private static final String logo = "logo.png";
	private static final String fileName = "QRCode.png";
	
	private static String getPathLocation() {
		String data;
		String operationSystem = System.getProperty("os.name");
		String os = SystemUtils.OS_NAME;
		if(operationSystem.contains("Windows")) {
			data = System.getProperty("user.dir") + "/";
		}else {
			data = "/opt/photos";
		}
		return data;
	}
	
	@GetMapping(value = "/", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> generateQRCode() throws SException {
		return SQRCodeToByte.GenerateQRCode("Content", 500, 500);
	}
	
	@GetMapping(value = "/readQRCode")
	public Solo readQRCode() throws SException {
		Solo solo = new Solo();
		solo.put("decode_qrcode", SQRCodeToByte.readQRcode(getPathLocation()+ fileName));
		return solo;
	}
	
	@GetMapping(value = "/background", produces = MediaType.IMAGE_PNG_VALUE)
    public void generateQRCodeBG(HttpServletResponse response) throws SoloException, Exception {
    	
    	byte[] qrcode = SQRCodeToByte.generateQRCodeBG("Hello Content", ImgSize.BIG.getWidth(), ImgSize.BIG.getHeight(), getPathLocation() + logo, Color.BLACK);
    	response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(qrcode, response.getOutputStream());
    }
	
	@GetMapping(value = "/byte", produces = IMAGE_PNG_VALUE)
	public void generateQRCodeWiyhLogo(HttpServletResponse response) {
		try {			
			Solo solo = new Solo();
	    	solo.setString("content", "Content");
	    	solo.setInt("width", ImgSize.BIG.getWidth());
	    	solo.setInt("height", ImgSize.BIG.getHeight());
	    	solo.setString("logo", getPathLocation() + logo);
			byte[] qrcodeByte = QRCodeWithLogoGenerator.generateQRCodeByte(solo);
			response.setContentType(MediaType.IMAGE_JPEG_VALUE);
			StreamUtils.copy(qrcodeByte, response.getOutputStream());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    @GetMapping(value = "/file", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
	public ResponseEntity<byte[]> generateQRCodeWiyhLogoFile(HttpServletResponse response) throws IOException {
		Solo solo = new Solo();
		String image = null;
		byte[] imageBytes = null;
		try {
	    	solo.setString("content", "Content");
	    	solo.setInt("width", ImgSize.BIG.getWidth());
	    	solo.setInt("height", ImgSize.BIG.getHeight());
	    	solo.setString("logo", getPathLocation() + logo);
	    	solo.setString("imgPath", getPathLocation());
	    	solo.setString("fileName", fileName);
			image = QRCodeWithLogoGenerator.generateQRCodeFile(solo);
			Path imagePath = Paths.get(getPathLocation(), image);
			if (!Files.exists(imagePath)) {
	            return ResponseEntity.notFound().build();
	        }
			imageBytes = Files.readAllBytes(imagePath);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes); // Adjust MediaType as needed.
	}

	@GetMapping(value = "/generateQRCodeWiFi", produces = MediaType.IMAGE_PNG_VALUE)
    public void generateQRCodeWiFi(HttpServletResponse response) throws SoloException, Exception {
    	
    	Solo wifi = new Solo();
    	wifi.setString("wifiName", "Notebook");
    	wifi.setString("password", "8888999955552222");
    	String WiFiPattern = QRCodeWiFiSecureTypeCode.generateQRCode(QRCodeWiFiSecureTypeCode.WPA_WPA2_WPA3, wifi);
    	System.out.println(WiFiPattern);
    	Solo param = new Solo();
    	param.setString("content", WiFiPattern);
    	param.setString("textUnder", "Wifi name : " + wifi.getString("wifiName"));
    	param.setString("width", "500");
    	param.setString("height", "500");
    	
    	byte[] qrcode = QRCodeWithTextUnderGenerator.generateQRCode(param);
    	response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(qrcode, response.getOutputStream());
    }
	
	@GetMapping(value = "/generateQRCode", produces = MediaType.IMAGE_PNG_VALUE)
    public void generateQRCode(HttpServletResponse response) throws SoloException, Exception {
    	
    	Solo param = new Solo();
    	param.setString("content", "Hey!");
    	param.setString("textUnder", "Scan Me");
    	param.setString("width", "500");
    	param.setString("height", "500");
    	
    	byte[] qrcode = QRCodeWithTextUnderGenerator.generateQRCode(param);
    	response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(qrcode, response.getOutputStream());
    }
}
