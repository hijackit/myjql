package hijackit.myjql;



public class DateTimeField extends Column {

	public DateTimeField(Table table, String name) {
		super(table, name);
	}

	public static String now() {
		return "now()";
	}

	public Condition olderThan(int seconds) {
		return new Condition() {
			@Override
			public String getFor() {
				return "TIMESTAMPDIFF (SECOND, " + DateTimeField.this + ", NOW()) > " + seconds;
			}
			
			public Column getColumn() {
				return DateTimeField.this;
			}
		};
	}

	public Condition youngerThan(int seconds) {
		return new Condition() {
			@Override
			public String getFor() {
				return "TIMESTAMPDIFF (SECOND, " + DateTimeField.this + ", NOW()) < " + seconds;
			}
			
			public Column getColumn() {
				return DateTimeField.this;
			}
		};
	}
}