/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.core.logger;

import com.tamnd.core.util.ZSystemProp;
import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.net.SyslogAppender;

/**
 *
 * @author namnq
 * @sample vi du cai dat system-properties: -Dzappname=zsampleapp
 *
 */
public class ZLogger {

	/*
	 * list level of log
	 *  OFF/FATAL, ERROR, WARN, INFO, DEBUG/TRACE/ALL
	 *
	 */
	private static final Logger _Log;

	static {
		String appName = ZSystemProp.GetAppName();//check if zappname is exist
		String appProf = ZSystemProp.GetAppProf();
		String configDir = ZSystemProp.GetConfDir();
		String logConfigFile = ZSystemProp.GetProp("zlogconffile", "log4j.ini");
		String path;
		if (configDir.endsWith("/")) {
			path = configDir + appProf + "." + logConfigFile;
		} else {
			path = configDir + "/" + appProf + "." + logConfigFile;
		}
		
		File f = new File(path);
		if (f.exists()) {
			PropertyConfigurator.configure(path);
		} else {
			URL url = ClassLoader.getSystemResource("conf/" + appProf + ".log4j.ini");
			assert (url != null);
			PropertyConfigurator.configure(url);
		}
		Enumeration<Appender> appenders = Logger.getRootLogger().getAllAppenders();
		boolean log4z = false, syslog = false;
		while (appenders.hasMoreElements()) {
			Appender appender = appenders.nextElement();
			Class clas = appender.getClass();
			if (clas == SyslogAppender.class) {
				syslog = true;
			}
		}
		if (syslog) {
			System.err.println("SYSLOG FOUND!!! Syslog is should removed out, use standard log4z instead");
		}
		//
		_Log = ZLogger.getLogger(ZLogger.class);
		_Log.info("========================== ZLogger starts new session ==========================");
	}

	public static org.apache.log4j.Logger getLogger(Class clazz) {
		if (clazz == null) {
			return ZEmptyLogger.instance;
		}
		org.apache.log4j.Logger ret = org.apache.log4j.Logger.getLogger(clazz);
		if (ret == null) {
			return ZEmptyLogger.instance;
		}
		return ret;
	}

	public static org.apache.log4j.Logger getLogger(String name) {
		if (name == null) {
			return ZEmptyLogger.instance;
		}
		name = name.trim();
		if (name.isEmpty()) {
			return ZEmptyLogger.instance;
		}
		org.apache.log4j.Logger ret = org.apache.log4j.Logger.getLogger(name);
		if (ret == null) {
			return ZEmptyLogger.instance;
		}
		return ret;
	}
}
