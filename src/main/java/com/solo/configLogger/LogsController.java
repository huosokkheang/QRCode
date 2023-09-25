package com.solo.configLogger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RestController;

import com.core.liveLog.LiveLogsController;

@RestController
public class LogsController extends LiveLogsController{
	
	private static final Logger logger = LogManager.getLogger(LogsController.class);

}
