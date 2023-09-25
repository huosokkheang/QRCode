package com.solo.configLogger;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.core.liveLog.MonitoringFileService;

@Component
public class MonitoringFileServiceConfig extends MonitoringFileService{

	public MonitoringFileServiceConfig(@Value("${logging.file.path}") String directory, @Value("${logging.file.name:spring.log}") String fileName) throws IOException {
		super(directory, fileName);
	}

}
