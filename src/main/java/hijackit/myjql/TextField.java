package hijackit.myjql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class TextField extends Column {

	public TextField(Table table, String name) {
		super(table, name);
	}

	public Condition equal(final String value) {
		return new Condition() {
			public String getFor() {
				if(value == null) {
					return table + "." + name + " IS NULL";
				}
				
				if ("?".equals(value)) {
					return table + "." + name + " = ?";
				}
				return table + "." + name + " = '" + value + "'";
			}
			public Column getColumn() {
				return TextField.this;
			}
		};
	}
	
	public Condition notEqual(final String value) {
		return new Condition() {
			public String getFor() {
				if(value == null) {
					return table + "." + name + " IS NOT NULL";
				}
				
				if ("?".equals(value)) {
					return table + "." + name + " <> ?";
				}
				return table + "." + name + " <> '" + value + "'";
			}
			public Column getColumn() {
				return TextField.this;
			}
		};
	}

	public Condition equalUpper(final String value) {
		return new Condition() {
			public String getFor() {
				return table + "." + name + " = UPPER ('" + value + "')";
			}
			public Column getColumn() {
				return TextField.this;
			}
		};
	}

	public Condition likeUpper(final String value) {
		return new Condition() {
			public String getFor() {
				return table + "." + name + " LIKE UPPER ('" + value + "')";
			}
			public Column getColumn() {
				return TextField.this;
			}
		};
	}

	public Condition like(final String value) {
		return new Condition() {
			public String getFor() {
				return table + "." + name + " LIKE ('" + value + "')";
			}
			public Column getColumn() {
				return TextField.this;
			}
		};
	}
	
	public Condition in(final Collection<String> values) {
		return in(values.toArray(new String[] {}));
	}
	
	public Condition notIn(final Collection<String> values) {
		return notIn(values.toArray(new String[] {}));
	}
	
	public Condition notIn(final String... values) {
		return new Condition() {
			public String getFor() {
				if (values != null && values.length > 0) {
					String joinedValues = Strings.join(values, "', '");
					return table + "." + name + " NOT IN ('" + joinedValues + "')";
				}
				return table + "." + name + " NOT IN ('')";
			}
			public Column getColumn() {
				return TextField.this;
			}
		};
	}

	public Condition in(final String... values) {
		return new Condition() {
			public String getFor() {
				if (values != null && values.length > 0) {
					String joinedValues = Strings.join(values, "', '");
					return table + "." + name + " IN ('" + joinedValues + "')";
				}
				return "";
			}
			public Column getColumn() {
				return TextField.this;
			}
		};
	}

	public Condition in(Select select) {
		return new Condition() {
			public String getFor() {
				return table + "." + name + " IN (" + select.toString() + ")";
			}
			public Column getColumn() {
				return TextField.this;
			}
		};
	}
	
	public Condition inUpper(final String... values) {
		return new Condition() {
			public String getFor() {
				if (values != null && values.length > 0) {
					List<String> upperValues = new ArrayList<String>();
					for (String value : values) {
						upperValues.add("UPPER ('" + value + "')");
					}

					String joinedValues = Strings.join(upperValues, ", ");
					return table + "." + name + " IN (" + joinedValues + ")";
				}
				return "";
			}
			public Column getColumn() {
				return TextField.this;
			}
		};
	}

	public TextField upper() {
		Table upperTable = new Table() {
			public String toString() {
				return "UPPER (" + TextField.this.table;
			};
		};

		return new TextField(upperTable, this.name + ")");
	}

	public static Supplier<String> value(String value) {
		return () -> "'" + value + "'";
	}

}