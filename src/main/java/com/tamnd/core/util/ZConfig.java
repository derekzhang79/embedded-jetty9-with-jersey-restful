/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.core.util;

import com.tamnd.core.exception.CircularReferenceException;
import com.tamnd.core.exception.InvalidParamException;
import com.tamnd.core.exception.NotExistException;
import com.tamnd.core.util.common.CompositeKey;
import com.tamnd.core.util.common.ZUtil;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

/**
 *
 * @author tamnd2
 */
public class ZConfig {

	public static final ZConfig Instance = new ZConfig();

	private static final String _ClazzKeySep = "@";
	private static final String _InstNameKeySep = ".";
	private static final ConcurrentMap<CompositeKey.Three, String> _fqKeysMap = new NonBlockingHashMap<>();
	private final ConcurrentMap<String, String> _configMap = new NonBlockingHashMap<>();//new ConcurrentHashMap<String, String>(1024);

	private ZConfig() {
		_init();
	}

	private void _init() {
		//~~~~~~~~~~ init configs from files ~~~~~~~~~~
		String appName = ZSystemProp.GetAppName();//check if zappname is exist
		String appProf = ZSystemProp.GetAppProf();
		String configDir = ZSystemProp.GetConfDir();
		String prePath;
		if (configDir.endsWith("/")) {
			prePath = configDir + appProf + ".";
		} else {
			prePath = configDir + "/" + appProf + ".";
		}
		String configFiles = ZSystemProp.GetProp("zconffiles", "config.ini");
		if (configFiles != null && !configFiles.isEmpty()) {
			StringTokenizer strTok = new StringTokenizer(configFiles, ",");
			while (strTok.hasMoreTokens()) {
				String fileName = strTok.nextToken();
				String path = prePath + fileName;
				try {
					ConfigToMap(_configMap, LoadFromFile(path), false);
				} catch (Exception ex) {
					System.err.println(ex);
				}
			}
		} else {
			System.out.println("No configuration file is specified");
		}
	}

	private static void ConfigToMap(ConcurrentMap<String, String> map, Configuration config, boolean overwrDup) {
		if (config == null) {
			return;
		}
		Iterator<String> keyIt = config.getKeys();
		while (keyIt.hasNext()) {
			String key = keyIt.next();
			if (key == null || key.isEmpty()) {
				continue;
			}
			try {
				String value = config.getString(key);
				if (value == null) {
					continue;
				}
				if (overwrDup) {
					String oldVal = map.put(key, value);
					if (oldVal != null) {
						System.out.println("Configuration key \"" + key + "\" has old value \"" + oldVal + "\" has been overwritten by new value \"" + value + "\"");
					}
				} else {
					String oldVal = map.putIfAbsent(key, value);
					if (oldVal != null) {
						System.out.println("Configuration key \"" + key + "\" has value \"" + oldVal + "\" NOT be overwrited by new value \"" + value + "\"");
					}
				}
			} catch (Exception ex) {
				System.err.println(ex);
			}
		}
	}

	private static Configuration LoadFromFile(String path) throws ConfigurationException {
		if (path.endsWith(".ini") || path.endsWith(".properties")) {
			HierarchicalINIConfiguration config = new HierarchicalINIConfiguration();
			config.setDelimiterParsingDisabled(true);
			config.setFileName(path);
			config.load();
			return config;
		} else if (path.endsWith(".xml")) {
			return new XMLConfiguration(path);
		} else {
			throw new ConfigurationException("Unknown configuration file extension");
		}
	}

	public static String ClazzKeyToKey(Class clazz, String instName, String key) {
		if (key == null) {
			return null;
		}
		assert (clazz != null && instName != null);
		String strClazz = ZUtil.getClassSimpleName(clazz);
		instName = instName.trim();
		key = key.trim();
		////////////////////////////////////////////////////////////////////////
		CompositeKey.Three compositeKey = CompositeKey.Three.ObjectPool.borrowObject().set(clazz, instName, key);
		String fqKey = _fqKeysMap.get(compositeKey);
		if (fqKey == null) {
			StringBuilder sb = new StringBuilder(512);
			sb.append(strClazz).append(_ClazzKeySep);
			if (instName.isEmpty() || key.isEmpty()) {
				sb.append(instName).append(key);
			} else {
				sb.append(instName).append(_InstNameKeySep).append(key);
			}
			fqKey = sb.toString();
			//put a new key instead of the key from pool
			//, sothat the map could not compare object referrence
			_fqKeysMap.put(new CompositeKey.Three(clazz, instName, key), fqKey);
		}
		CompositeKey.Three.ObjectPool.returnObject(compositeKey);
		return fqKey;
	}

	private String _getPropertyInternal(String key, boolean addToSmAgent) throws NotExistException {
		assert (key != null);
		String origKey = key;
		String value = _configMap.get(key);
		//
		while (value != null && value.startsWith("@")) {
			key = value.substring(1).trim();
			if (key.equals(origKey)) {
				throw new CircularReferenceException(origKey);
			}
			value = _configMap.get(key);
		}
		//
		if (value == null) {
			throw new NotExistException(origKey + " is refered to " + key);
		}
		return value;
	}

	public String _getProperty(String key) throws NotExistException, InvalidParamException {
		if (key == null) {
			throw new InvalidParamException("Key is null");
		}
		key = key.trim();
		if (key.isEmpty()) {
			throw new InvalidParamException("Key is empty");
		}
		return _getPropertyInternal(key, true);
	}

	////////////////////////////////////////////////////////////////////////////
	///specdatatype read with throwing exceptions
	///
	public boolean getBoolean(String key) throws NotExistException, InvalidParamException {
		String strVal = _getProperty(key);

		if (strVal.equalsIgnoreCase("true")) {
			return true;
		} else if (strVal.equalsIgnoreCase("false")) {
			return false;
		} else if (strVal.equalsIgnoreCase("yes")) {
			return true;
		} else if (strVal.equalsIgnoreCase("no")) {
			return false;
		} else if (strVal.equalsIgnoreCase("on")) {
			return true;
		} else if (strVal.equalsIgnoreCase("off")) {
			return false;
		} else if (strVal.equalsIgnoreCase("1")) {
			return true;
		} else if (strVal.equalsIgnoreCase("0")) {
			return false;
		} else {
			throw new NumberFormatException("Wrong format while parsing boolean");
		}
	}

	public byte getByte(String key) throws NotExistException, InvalidParamException {
		return Byte.parseByte(_getProperty(key));
	}

	public double getDouble(String key) throws NotExistException, InvalidParamException {
		return Double.parseDouble(_getProperty(key));
	}

	public float getFloat(String key) throws NotExistException, InvalidParamException {
		return Float.parseFloat(_getProperty(key));
	}

	public int getInt(String key) throws NotExistException, InvalidParamException {
		return Integer.parseInt(_getProperty(key));
	}

	public long getLong(String key) throws NotExistException, InvalidParamException {
		return Long.parseLong(_getProperty(key));
	}

	public short getShort(String key) throws NotExistException, InvalidParamException {
		return Short.parseShort(_getProperty(key));
	}

	public String getString(String key) throws NotExistException, InvalidParamException {
		return _getProperty(key);
	}

	////////////////////////////////////////////////////////////////////////////
	///fromclass and specdatatype read with throwing exceptions
	///
	public boolean getBoolean(Class clazz, String instName, String key) throws NotExistException, InvalidParamException {
		return getBoolean(ClazzKeyToKey(clazz, instName, key));
	}

	public byte getByte(Class clazz, String instName, String key) throws NotExistException, InvalidParamException {
		return getByte(ClazzKeyToKey(clazz, instName, key));
	}

	public double getDouble(Class clazz, String instName, String key) throws NotExistException, InvalidParamException {
		return getDouble(ClazzKeyToKey(clazz, instName, key));
	}

	public float getFloat(Class clazz, String instName, String key) throws NotExistException, InvalidParamException {
		return getFloat(ClazzKeyToKey(clazz, instName, key));
	}

	public int getInt(Class clazz, String instName, String key) throws NotExistException, InvalidParamException {
		return getInt(ClazzKeyToKey(clazz, instName, key));
	}

	public long getLong(Class clazz, String instName, String key) throws NotExistException, InvalidParamException {
		return getLong(ClazzKeyToKey(clazz, instName, key));
	}

	public short getShort(Class clazz, String instName, String key) throws NotExistException, InvalidParamException {
		return getShort(ClazzKeyToKey(clazz, instName, key));
	}

	public String getString(Class clazz, String instName, String key) throws NotExistException, InvalidParamException {
		return getString(ClazzKeyToKey(clazz, instName, key));
	}

	////////////////////////////////////////////////////////////////////////////
	///fromclass and specdatatype read without throwing exception (use defaultVal instead)
	///
	public boolean getBoolean(Class clazz, String instName, String key, boolean defaultVal) {
		try {
			return getBoolean(clazz, instName, key);
		} catch (Exception ex) {
			return defaultVal;
		}
	}

	public byte getByte(Class clazz, String instName, String key, byte defaultVal) {
		try {
			return getByte(clazz, instName, key);
		} catch (Exception ex) {
			return defaultVal;
		}
	}

	public double getDouble(Class clazz, String instName, String key, double defaultVal) {
		try {
			return getDouble(clazz, instName, key);
		} catch (Exception ex) {
			return defaultVal;
		}
	}

	public float getFloat(Class clazz, String instName, String key, float defaultVal) {
		try {
			return getFloat(clazz, instName, key);
		} catch (Exception ex) {
			return defaultVal;
		}
	}

	public int getInt(Class clazz, String instName, String key, int defaultVal) {
		try {
			return getInt(clazz, instName, key);
		} catch (Exception ex) {
			return defaultVal;
		}
	}

	public long getLong(Class clazz, String instName, String key, long defaultVal) {
		try {
			return getLong(clazz, instName, key);
		} catch (Exception ex) {
			return defaultVal;
		}
	}

	public short getShort(Class clazz, String instName, String key, short defaultVal) {
		try {
			return getShort(clazz, instName, key);
		} catch (Exception ex) {
			return defaultVal;
		}
	}

	public String getString(Class clazz, String instName, String key, String defaultVal) {
		try {
			return getString(clazz, instName, key);
		} catch (Exception ex) {
			return defaultVal;
		}
	}
}
