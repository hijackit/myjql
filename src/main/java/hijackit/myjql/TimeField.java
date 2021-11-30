package hijackit.myjql;

public class TimeField extends Column {

	public TimeField(Table table, String name) {
		super(table, name);
	}

	public Condition equal(final String value) {
		return new Condition() {

			@Override
			public String getFor() {
				return TimeField.this + " = '" + value + "'";
			}
			
			public Column getColumn() {
				return TimeField.this;
			}
		};
	}

	public Condition greaterThan(final String value) {
		return new Condition() {

			@Override
			public String getFor() {
				return TimeField.this + " >= '" + value + "'";
			}
			
			public Column getColumn() {
				return TimeField.this;
			}
		};
	}

	public Condition lesserThan(final String value) {
		return new Condition() {

			@Override
			public String getFor() {
				return TimeField.this + " <= '" + value + "'";
			}
			
			public Column getColumn() {
				return TimeField.this;
			}
		};
	}

	public Condition between(final String from, final String to) {
		return new Condition() {

			@Override
			public String getFor() {
				return TimeField.this + " >= '" + from + "' AND " + TimeField.this + " <= '" + to + "'";
			}
			
			public Column getColumn() {
				return TimeField.this;
			}
		};
	}
}
