/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.core.util.common;

import java.nio.ByteBuffer;

/**
 *
 * @author namnq
 */
public interface IZStructor<_Type> {

	_Type ctor();

	////////////////////////////////////////////////////////////////////////////
	public static class BooleanStructor implements IZStructor<Boolean> {

		@Override
		public Boolean ctor() {
			return false;
		}
	}

	////////////////////////////////////////////////////////////////////////////
	public static class ByteStructor implements IZStructor<Byte> {

		@Override
		public Byte ctor() {
			return 0;
		}
	}

	////////////////////////////////////////////////////////////////////////////
	public static class ShortStructor implements IZStructor<Short> {

		@Override
		public Short ctor() {
			return 0;
		}
	}

	////////////////////////////////////////////////////////////////////////////
	public static class IntegerStructor implements IZStructor<Integer> {

		@Override
		public Integer ctor() {
			return 0;
		}
	}

	////////////////////////////////////////////////////////////////////////////
	public static class LongStructor implements IZStructor<Long> {

		@Override
		public Long ctor() {
			return 0l;
		}
	}

	////////////////////////////////////////////////////////////////////////////
	public static class FloatStructor implements IZStructor<Float> {

		@Override
		public Float ctor() {
			return 0.0f;
		}
	}

	////////////////////////////////////////////////////////////////////////////
	public static class DoubleStructor implements IZStructor<Double> {

		@Override
		public Double ctor() {
			return 0.0;
		}
	}

	////////////////////////////////////////////////////////////////////////////
	public static class StringStructor implements IZStructor<String> {

		@Override
		public String ctor() {
			return "";
		}
	}

	////////////////////////////////////////////////////////////////////////////
	public static class ByteBufferStructor implements IZStructor<ByteBuffer> {

		@Override
		public ByteBuffer ctor() {
			return ByteBuffer.wrap(new byte[0]);
		}
	}
}
