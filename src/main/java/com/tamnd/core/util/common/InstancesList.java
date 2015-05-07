/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.core.util.common;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author phuong
 * @param <T>
 */
public class InstancesList<T> {

	private final List<WeakReference<T>> _list = new ArrayList<WeakReference<T>>();
	private final List<WeakReference<T>> __rolist = Collections.unmodifiableList(_list);
	private final ReentrantReadWriteLock _rwlock = new ReentrantReadWriteLock();

	public List<WeakReference<T>> getListForReading() {
		_rwlock.readLock().lock();
		return __rolist;
	}

	public void unaccessList() {
		_rwlock.readLock().unlock();
	}

	public int size() {
		_rwlock.readLock().lock();
		try {
			return _list.size();
		} finally {
			_rwlock.readLock().unlock();
		}
	}

	public void add(T inst) {
		boolean isNew = true;
		T instI;
		_rwlock.writeLock().lock();
		try {
			for (int i = 0, size = _list.size(); i < size; i++) {
				instI = _list.get(i).get();
				if (instI == null) {
					_list.remove(i);
					size = _list.size();
					--i;
					continue;
				}
				if (instI == inst) { // same object, not equal to by value
					isNew = false;
				}
			}
			if (isNew) {
				_list.add(new WeakReference<T>(inst));
			}
		} finally {
			_rwlock.writeLock().unlock();
		}
	}

	public void remove(T inst) {
		T instI;
		_rwlock.writeLock().lock();
		try {
			for (int i = 0, size = _list.size(); i < size; i++) {
				instI = _list.get(i).get();
				if (instI == null
						|| instI == inst) { // same object, not equal to by value
					_list.remove(i);
					size = _list.size();
					--i;
				}
			}
		} finally {
			_rwlock.writeLock().unlock();
		}
	}

	public void cleanUp() {
		T instI;
		_rwlock.writeLock().lock();
		try {
			for (int i = 0, size = _list.size(); i < size; i++) {
				instI = _list.get(i).get();
				if (instI == null) {
					_list.remove(i);
					size = _list.size();
					--i;
				}
			}
		} finally {
			_rwlock.writeLock().unlock();
		}
	}
}
