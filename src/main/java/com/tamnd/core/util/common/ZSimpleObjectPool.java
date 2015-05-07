/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.core.util.common;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author namnq
 */
public class ZSimpleObjectPool<T> {
////////////////////////////////////////////////////////////////////////////
	// for managing pools -->

	public class Stat {

		protected final AtomicLong nalloc = new AtomicLong();
		protected final AtomicLong nborrow = new AtomicLong();
		protected final AtomicLong nreturn = new AtomicLong();
		protected final AtomicLong ninvalidate = new AtomicLong();

		public Stat() {
		}

		protected void clear() {
			nalloc.set(0);
			nborrow.set(0);
			nreturn.set(0);
			ninvalidate.set(0);
		}

		public void copyTo(Stat o) {
			if (o == null) {
				return;
			}
			o.nalloc.set(this.nalloc.get());
			o.nborrow.set(this.nborrow.get());
			o.nreturn.set(this.nreturn.get());
			o.ninvalidate.set(this.ninvalidate.get());
		}

		public long getNalloc() {
			return nalloc.get();
		}

		public long getNborrow() {
			return nborrow.get();
		}

		public long getNreturn() {
			return nreturn.get();
		}

		public long getNinvalidate() {
			return ninvalidate.get();
		}

		public long getNumIdle() {
			return ZSimpleObjectPool.this.getNumIdle();
		}

	}
	////////////////////////////////////////////////////////////////////////////
	private final static List<ZSimpleObjectPool> _PoolList = new ArrayList<ZSimpleObjectPool>();
	private final static List<ZSimpleObjectPool> __RoPoolList = Collections.unmodifiableList(_PoolList);
	private final static ReentrantReadWriteLock _poolListLock = new ReentrantReadWriteLock();

	public static List<ZSimpleObjectPool> getPoolListForReading() {
		_poolListLock.readLock().lock();
		return __RoPoolList;
	}

	public static void unaccessPoolList() {
		_poolListLock.readLock().unlock();
	}
	////
	protected final String _name;
	protected final Stat _stat = new Stat();
	protected Class _objectClass;
	// <-- for managing pools
	////////////////////////////////////////////////////////////////////////////
	private final ArrayDeque<T> _queue;
	private IZStructor<T> _ctor;
	private IZObjectBuilder<T> _obder;
	private IZDestructor<T> _dtor;

	/**
	 *
	 * @param initCap initialized capacity of internal queue of pool
	 * @param ctor allocator of object, it will be invoked in borrowObject
	 * incase the internal queue is empty
	 * @param obder builder of object, it will be invoked in borrowObject incase
	 * returned object is taken from queue
	 * @param dtor destructor of object, it will be invoked in returnObject
	 * before the returned object is put back into queue
	 */
	public ZSimpleObjectPool(String name, int initCap, IZStructor<T> ctor, IZObjectBuilder<T> obder, IZDestructor<T> dtor) {
		_queue = new ArrayDeque<T>(initCap);
		_ctor = ctor;
		_obder = obder;
		_dtor = dtor;
		////
		addPoolToList();
		_name = name;
	}

	/**
	 *
	 * @param initCap initialized capacity of internal queue of pool
	 * @param ctor allocator of object, it will be invoked in borrowObject
	 * incase the internal queue is empty
	 * @param obder builder of object, it will be invoked in borrowObject incase
	 * returned object is taken from queue
	 */
	public ZSimpleObjectPool(String name, int initCap, IZStructor<T> ctor, IZObjectBuilder<T> obder) {
		_queue = new ArrayDeque<T>(initCap);
		_ctor = ctor;
		_obder = obder;
		////
		addPoolToList();
		_name = name;
	}

	/**
	 *
	 * @param initCap initialized capacity of internal queue of pool
	 * @param ctor allocator of object, it will be invoked in borrowObject
	 * incase the internal queue is empty
	 */
	public ZSimpleObjectPool(String name, int initCap, IZStructor<T> ctor) {
		_queue = new ArrayDeque<T>(initCap);
		_ctor = ctor;
		////
		addPoolToList();
		_name = name;
	}

	/**
	 *
	 * @param ctor allocator of object, it will be invoked in borrowObject
	 * incase the internal queue is empty
	 */
	public ZSimpleObjectPool(String name, IZStructor<T> ctor) {
		_queue = new ArrayDeque<T>();
		_ctor = ctor;
		////
		addPoolToList();
		_name = name;
	}

	protected void addPoolToList() {
		_poolListLock.writeLock().lock();
		_PoolList.add(this);
		_poolListLock.writeLock().unlock();
	}

	public String getName() {
		return _name;
	}

	public Stat getStat() {
		return _stat;
	}

	public Class getObjectClass() {
		return _objectClass;
	}

	public IZStructor<T> getObjectCtor() {
		synchronized (this) {
			return _ctor;
		}
	}

	public void setObjectCtor(IZStructor<T> ctor) {
		synchronized (this) {
			this._ctor = ctor;
		}
	}

	public IZObjectBuilder<T> getObjectBuilder() {
		synchronized (this) {
			return _obder;
		}
	}

	public void setObjectBuilder(IZObjectBuilder<T> obder) {
		synchronized (this) {
			this._obder = obder;
		}
	}

	public IZDestructor<T> getObjectDtor() {
		synchronized (this) {
			return _dtor;
		}
	}

	public void setObjectDtor(IZDestructor<T> dtor) {
		synchronized (this) {
			this._dtor = dtor;
		}
	}

	public int getNumIdle() {
		synchronized (this) {
			return _queue.size();
		}
	}

	public void clear() {
		synchronized (this) {
			_queue.clear();
		}
	}

	public boolean isEmpty() {
		synchronized (this) {
			return _queue.isEmpty();
		}
	}

	public T borrowObject(IZMatcher<T> matcher, boolean autoCtor) {
		T ret = null;
		IZStructor<T> ctor;
		IZObjectBuilder<T> obder;
		boolean ctored = false;
		synchronized (this) {
			Iterator<T> it = _queue.iterator();
			while (it.hasNext()) {
				T t = it.next();
				if (matcher.match(t)) {
					//return this result
					ret = t;
					it.remove();
					break;
				}
			}
			////
			obder = this._obder;
			ctor = this._ctor;
		}
		if (ret != null) {
			if (obder != null) {
				ret = obder.build(ret);
			}
		} else if (ctor != null && autoCtor) {
			ret = ctor.ctor();
			ctored = true;
		}
		////
		if (ret != null) {
			if (ctored) {
				_stat.nalloc.incrementAndGet();
			}
			_stat.nborrow.incrementAndGet();
			if (_objectClass == null) {
				synchronized (this) {
					_objectClass = ret.getClass();
				}
			}
		}
		return ret;
	}

	public T borrowObject() {
		T ret;
		IZStructor<T> ctor;
		IZObjectBuilder<T> obder;
		boolean ctored = false;
		synchronized (this) {
			ret = _queue.pollLast();
			obder = this._obder;
			ctor = this._ctor;
		}
		if (ret != null) {
			if (obder != null) {
				ret = obder.build(ret);
			}
		} else if (ctor != null) {
			ret = ctor.ctor();
			ctored = true;
		}
		////
		if (ret != null) {
			if (ctored) {
				_stat.nalloc.incrementAndGet();
			}
			_stat.nborrow.incrementAndGet();
			if (_objectClass == null) {
				synchronized (this) {
					_objectClass = ret.getClass();
				}
			}
		}
		return ret;
	}

	public void returnObject(T t) {
		if (t == null) {
			return;
		}
		////
		_stat.nreturn.incrementAndGet();
		IZDestructor<T> dtor = _dtor;
		if (dtor != null) {
			t = dtor.dtor(t);
			if (t == null) {
				return;
			}
		}
		synchronized (this) {
			_queue.addFirst(t);
		}
	}

}
