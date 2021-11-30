package hijackit.myjql;

import java.util.Collection;

public class NumberField extends Column {

	public NumberField(Table table, String name) {
		super(table, name);
	}

	public Condition equal(final String value) {
		return new Condition() {
			
			@Override
			public String getFor() {
				return NumberField.this + " = " + value;
			}
			
			public Column getColumn() {
				return NumberField.this;
			}
		};
	}
	
	public Condition equal(final int value) {
		return new Condition() {
			
			@Override
			public String getFor() {
				return NumberField.this + " = " + value;
			}
			
			public Column getColumn() {
				return NumberField.this;
			}
		};
	}

	public Condition in(final Collection<Integer> values) {
		return in(values.toArray(new Integer[] {}));
	}

	public Condition in(final Integer... values) {
		return new Condition() {
			public String getFor() {
				if (values != null && values.length > 0) {
					String joinedValues = Strings.join(values, ", ");
					return table + "." + name + " IN (" + joinedValues + ")";
				}
				return "";
			}
			
			public Column getColumn() {
				return NumberField.this;
			}
		};
	}

	public Condition in(Select select) {
		return new Condition() {
			public String getFor() {
				return table + "." + name + " IN (" + select.toString() + ")";
			}
			public Column getColumn() {
				return NumberField.this;
			}
		};
	}
}
