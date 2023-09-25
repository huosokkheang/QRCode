package com.solo.configLogger;

import org.springframework.stereotype.Component;

import com.core.liveLog.LogsService;
import com.core.liveLog.LogsTemplate;
import com.core.liveLog.MonitoringFileService;

@Component
public class LogsServiceConfig extends LogsService {

	public LogsServiceConfig(LogsTemplate template, MonitoringFileService monitoringFileService) {
		super(template, monitoringFileService);
	}

}
