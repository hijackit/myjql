package hijackit.myjql;

import java.util.function.Supplier;

public interface Nulls {
	
	static Supplier<String> nullValue() {
		return new Supplier<String>() {
			public String get() {
				return "null";
			}
		};
	}

	/**
	 * null if value is null or empty
	 */
	static Supplier<String> nullable(String value) {
		if (value != null && value.length() > 0)
			return new Supplier<String>() {
				public String get() {
					return "'" + value + "'";
				}
			};

		return new Supplier<String>() {
			public String get() {
				return "null";
			}
		};
	}
}
