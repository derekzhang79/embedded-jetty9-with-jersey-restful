/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.app.common;

import com.tamnd.core.server.EmbeddedJettyServer;
import com.tamnd.core.util.ZConfig;
import com.tamnd.core.util.ZSystemProp;

/**
 *
 * @author tamnd2
 */
public class Common {

	public static final String PROJECT_NAME;
	public static final int JETTY_PORT;

	static {
		PROJECT_NAME = ZSystemProp.GetAppName();
		JETTY_PORT = ZConfig.Instance.getInt(EmbeddedJettyServer.class, PROJECT_NAME, "port", 8080);
	}
}
