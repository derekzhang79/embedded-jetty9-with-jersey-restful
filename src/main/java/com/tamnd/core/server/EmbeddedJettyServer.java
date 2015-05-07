/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.core.server;

import com.tamnd.core.logger.ZConsoleJettyLogger;
import com.tamnd.core.logger.ZEmptyJettyLogger;
import com.tamnd.core.logger.ZLogger;
import com.tamnd.core.util.ZConfig;
import com.tamnd.core.util.common.InstancesList;
import com.tamnd.core.util.common.ZUtil;
import java.net.BindException;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/**
 *
 * @author namnq
 */
public class EmbeddedJettyServer {

	////////////////////////////////////////////////////////////////////////////
	// for managing servers -->
	public final static InstancesList<EmbeddedJettyServer> Instances = new InstancesList<>();

	////
	protected Class _serverClass = null;
	protected String _info = null;
	protected QueuedThreadPool _threadPool = null;
	// <-- for managing servers
	////////////////////////////////////////////////////////////////////////////

	private static final Logger _Log = ZLogger.getLogger(EmbeddedJettyServer.class);
	protected final String _name;
	protected Server _server;
	protected final Config _config = new Config();
	private final AtomicBoolean _running = new AtomicBoolean(false);
	private Thread _thread = null;

	static {
		int jettyLogChannel = ZConfig.Instance.getInt(EmbeddedJettyServer.class, "static", "jettyLogChannel", 0); // 0: off, 1: console, 2: default

		assert (0 <= jettyLogChannel && jettyLogChannel <= 2);

		if (jettyLogChannel == 0) { // off
			org.eclipse.jetty.util.log.Log.setLog(ZEmptyJettyLogger.instance);
			System.out.println("Turn off the jetty log!");
		} else if (jettyLogChannel == 1) { // console
			org.eclipse.jetty.util.log.Log.setLog(ZConsoleJettyLogger.instance);
			System.out.println("Set the jetty log to console!");
		} // default
	}

	public static class Config {

		public String host = "0.0.0.0";
		public int port = 80;
		public int nconnectors = 1;
		public int nacceptors = 4;
		public int acceptQueueSize = 500;
		public int nminThreads = 100;
		public int nmaxThreads = nminThreads * 2;
		public int maxIdleTime = 5000;
		public int connMaxIdleTime = maxIdleTime;
		public int threadMaxIdleTime = maxIdleTime;

		@Override
		public Config clone() {
			Config ret = new Config();
			ret.host = this.host;
			ret.port = this.port;
			ret.nconnectors = this.nconnectors;
			ret.nacceptors = this.nacceptors;
			ret.acceptQueueSize = this.acceptQueueSize;
			ret.nminThreads = this.nminThreads;
			ret.nmaxThreads = this.nmaxThreads;
			ret.maxIdleTime = this.maxIdleTime;
			ret.connMaxIdleTime = this.connMaxIdleTime;
			ret.threadMaxIdleTime = this.threadMaxIdleTime;
			return ret;
		}

		public boolean isValid() {
			return !host.isEmpty() && port > 0 && nconnectors > 0 && nacceptors > 0 && acceptQueueSize > 0
					&& nminThreads > 0 && nmaxThreads >= nminThreads
					&& connMaxIdleTime > 0 && threadMaxIdleTime > 0;
			//maxIdleTime is just a default value for connMaxIdleTime and threadMaxIdleTime
		}
	}

	protected class ServerRunner implements Runnable {

		private final Server _server;
		private final AtomicBoolean _running;

		public ServerRunner(Server server, AtomicBoolean running) {
			assert (server != null && running != null);
			_server = server;
			_running = running;
		}

		@Override
		public void run() {
			_Log.info("Web server is going to serve");
			try {
				//_server.start();
				_server.join();
			} catch (Exception ex) {
				_Log.error(null, ex);
			}
			_Log.info("Web server stopped");
			_running.set(false);
		}
	}

	public EmbeddedJettyServer(String name) {
		if (ZUtil.isStringNullOrEmpty(name)) {
			_name = "main";
		} else {
			_name = name.trim();
		}
	}

	public final String getName() {
		return _name;
	}

	public final Server getServer() {
		return _server;
	}

	public final Config getConfig() {
		return _config.clone();
	}

	public final boolean isRunning() {
		return _running.get();
	}

	public final Class getServerClass() {
		return _serverClass;
	}

	public final QueuedThreadPool getThreadPool() {
		return _threadPool;
	}

	public final int getNWaitingJob() {
		String info = _threadPool.toString();
		String sWaitingJob = info.substring(info.lastIndexOf(",") + 1, info.lastIndexOf("}"));
		return Integer.parseInt(sWaitingJob);
	}

	public final String getInfo() {
		return _info;
	}

	public boolean setup(Handler handlers) {
		if (_server != null) {
			_Log.warn("Server was already setup, dont need to setup again");
			return true;
		}
		try {
			//===== read config =====
			_config.host = ZConfig.Instance.getString(EmbeddedJettyServer.class, _name, "host", _config.host);
			_config.port = ZConfig.Instance.getInt(EmbeddedJettyServer.class, _name, "port", _config.port);
			_config.nconnectors = ZConfig.Instance.getInt(EmbeddedJettyServer.class, _name, "nconnectors", _config.nconnectors);
			_config.nacceptors = ZConfig.Instance.getInt(EmbeddedJettyServer.class, _name, "nacceptors", _config.nacceptors);
			_config.acceptQueueSize = ZConfig.Instance.getInt(EmbeddedJettyServer.class, _name, "acceptQueueSize", _config.acceptQueueSize);
			_config.nminThreads = ZConfig.Instance.getInt(EmbeddedJettyServer.class, _name, "nminThreads", _config.nminThreads);
			_config.nmaxThreads = ZConfig.Instance.getInt(EmbeddedJettyServer.class, _name, "nmaxThreads", _config.nminThreads * 2);
			_config.maxIdleTime = ZConfig.Instance.getInt(EmbeddedJettyServer.class, _name, "maxIdleTime", _config.maxIdleTime);
			_config.connMaxIdleTime = ZConfig.Instance.getInt(EmbeddedJettyServer.class, _name, "connMaxIdleTime", _config.maxIdleTime);
			_config.threadMaxIdleTime = ZConfig.Instance.getInt(EmbeddedJettyServer.class, _name, "threadMaxIdleTime", _config.maxIdleTime);

			assert (_config.isValid());

			//===== setup threadPool =====
			_threadPool = new QueuedThreadPool();
			_threadPool.setName(this._name);
			_threadPool.setMinThreads(_config.nminThreads);
			_threadPool.setMaxThreads(_config.nmaxThreads);
			_threadPool.setIdleTimeout(_config.threadMaxIdleTime);

			//create server
			Server server = new Server(_threadPool);

			// HTTP connector
			ServerConnector http = new ServerConnector(server);
			http.setHost(_config.host);
			http.setPort(_config.port);
			http.setIdleTimeout(_config.connMaxIdleTime);
			http.setAcceptQueueSize(_config.acceptQueueSize);

			// Set the connector
			server.addConnector(http);

			//Enable parsing of jndi-related parts of web.xml and jetty-env.xml
			org.eclipse.jetty.webapp.Configuration.ClassList classlist = org.eclipse.jetty.webapp.Configuration.ClassList.setServerDefault(server);
			classlist.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration",
					"org.eclipse.jetty.plus.webapp.EnvConfiguration",
					"org.eclipse.jetty.plus.webapp.PlusConfiguration");
			classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
					"org.eclipse.jetty.annotations.AnnotationConfiguration");

			//===== setup server =====
			server.setStopAtShutdown(true);
			if (handlers != null) {
				server.setHandler(handlers);
			}
			//almost done
			_server = server;
			
			//===== add info =====
			_serverClass = _server.getClass();
			_info = "host=" + InetAddress.getByName(_config.host).getHostAddress() + ", port=" + _config.port
					+ ", " + _threadPool.getClass().getName()
					+ "{maxIdleTimeMs=" + formatNum(_threadPool.getIdleTimeout())
					+ ", maxJobQueue=" + formatNum(_threadPool.getQueueSize())
					+ "}, " + http.getClass().getName()
					+ "{acceptQueueSize=" + formatNum(_config.acceptQueueSize)
					+ ", nacceptors=" + formatNum(_config.nacceptors)
					+ ", maxIdleTime=" + formatNum(http.getIdleTimeout())
					+ "}";
			Instances.add(this);
			return true;
		} catch (Exception ex) {
			_Log.error(null, ex);
			return false;
		}
	}

	public static String formatNum(long l) {
		if (-1000 < l && l < 1000) {
			return Long.toString(l);
		}
		// some common case
		if (l == 3600) { // default exp time
			return "3,600";
		}
		if (l == 5000) { // default timeout
			return "5,000";
		}
		if (l == 1000000) { // default queue size, lru size
			return "1,000,000";
		}
		return String.format("%,d", l);
	}

	public boolean start() {
		if (_server == null) {
			return false;
		}
		if (!_running.compareAndSet(false, true)) {
			_Log.warn("Server is already running, dont need to start again");
			return true;
		}
		boolean result = false;
		try {
			_server.start(); //non-blocked here, de o day de neu co loi thi bat duoc exception
			_thread = new Thread(new ServerRunner(_server, _running),
					ZUtil.getClassSimpleName(this.getClass()) + "-" + this._name + "-" + ZUtil.nextGID());
			_thread.start();
			result = true;
		} catch (BindException ex) {
			_Log.error(null, ex);
			stop();
		} catch (Exception ex) {
			_Log.error(null, ex);
			stop();
		}
		return result;
	}

	public void stop() {
		if (_server == null) {
			return;
		}
		if (_running.get()) {
			try {
				_server.stop();
				if (_thread != null) {
					_thread.join();
					_thread = null;
				} else {
					_running.set(false);
				}
			} catch (Exception ex) {
				_Log.error(null, ex);
			}
		}
	}

	public void destroy() {
		if (_server == null) {
			return;
		}
		stop();
		_server.destroy();
		_server = null;
		Instances.remove(this);
	}
}
