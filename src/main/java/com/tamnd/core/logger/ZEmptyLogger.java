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
public final class ZEmptyLogger extends org.apache.log4j.Logger implements org.apache.commons.logging.Log {

	public static final ZEmptyLogger instance = new ZEmptyLogger();

	private ZEmptyLogger() {
		super("ZEmptyLogger");
	}

	@Override
	public void addAppender(final org.apache.log4j.Appender newAppender) {
	}

	@Override
	public void assertLog(final boolean assertion, final String msg) {
	}

	@Override
	public void callAppenders(final org.apache.log4j.spi.LoggingEvent event) {
	}

	void closeNestedAppenders() {
	}

	@Override
	public void debug(final Object message) {
	}

	@Override
	public void debug(final Object message, final Throwable t) {
	}

	@Override
	public void error(final Object message) {
	}

	@Override
	public void error(final Object message, final Throwable t) {
	}

	@Override
	public void fatal(final Object message) {
	}

	@Override
	public void fatal(final Object message, final Throwable t) {
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
		return org.apache.log4j.Level.OFF;
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
	}

	@Override
	public void info(final Object message, final Throwable t) {
	}

	@Override
	public boolean isAttached(org.apache.log4j.Appender appender) {
		return false;
	}

	@Override
	public boolean isDebugEnabled() {
		return false;
	}

	@Override
	public boolean isEnabledFor(final org.apache.log4j.Priority level) {
		return false;
	}

	@Override
	public boolean isInfoEnabled() {
		return false;
	}

	@Override
	public void l7dlog(final org.apache.log4j.Priority priority, final String key, final Throwable t) {
	}

	@Override
	public void l7dlog(final org.apache.log4j.Priority priority, final String key, final Object[] params, final Throwable t) {
	}

	@Override
	public void log(final org.apache.log4j.Priority priority, final Object message, final Throwable t) {
	}

	@Override
	public void log(final org.apache.log4j.Priority priority, final Object message) {
	}

	@Override
	public void log(final String callerFQCN, final org.apache.log4j.Priority level, final Object message, final Throwable t) {
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
	}

	@Override
	public void warn(final Object message, final Throwable t) {
	}

	@Override
	public void trace(Object message) {
	}

	@Override
	public void trace(Object message, Throwable t) {
	}

	@Override
	public boolean isTraceEnabled() {
		return false;
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
