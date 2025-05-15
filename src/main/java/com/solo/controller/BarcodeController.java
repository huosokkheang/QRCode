package com.solo.controller;

import static com.core.util.convert.SConverter.convertBufferedImageToByteArray;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.core.collection.Solo;
import com.core.util.QrBarcode.BarcodeGenerator;

@RestController
@RequestMapping("/BarCode")
public class BarcodeController {

	@GetMapping(value = "/{code}", produces = IMAGE_PNG_VALUE)
    public void generateBarcodeWithText(HttpServletResponse response, @PathVariable("code") final String code) {
        try {
        	Solo solo = new Solo();
        	solo.setString("code", code);
            byte[] bs = BarcodeGenerator.generateBarcodeWithTextByte(response, solo);
        	response.setContentType(MediaType.IMAGE_JPEG_VALUE);
    		StreamUtils.copy(bs, response.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/{type}/{barcode}", produces = IMAGE_PNG_VALUE)
    public void generate(HttpServletResponse response, @PathVariable("type") final String type,
                                                  @PathVariable("barcode") final String barcodeText) throws Exception {

		BarcodeGenerator barcodeGenerator = new BarcodeGenerator();
        try {
        	BufferedImage bufferedImage = null;
        	if(type.equalsIgnoreCase("EAN13")) {
        		// 978020137962
        		if(barcodeText.length() == 12) {
        			bufferedImage = barcodeGenerator.generateEAN13BarcodeImage(barcodeText);
        		}else {
        			throw new Exception("Code must be 12 length");
        		}
        	}
        	if(type.equalsIgnoreCase("UPC")) {
        		// 12345678901
        		if(barcodeText.length() == 11) {
            		bufferedImage = barcodeGenerator.generateUPCBarcodeImage(barcodeText);
        		}else {
        			throw new Exception("Code must be 11 length");
        		}
        	}
        	if(type.equalsIgnoreCase("EAN128")) {
        		// 0101234567890128TEC
        		if(barcodeText.length() == 19) {
            		bufferedImage = barcodeGenerator.generateEAN128BarCodeImage(barcodeText);
        		}else {
        			throw new Exception("Code must be 19 length");
        		}
        	}
        	if(type.equalsIgnoreCase("CODE128")) {
        		// any-string
        		bufferedImage = barcodeGenerator.generateCode128BarCodeImage(barcodeText);
        	}
        	if(type.equalsIgnoreCase("USPS") && barcodeText.length() == 9) {
        		// 123456789
        		if(barcodeText.length() == 9) {
            		bufferedImage = barcodeGenerator.generateUSPSBarcodeImage(barcodeText);
        		}else {
        			throw new Exception("Code must be 9 length");
        		}
        	}
        	if(type.equalsIgnoreCase("SCC14")) {
        		bufferedImage = barcodeGenerator.generateSCC14ShippingCodeBarcodeImage(barcodeText);
        	}
        	if(type.equalsIgnoreCase("CODE39")) {
        		bufferedImage = barcodeGenerator.generateCode39BarcodeImage(barcodeText);
        	}
        	if(type.equalsIgnoreCase("GTIN")) {
        		bufferedImage = barcodeGenerator.generateGlobalTradeItemNumberBarcodeImage(barcodeText);
        	}
        	if(type.equalsIgnoreCase("PDF417")) {
        		bufferedImage = barcodeGenerator.generatePDF417BarcodeImage(barcodeText);
        	}
        	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        	ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        	byte[] bs = convertBufferedImageToByteArray(bufferedImage, "png");
        	response.setContentType(MediaType.IMAGE_JPEG_VALUE);
    		StreamUtils.copy(bs, response.getOutputStream());
    		
        } catch (Exception ex) {
            throw ex;
        }
    }
}
