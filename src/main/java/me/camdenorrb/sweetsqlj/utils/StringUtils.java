package me.camdenorrb.sweetsqlj.utils;

import java.util.List;
import java.util.function.Function;


public final class StringUtils {

	private StringUtils() {}


	public static String build(final char value, final String delimiter, final int amount) {

		final StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < amount; i++) {

			builder.append(value);

			if (i + 1 < amount) {
				builder.append(delimiter);
			}
		}

		return builder.toString();
	}
	
	public static <T> String build(final T[] values, final String delimiter, final Function<T, String> consumer) {

		final StringBuilder builder = new StringBuilder();

		for (int i = 0; i < values.length; i++) {

			builder.append(consumer.apply(values[i]));

			if (i + 1 < values.length) {
				builder.append(delimiter);
			}
		}

		return builder.toString();
	}

	public static <T> String build(final List<T> values, final String delimiter, final Function<T, String> consumer) {

		final StringBuilder builder = new StringBuilder();

		for (int i = 0; i < values.size(); i++) {

			builder.append(consumer.apply(values.get(i)));

			if (i + 1 < values.size()) {
				builder.append(delimiter);
			}
		}

		return builder.toString();
	}

	
}
