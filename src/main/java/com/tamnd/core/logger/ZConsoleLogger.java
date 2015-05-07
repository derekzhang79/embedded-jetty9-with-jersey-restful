/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.core.logger;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 *
 * @author namnq
 */
public final class ZConsoleLogger extends org.apache.log4j.Logger implements org.apache.commons.logging.Log {

	public static final ZConsoleLogger instance = new ZConsoleLogger();

	private ZConsoleLogger() {
		super("ZConsoleLogger");
	}

	@Override
	public void addAppender(final org.apache.log4j.Appender newAppender) {
	}

	@Override
	public void assertLog(final boolean assertion, final String msg) {
		if (!assertion) {
			System.err.println("Assertion: " + msg);
		}
	}

	@Override
	public void callAppenders(final org.apache.log4j.spi.LoggingEvent event) {
	}

	void closeNestedAppenders() {
	}

	@Override
	public void debug(final Object message) {
		System.out.println("[DEBUG] " + message);
	}

	@Override
	public void debug(final Object message, final Throwable t) {
		System.out.println("[DEBUG] " + message + ". Caused by " + t);
	}

	@Override
	public void error(final Object message) {
		System.err.println("[ERROR] " + message);
	}

	@Override
	public void error(final Object message, final Throwable t) {
		System.err.println("[ERROR] " + message + ". Caused by " + t);
	}

	@Override
	public void fatal(final Object message) {
		System.err.println("[FATAL] " + message);
	}

	@Override
	public void fatal(final Object message, final Throwable t) {
		System.err.println("[FATAL] " + message + ". Caused by " + t);
	}

	@Override
	public Enumeration getAllAppenders() {
		return new Vector().elements();
	}

	@Override
	public org.apache.log4j.Appender getAppender(final String name) {
		return null;
	}

	@Override
	public org.apache.log4j.Level getEffectiveLevel() {
		return org.apache.log4j.Level.ALL;
	}

	@Override
	public org.apache.log4j.Priority getChainedPriority() {
		return getEffectiveLevel();
	}

	@Override
	public ResourceBundle getResourceBundle() {
		return null;
	}

	@Override
	public void info(final Object message) {
		System.out.println("[INFO] " + message);
	}

	@Override
	public void info(final Object message, final Throwable t) {
		System.out.println("[INFO] " + message + ". Caused by " + t);
	}

	@Override
	public boolean isAttached(org.apache.log4j.Appender appender) {
		return false;
	}

	@Override
	public boolean isDebugEnabled() {
		return true;
	}

	@Override
	public boolean isEnabledFor(final org.apache.log4j.Priority level) {
		return true;
	}

	@Override
	public boolean isInfoEnabled() {
		return true;
	}

	@Override
	public void l7dlog(final org.apache.log4j.Priority priority, final String key, final Throwable t) {
		System.out.println("[" + priority + "] key=\"" + key + "\". Caused by " + t);
	}

	@Override
	public void l7dlog(final org.apache.log4j.Priority priority, final String key, final Object[] params, final Throwable t) {
		System.out.println("[" + priority + "] key=\"" + key + "\", params=" + params + ". Caused by " + t);
	}

	@Override
	public void log(final org.apache.log4j.Priority priority, final Object message, final Throwable t) {
		System.out.println("[" + priority + "] " + message + ". Caused by " + t);
	}

	@Override
	public void log(final org.apache.log4j.Priority priority, final Object message) {
		System.out.println("[" + priority + "] " + message);
	}

	@Override
	public void log(final String callerFQCN, final org.apache.log4j.Priority level, final Object message, final Throwable t) {
		System.out.println("[" + level + "] " + callerFQCN + ": " + message + ". Caused by " + t);
	}

	@Override
	public void removeAllAppenders() {
	}

	@Override
	public void removeAppender(org.apache.log4j.Appender appender) {
	}

	@Override
	public void removeAppender(final String name) {
	}

	@Override
	public void setLevel(final org.apache.log4j.Level level) {
	}

	@Override
	public void setPriority(final org.apache.log4j.Priority priority) {
	}

	@Override
	public void setResourceBundle(final ResourceBundle bundle) {
	}

	@Override
	public void warn(final Object message) {
		System.out.println("[WARN] " + message);
	}

	@Override
	public void warn(final Object message, final Throwable t) {
		System.out.println("[WARN] " + message + ". Caused by " + t);
	}

	@Override
	public void trace(Object message) {
		System.out.println("[TRACE] " + message);
	}

	@Override
	public void trace(Object message, Throwable t) {
		System.out.println("[TRACE] " + message + ". Caused by " + t);
	}

	@Override
	public boolean isTraceEnabled() {
		return true;
	}

	@Override
	public boolean isErrorEnabled() {
		return true;
	}

	@Override
	public boolean isFatalEnabled() {
		return true;
	}

	@Override
	public boolean isWarnEnabled() {
		return true;
	}
}
