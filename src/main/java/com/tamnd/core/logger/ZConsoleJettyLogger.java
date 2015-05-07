/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.core.logger;

import org.eclipse.jetty.util.log.Logger;

/**
 *
 * @author namnq
 */
public class ZConsoleJettyLogger implements org.eclipse.jetty.util.log.Logger {

	public static final ZConsoleJettyLogger instance = new ZConsoleJettyLogger("ZConsoleJettyLogger", false);
	private final String _name;
	private boolean _debugEnable;

	public ZConsoleJettyLogger(String name, boolean debugEnable) {
		assert (name != null);
		this._name = name;
		this._debugEnable = debugEnable;
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public void warn(Throwable thrwbl) {
		System.out.println("[WARN] " + thrwbl);
	}

	@Override
	public void warn(final String message, Object... os) {
		System.out.println("[WARN] " + message);
	}

	@Override
	public void warn(final String message, final Throwable t) {
		System.out.println("[WARN] " + message + ". Caused by " + t);
	}

	@Override
	public void info(Throwable thrwbl) {
		System.out.println("[INFO] " + thrwbl);
	}

	@Override
	public void info(final String message, Object... os) {
		System.out.println("[INFO] " + message);
	}

	@Override
	public void info(final String message, final Throwable t) {
		System.out.println("[INFO] " + message + ". Caused by " + t);
	}

	@Override
	public void debug(Throwable thrwbl) {
		if (_debugEnable) {
			System.out.println("[DEBUG] " + thrwbl);
		}
	}

	@Override
	public void debug(final String message, Object... os) {
		if (_debugEnable) {
			System.out.println("[DEBUG] " + message);
		}
	}

	@Override
	public void debug(final String message, final Throwable t) {
		if (_debugEnable) {
			System.out.println("[DEBUG] " + message + ". Caused by " + t);
		}
	}
	////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean isDebugEnabled() {
		return _debugEnable;
	}

	@Override
	public void setDebugEnabled(boolean bln) {
		if (this == ZConsoleJettyLogger.instance) {
			return;//do not allow to change the global instance
		}
		_debugEnable = bln;
	}

	@Override
	public Logger getLogger(String string) {
		return this;
	}

	@Override
	public void ignore(Throwable thrwbl) {
	}

	@Override
	public void debug(String string, long l) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
