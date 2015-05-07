/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.core.util.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentMap;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

/**
 *
 * @author namnq
 */
public class ZUtil {

	////////////////////////////////////////////////////////////////////////////
	//common constants
	public static final int KILO = (1024);
	public static final int MEGA = (1048576);
	public static final int GIGA = (1073741824);
	public static final long TETRA = (1099511627776l);

	public static final int SECS_IN_MIN = (60);
	public static final int MINS_IN_HOUR = (60);
	public static final int HOURS_IN_DAY = (24);

	public static final int SECS_IN_HOUR = (3600);
	public static final int SECS_IN_DAY = (86400);
	public static final int MINS_IN_DAY = (1440);
	public static final int _100NANOS_IN_MILI = (10000);

	////////////////////////////////////////////////////////////////////////////
	public static final Random RandGen = new Random(System.currentTimeMillis());
	public static final TimeZone DefaultTimeZone = TimeZone.getDefault();
	public static final int DefaultTimeZoneRawOffset = DefaultTimeZone.getRawOffset();
	public static final long StartTimeMillisInDTZ;
	public static final long StartHiRTimeNanos;

	////
	public static final IdGen IdGen = new IdGen();

	////////////////////////////////////////////////////////////////////////////
	static class ClassMeta {

		String simpleName;
	}
	private static final ConcurrentMap<Class, ClassMeta> _classSimpleNameMap = new NonBlockingHashMap<Class, ClassMeta>();

	////////////////////////////////////////////////////////////////////////////
	static {
		long timeMillis = System.currentTimeMillis();
		long timeNanos = System.nanoTime();
		StartTimeMillisInDTZ = timeMillis + DefaultTimeZoneRawOffset; //same as currentTimeMillisInDTZ();
		StartHiRTimeNanos = timeNanos;
	}

	public static final long nextGID() {
		return IdGen.nextId();
	}

	public static final String getClassSimpleName(Class cls) {
		if (cls == null) {
			return "";
		}
		ClassMeta cmeta = _classSimpleNameMap.get(cls);
		if (cmeta == null) {
			cmeta = new ClassMeta();
			cmeta.simpleName = cls.getSimpleName();
			ClassMeta old = _classSimpleNameMap.putIfAbsent(cls, cmeta);
			if (old != null) {
				cmeta = old;
			}
		}
		return cmeta.simpleName;
	}

	public static StringBuilder sbExpandMore(StringBuilder sb, int len) {
		assert (sb != null);
		sb.ensureCapacity(sb.length() + len);
		return sb;
	}

	/**
	 * logarith base 2 of an integer
	 *
	 * @param n
	 * @return logarith base 2 of an integer input, if the input is zero then
	 * result is zero
	 */
	public static int binLoga(int n) {
		int log = 0;
		if ((n & 0xffff0000) != 0) {
			n >>>= 16;
			log = 16;
		}
		if (n >= 256) {
			n >>>= 8;
			log += 8;
		}
		if (n >= 16) {
			n >>>= 4;
			log += 4;
		}
		if (n >= 4) {
			n >>>= 2;
			log += 2;
		}
		return log + (n >>> 1);
	}

	/**
	 * Convert high resolution time in nanoseconds (System.nanoTime()) to unix
	 * time in default time zone in milliseconds
	 *
	 * @param hirTimeNanos high resolution time in nanoseconds
	 * @return unix time in default time zone in milliseconds
	 */
	public static long timeMillisInDTZFromHiRTimeNanos(long hirTimeNanos) {
		return StartTimeMillisInDTZ + ((hirTimeNanos - StartHiRTimeNanos) / 1000000);
	}

	/**
	 * the currentTimeMillis in default time zone
	 */
	public static long currentTimeMillisInDTZ() {
		return System.currentTimeMillis() + DefaultTimeZoneRawOffset;
	}

	/**
	 * Convert date-time to unix time in default time zone in milliseconds
	 *
	 * @param year sample '2014'
	 * @param month zero-based 0-11
	 * @param dayOfMonth 1-31
	 * @return unix time in default time zone in milliseconds
	 */
	public static long timeMillisInDTZ(int year, int month, int dayOfMonth) {
		return timeMillisInDTZ(year, month, dayOfMonth, 0, 0, 0, 0);
	}

	/**
	 * Convert date-time to unix time in default time zone in milliseconds
	 *
	 * @param year sample '2014'
	 * @param month zero-based 0-11
	 * @param dayOfMonth 1-31
	 * @param hourOfDay 0-23
	 * @param minute 0-59
	 * @param second 0-59
	 * @param millis 0-999
	 * @return unix time in default time zone in milliseconds
	 */
	public static long timeMillisInDTZ(int year, int month, int dayOfMonth,
			int hourOfDay, int minute, int second, int millis) {
		GregorianCalendar calendar = new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute, second);
		return calendar.getTimeInMillis() + millis + ZUtil.DefaultTimeZoneRawOffset;
	}

	public static void FlushSysStreams() {
		System.out.flush();
		System.err.flush();
	}

	public static void PrintStackTrace(Throwable ex, int rdepth) {
		PrintStackTrace(System.err, ex, rdepth);
	}

	public static void PrintStackTrace(PrintStream s, Throwable ex, int rdepth) {
		if (rdepth < 0) {
			rdepth = 0;
		}
		synchronized (s) {
			s.println(ex);
			StackTraceElement[] trace = ex.getStackTrace();
			for (int i = rdepth; i < trace.length; i++) {
				s.println("\tat " + trace[i]);
			}
		}
	}

	public static long versionToLong(String version) throws NumberFormatException {
		if (version == null) {
			return -1;
		}
		long ret = 0;
		StringTokenizer tokenizer = new StringTokenizer(version.trim(), ".");
		for (int i = 0; i < 4; ++i) {
			String sub = "0";
			if (tokenizer.hasMoreTokens()) {
				sub = tokenizer.nextToken();
			}
			if (i == 0) {
				ret = Integer.parseInt(sub);
			} else {
				ret = 256 * ret + Integer.parseInt(sub);
			}
		}
		return ret;
	}

	public static String trimString(String s) {
		if (s == null) {
			return null;
		}
		return s.trim();
	}

	public static boolean isStringNullOrEmpty(String s) {
		if (s == null) {
			return true;
		}
		return s.trim().isEmpty();
	}

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ex) {
		}
	}

	public static void joinThead(Thread thread) {
		try {
			thread.join();
		} catch (InterruptedException ex) {
		}
	}

	public static void joinThead(Thread thread, long millis) {
		try {
			thread.join(millis);
		} catch (InterruptedException ex) {
		}
	}

	public static void joinThead(Thread thread, long millis, int nanos) {
		try {
			thread.join(millis, nanos);
		} catch (InterruptedException ex) {
		}
	}

	public static String byteToString(byte abyte) {
		final char CHARS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		char low = CHARS[abyte & 0x0f];
		char high = CHARS[(abyte >> 4) & 0x0f];
		return "" + high + low;
	}

	//public static String bytesToString(byte[] bytes) {
	//	return bytesToString(bytes, null);
	//}
	public static StringBuilder bytesToString(StringBuilder ret, byte[] bytes, Character sep) {
		if (ret == null) {
			return null;
		}
		if (bytes == null) {
			return ret;
		}
		if (bytes.length <= 0) {
			return ret;
		}
		////
		ZUtil.sbExpandMore(ret, 3 * bytes.length);
		ret.append(byteToString(bytes[0]));
		for (int i = 1; i < bytes.length; ++i) {
			ret.append(sep);
			ret.append(byteToString(bytes[i]));
		}
		return ret;
	}

	public static String intToByteString(int i) {
		return intToByteString(i, null);
	}

	public static String intToByteString(int i, Character sep) {
		byte byte3 = (byte) ((i) & 0xff);
		byte byte2 = (byte) ((i >> 8) & 0xff);
		byte byte1 = (byte) ((i >> 16) & 0xff);
		byte byte0 = (byte) ((i >> 24) & 0xff);
		return "" + byteToString(byte0)
				+ sep + byteToString(byte1)
				+ sep + byteToString(byte2)
				+ sep + byteToString(byte3);
	}

	public static String longToByteString(long l) {
		return longToByteString(l, null);
	}

	public static String longToByteString(long l, Character sep) {
		byte byte7 = (byte) ((l) & 0xffl);
		byte byte6 = (byte) ((l >> 8) & 0xffl);
		byte byte5 = (byte) ((l >> 16) & 0xffl);
		byte byte4 = (byte) ((l >> 24) & 0xffl);
		byte byte3 = (byte) ((l >> 32) & 0xffl);
		byte byte2 = (byte) ((l >> 40) & 0xffl);
		byte byte1 = (byte) ((l >> 48) & 0xffl);
		byte byte0 = (byte) ((l >> 56) & 0xffl);
		return "" + byteToString(byte0)
				+ sep + byteToString(byte1)
				+ sep + byteToString(byte2)
				+ sep + byteToString(byte3)
				+ sep + byteToString(byte4)
				+ sep + byteToString(byte5)
				+ sep + byteToString(byte6)
				+ sep + byteToString(byte7);
	}

	public static String getJVMVersion() {
		return System.getProperty("java.version");
	}

	public static String getJVMExePath() {
		String javaHome = System.getProperty("java.home");
		if (javaHome.endsWith("/")) {
			return javaHome + "bin/java";
		} else {
			return javaHome + "/bin/java";
		}
	}

	public static String getJVMRunOptions() {
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		List<String> jvmArgs = runtimeMXBean.getInputArguments();
		String runOptions = "";
		//String javaHome = System.getProperty("java.home");
		//if (javaHome.endsWith("/")) {
		//	runOptions += javaHome + "bin/java";
		//} else {
		//	runOptions += javaHome + "/bin/java";
		//}
		for (String arg : jvmArgs) {
			if (runOptions.isEmpty()) {
				runOptions += arg;
			} else {
				runOptions += " " + arg;
			}
		}
		String sjc = System.getProperty("sun.java.command");
		String[] subsjc = sjc.split("[ \t]");
		if (subsjc[0].endsWith(".jar")) {
			runOptions += " -jar " + sjc;
		} else {
			runOptions += " -cp " + System.getProperty("java.class.path");
			runOptions += " " + sjc;
		}
		return runOptions;
	}

	public static List<String> getListIps() {
		List<String> ret = new LinkedList<String>();
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddr = enumIpAddr.nextElement();
					if (inetAddr.isSiteLocalAddress()) {
						ret.add(inetAddr.getHostAddress());
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return ret;
	}

	public static String getIpAddr() {
		String ipAddr;
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddr = enumIpAddr.nextElement();
					if (inetAddr.isSiteLocalAddress()) {
						ipAddr = inetAddr.getHostAddress();
						if (!ipAddr.equalsIgnoreCase("localhost") && !ipAddr.startsWith("127")) {
							return ipAddr;
						}
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return "localhost";
	}

	public static StringBuilder fillLine(StringBuilder ret, char ch, int length) {
		if (ret == null) {
			return null;
		}
		if (length <= 0) {
			return ret;
		}
		ZUtil.sbExpandMore(ret, length);
		for (int i = 0; i < length; ++i) {
			ret.append(ch);
		}
		return ret;
	}

	public static <T> StringBuilder toString(StringBuilder ret, Collection<T> collection, String separator) {
		if (ret == null) {
			return null;
		}
		if (collection == null || collection.isEmpty()) {
			return ret;
		}
		if (separator == null) {
			separator = ", ";
		}
		ZUtil.sbExpandMore(ret, 1024);
		Iterator<T> iterator = collection.iterator();
		ret.append(iterator.next());
		while (iterator.hasNext()) {
			ret.append(separator).append(iterator.next());
		}
		return ret;
	}

	public static <K, V> Map<K, V> subMap(Iterator<Map.Entry<K, V>> it, int nelems) {
		Map<K, V> ret = new HashMap<K, V>(nelems);
		Map.Entry<K, V> entry = null;
		for (int j = 0; j < nelems; ++j) {
			if (!it.hasNext()) {
				break;
			}
			entry = it.next();
			ret.put(entry.getKey(), entry.getValue());
		}
		return ret;
	}

	public static <K, V> List<Map<K, V>> splitMap(Map<K, V> map, int melemsAtime) {
		if (map == null) {
			return null;
		}
		//correct melemsAtime
		if (melemsAtime < 1) {
			melemsAtime = 1;
		}
		List<Map<K, V>> ret = new LinkedList<Map<K, V>>();
		int size = map.size();
		if (size <= melemsAtime) {
			ret.add(map);
			return ret;
		}
		Iterator<Map.Entry<K, V>> it = map.entrySet().iterator();
		int nsubMap = size / melemsAtime;
		for (int i = 0; i < nsubMap; ++i) {
			ret.add(subMap(it, melemsAtime));
		}
		int lastMapSz = size % melemsAtime;
		if (lastMapSz > 0) {
			ret.add(subMap(it, lastMapSz));
		}
		return ret;
	}

	public static <T> List<List<T>> splitList(List<T> list, int melemsAtime) {
		if (list == null) {
			return null;
		}
		//correct melemsAtime
		if (melemsAtime < 1) {
			melemsAtime = 1;
		}
		List<List<T>> ret = new LinkedList<List<T>>();
		int size = list.size();
		if (size <= melemsAtime) {
			ret.add(list);
			return ret;
		}
		int nsubList = size / melemsAtime;
		for (int i = 0; i < nsubList; ++i) {
			int startIndex = i * melemsAtime;
			ret.add(list.subList(startIndex, startIndex + melemsAtime));
		}
		int lastListSz = size % melemsAtime;
		if (lastListSz > 0) {
			ret.add(list.subList(size - lastListSz, size));
		}
		return ret;
	}

	public static <K, V> Map<K, V> mergeMap(Map<K, V> out, Map<K, V> dataMap) {
		if (dataMap != null && !dataMap.isEmpty()) {
			if (out == null) {
				out = new HashMap<K, V>();
			}
			out.putAll(dataMap);
		}
		return out;
	}

	public static <T> List<T> mergeList(List<T> out, List<T> dataList) {
		if (dataList != null && !dataList.isEmpty()) {
			if (out == null) {
				out = new ArrayList<T>(dataList.size());
			}
			out.addAll(dataList);
		}
		return out;
	}

	public static <T> List<T> mergeList(List<T> out, List<T>... dataLists) {
		if (dataLists != null && dataLists.length > 0) {
			if (out == null) {
				int sz = 0;
				for (List<T> dataList : dataLists) {
					if (dataList != null) {
						sz += dataList.size();
					}
				}
				if (sz == 0) {
					return null;
				}
				out = new ArrayList<T>(sz);
			}
			for (List<T> dataList : dataLists) {
				if (dataList != null) {
					out.addAll(dataList);
				}
			}
		}
		return out;
	}

	public static Boolean parseBoolean(String strVal) {
		if (strVal != null) {
			strVal = strVal.trim();
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
			}
		}
		return null;
	}

	public static boolean parseBoolean(String strVal, boolean defaultVal) {
		Boolean ret = parseBoolean(strVal);
		if (ret != null) {
			return ret.booleanValue();
		} else {
			return defaultVal;
		}
	}

	/**
	 * Ket qua tra ve co the khong chinh xac, tuy it xay ra
	 *
	 * @param port
	 * @return
	 */
	public static boolean isTcpPortFree(int port) {
		return isTcpPortFree(null, port);
	}

	/**
	 * Ket qua tra ve co the khong chinh xac, tuy it xay ra
	 *
	 * @param host
	 * @param port
	 * @return
	 */
	public static boolean isTcpPortFree(String host, int port) {
		boolean isFree = false;
		ServerSocket ss = null;
		try {
			ss = new ServerSocket();
			if (host == null) {
				ss.bind(new InetSocketAddress(port));
			} else {
				ss.bind(new InetSocketAddress(host, port));
			}
			ss.setReuseAddress(true);
			isFree = true;
		} catch (Exception e) {
			//... do no thing
		} finally {
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					/* should not be thrown */
				}
			}
		}
		return isFree;
	}

	/**
	 * kiem tra va tra ve 1 tcp port chua dung trong khoang [portBeg,portEnd) -
	 * include portBeg, exclude portEnd. Tra ve portEnd neu khong tim thay
	 *
	 * @param host
	 * @param portBeg
	 * @param portEnd
	 * @return
	 */
	public static int getFreeTcpPortInRange(String host, int portBeg, int portEnd) {
		for (int port = portBeg; port < portEnd; ++port) {
			if (isTcpPortFree(host, port)) {
				return port;
			}
		}
		return portEnd;
	}

	public static int getFreeTcpPortInRange(int portBeg, int portEnd) {
		return getFreeTcpPortInRange(null, portBeg, portEnd);
	}

	/**
	 * load resource file which is embeded in jar file
	 *
	 * @param clazz
	 * @param filePath full package path included ex: com/vng/zing/flash.swf
	 * @return byte[] contains content of loaded file
	 */
	public static byte[] loadResourceAsByteArray(String filePath) {
		byte[] ret = null;
		InputStream is = ClassLoader.getSystemResourceAsStream(filePath);
		if (is == null) {
			return null;
		}
		try {
			byte[] tmp = null;
			int count = 0;
			for (;;) {
				int avail = is.available();
				if (avail <= 0) {
					break;
				}
				//int avail = 138092;
				tmp = new byte[avail];
				count = is.read(tmp);
				if (count > 0) {
					if (ret == null && count == avail) {
						ret = tmp;
					} else {
						if (ret != null) {
							byte[] ret_n = new byte[ret.length + count];
							System.arraycopy(ret, 0, ret_n, 0, ret.length);
							System.arraycopy(tmp, 0, ret_n, ret.length, count);
							ret = ret_n;
						} else {
							ret = new byte[count];
							System.arraycopy(tmp, 0, ret, 0, count);
						}
					}
				} else {
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return ret;
	}

	public static String loadResourceAsString(String filePath) {
		InputStream is = ClassLoader.getSystemResourceAsStream(filePath);
		if (is == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder(1024);
		try {
			for (;;) {
				int ch = is.read();
				if (ch < 0) {
					break;
				}
				sb.append((char) ch);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * use reflection to get private field
	 *
	 * @param classInstance
	 * @param fieldName
	 * @return value of the fieldName on classInstance
	 */
	public static Object getInstanceValue(final Object classInstance, final String fieldName) {
		try {
			// Get the private field
			final Field field = classInstance.getClass().getDeclaredField(fieldName);
			boolean isAccessible = field.isAccessible();
			if (!isAccessible) {
				// Allow modification on the field
				field.setAccessible(true);
			}
			// Returns the value of the field on the object
			Object ret = field.get(classInstance);
			if (!isAccessible) {
				field.setAccessible(false);
			}
			return ret;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static boolean isClassLoaded(String fullyQualifiedClassName) {
		java.lang.reflect.Method m = null;
		boolean isAccessible = false;
		try {
			m = ClassLoader.class.getDeclaredMethod("findLoadedClass", new Class[]{String.class});
			isAccessible = m.isAccessible();
			if (!isAccessible) {
				// Allow modification on the field
				m.setAccessible(true);
			}
			ClassLoader cl = ClassLoader.getSystemClassLoader();
			Object loadedClass = m.invoke(cl, fullyQualifiedClassName);
			return loadedClass != null;
		} catch (Exception ex) {
			return false;
		} finally {
			if (m != null && !isAccessible) {
				m.setAccessible(false);
			}
		}
	}

	public static String getFileExtension(String fileName) {
		if (isStringNullOrEmpty(fileName)) {
			return "";
		}
		int beginIndex = fileName.lastIndexOf(".");
		if (beginIndex < 0 || beginIndex == fileName.length() - 1) {
			return "";
		}
		return fileName.substring(beginIndex + 1);
	}

	public static boolean deleteFileOrDir(String path) {
		path = trimString(path);
		if (path == null || path.isEmpty()) {
			return false;
		}
		//
		File f = new File(path);
		return deleteFileOrDir(f);
	}

	public static boolean deleteFileOrDir(File f) {
		try {
			if (f == null || !f.exists()) {
				return false;
			}
			//
			if (f.isFile()) {
				f.delete();
				return true;
			}
			//
			File[] files = f.listFiles();
			if (files == null || files.length == 0) {
				return false;
			}
			//
			for (int i = 0; i < files.length; ++i) {
				if (files[i].isDirectory()) {
					deleteFileOrDir(files[i]);
				} else {
					files[i].delete();
				}
			}
			f.delete();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	////////////////////////////////////////////////////////////////////////////
	///manifest util
	private final static Manifest _Manifest;

	static {
		Manifest mf = null;
		InputStream is = ClassLoader.getSystemResourceAsStream(JarFile.MANIFEST_NAME);
		if (is != null) {
			try {
				mf = new Manifest(is);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		_Manifest = mf;
	}

	public static String readManifestAsString() {
		InputStream is = ClassLoader.getSystemResourceAsStream(JarFile.MANIFEST_NAME);
		if (is == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder(1024);
		try {
			for (;;) {
				int ch = is.read();
				if (ch < 0) {
					break;
				}
				sb.append((char) ch);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static String getManifestClasspath() {
		if (_Manifest == null) {
			return null;
		}
		Attributes attribs = _Manifest.getMainAttributes();
		if (attribs == null) {
			return null;
		}
		String ret = attribs.getValue("Class-Path");
		if (ret == null) {
			return null;
		}
		ret = ret.trim();
		return ret.replace(" ", ":");
	}

	public static String getManifestMainclass() {
		if (_Manifest == null) {
			return null;
		}
		Attributes attribs = _Manifest.getMainAttributes();
		if (attribs == null) {
			return null;
		}
		String ret = attribs.getValue("Main-Class");
		if (ret == null) {
			return null;
		}
		return ret.trim();
	}

	public static boolean isSubclass(Class clsChild, Class clsParent) {
		try {
			clsChild.asSubclass(clsParent);
			return true;
		} catch (ClassCastException ex) {
			return false;
		}
	}
}
