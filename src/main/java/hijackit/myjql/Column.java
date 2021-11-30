package hijackit.myjql;


public abstract class Column {
	
	protected String name;
	protected Table table;

	public Column(Table table, String name) {
		this.table = table;
		this.name = name;
	}
	
	public String equal(Column field) {
		return table + "." + name + " = " + field.table + "." + field.name; 
	}

	public Table getParent() {
		return table;
	}
	
	public String toString() {
		return table + "." + name;
	}
	
	public Column decorate(boolean isOracle) {
		return this;
	}
	
	public Select.SortField desc() {
		return new Select.SortField(this, Select.SortField.SortOrder.DESC);
	}

	public Select.SortField asc() {
		return new Select.SortField(this, Select.SortField.SortOrder.ASC);
	}
}
