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
public class ZEmptyJettyLogger implements org.eclipse.jetty.util.log.Logger {

	public static final ZEmptyJettyLogger instance = new ZEmptyJettyLogger("ZEmptyJettyLogger");
	private final String _name;

	private ZEmptyJettyLogger(String name) {
		assert (name != null);
		this._name = name;
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public void warn(String string, Object... os) {
	}

	@Override
	public void warn(Throwable thrwbl) {
	}

	@Override
	public void warn(String string, Throwable thrwbl) {
	}

	@Override
	public void info(String string, Object... os) {
	}

	@Override
	public void info(Throwable thrwbl) {
	}

	@Override
	public void info(String string, Throwable thrwbl) {
	}

	@Override
	public boolean isDebugEnabled() {
		return false;
	}

	@Override
	public void setDebugEnabled(boolean bln) {
	}

	@Override
	public void debug(String string, Object... os) {
	}

	@Override
	public void debug(Throwable thrwbl) {
	}

	@Override
	public void debug(String string, Throwable thrwbl) {
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
