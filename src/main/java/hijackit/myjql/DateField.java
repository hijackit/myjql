package hijackit.myjql;

import java.util.function.Supplier;

public class DateField extends Column {

	public DateField(Table table, String name) {
		super(table, name);
	}

	public Condition equal(final String value) {
		return new Condition() {
			
			public String getFor() {
				return DateField.this + " = '" + value + "'";
			}
			
			public Column getColumn() {
				return DateField.this;
			}
		};
	}

	public Condition greaterThan(final String value) {
		return new Condition() {

			public String getFor() {
				return DateField.this + " >= '" + value + "'";
			}
			
			public Column getColumn() {
				return DateField.this;
			}
		};
	}

	public Condition lesserThan(final String value) {
		return new Condition() {

			public String getFor() {
				return DateField.this + " <= '" + value + "'";
			}
			
			public Column getColumn() {
				return DateField.this;
			}
		};
	}

	public Condition between(final String from, final String to) {
		return new Condition() {

			public String getFor() {
				return DateField.this + " >= '" + from + "' AND " + DateField.this + " <= '" + to + "'";
			}
			
			public Column getColumn() {
				return DateField.this;
			}
		};
	}
	
	public static Supplier<String> from(String value) {
		return new Supplier<String>() {
			@Override
			public String get() {
				return "'" + value+ "'";
			}
		};
	}
}