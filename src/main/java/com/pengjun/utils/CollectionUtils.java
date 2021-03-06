package com.pengjun.utils;

import java.util.HashMap;

public class CollectionUtils {

	public static <k, v> HashMap<k, v> newHashMap() {
		return new HashMap<k, v>();
	}

	public static class CountIntegerMap extends HashMap<String, Integer> {

		private static final long serialVersionUID = 1L;

		public void count(String key, Integer newValue) {

			Integer srcValue = get(key);
			if (srcValue == null) {
				put(key, newValue);
			} else {
				put(key, srcValue.intValue() + newValue.intValue());
			}

		}
	}

	public static class CountDoubleMap extends HashMap<String, Double> {

		private static final long serialVersionUID = 1L;

		public void count(String key, Double newValue) {

			Double srcValue = get(key);
			if (srcValue == null) {
				put(key, newValue);
			} else {
				put(key, srcValue.doubleValue() + newValue.doubleValue());
			}

		}
	}
}
