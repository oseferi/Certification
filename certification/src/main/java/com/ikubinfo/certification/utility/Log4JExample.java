package com.ikubinfo.certification.utility;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class Log4JExample {
	 static Logger log = Logger.getLogger(Log4JExample.class);
	   
	   public static void main(String[] args)throws IOException,SQLException{
	      log.debug("Hello this is a debug message");
	      log.info("Hello this is an info message");
	      log.fatal("Fatal");
	   }
}
