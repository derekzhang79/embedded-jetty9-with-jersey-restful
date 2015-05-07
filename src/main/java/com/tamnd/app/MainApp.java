/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.app;

/**
 *
 * @author tamnd2
 */
public class MainApp {

	public static final void main(String[] args) throws Exception {
		JettyRunner runner = new JettyRunner();
		if (!runner.setupAndStart()) {
			System.err.println("Could not start http servers! Exit now.");
			System.exit(1);
		}
	}
}