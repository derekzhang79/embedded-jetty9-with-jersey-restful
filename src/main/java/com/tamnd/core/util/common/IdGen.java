/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.core.util.common;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author namnq
 */
public class IdGen {

	private final AtomicLong _id;

	public IdGen(long startId) {
		_id = new AtomicLong(startId);
	}

	public IdGen() {
		this(0);
	}

	public long currentId() {
		return _id.get();
	}

	public long nextId() {
		return _id.incrementAndGet();
	}

}
