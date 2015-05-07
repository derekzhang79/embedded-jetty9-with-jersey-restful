/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.core.util;

/**
 *
 * @author namnq
 */
public class ZSystemProp {

	public static String GetAppName() {
		String ret = null;
		try {
			ret = System.getProperty("zappname");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (ret == null) {
			System.err.println("\nMissing required property \"zappname\", rerun the application with additional parameter \"-Dzappname=your_app_name\"\n");
			Runtime.getRuntime().exit(-1);
		}
		return ret;

	}

	public static String GetAppProf() {
		String ret = null;
		try {
			ret = System.getProperty("zappprof");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (ret == null) {
			System.err.println("\nMissing required property \"zappprof\", rerun the application with additional parameter \"-Dzappprof=your_app_profile\" (your_app_profile may be one of: \"production\", \"staging\", \"development\")\n");
			Runtime.getRuntime().exit(-1);
		}
		return ret;
	}

	public static String GetConfDir() {
		return System.getProperty("zconfdir", "conf");
	}

	public static String GetProp(String key, String def) {
		return System.getProperty(key, def);
	}

	public static String GetProp(String key) {
		return System.getProperty(key);
	}

	public static String SetProp(String key, String value) {
		return System.setProperty(key, value);
	}
}
