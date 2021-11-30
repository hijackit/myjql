package hijackit.myjql;

import java.util.function.Supplier;

public class Update {

	private StringBuilder sql = new StringBuilder();
	private int lastIncrement;
	private int setCount = 0;

	Update() {
	}

	public String toString() {
		return sql.toString();
	}

	public Update update(Table table) {
		sql.append("UPDATE ");
		sql.append(table.toString());
		sql.append(" SET ");
		return this;
	}

	public Update set(Column column, Supplier<String> f) {	
		int previousLength = sql.length();
		if (setCount > 0)
			sql.append(", ");
		sql.append(column + " = " + f.get());
		setCount++;
		lastIncrement = sql.length() - previousLength;
		return this;
	}
	
	public Update set(Column column, String value) {
		int previousLength = sql.length();
		if (setCount > 0)
			sql.append(", ");
		
		if(value == null) 
			sql.append(column + " = NULL");
		else if("?".equals(value))
			sql.append(column + " = ?");
		else
			sql.append(column + " = '" + value + "'");
		
		setCount++;
		lastIncrement = sql.length() - previousLength;
		return this;
	}

	public Update set(NumberField column, Integer value) {
		int previousLength = sql.length();
		if (setCount > 0)
			sql.append(", ");
		
		sql.append(column + " = " + value);
		
		setCount++;
		lastIncrement = sql.length() - previousLength;
		return this;
	}
	
	public Update increment(NumberField column) {
		int previousLength = sql.length();
		if (setCount > 0)
			sql.append(", ");
		sql.append(column.name);
		sql.append(" = ");
		sql.append(column.name + " + 1");
		setCount++;
		lastIncrement = sql.length() - previousLength;
		return this;
	}

	public Update decrement(NumberField column, int amount) {
		int previousLength = sql.length();
		if (setCount > 0)
			sql.append(", ");
		sql.append(column.name);
		sql.append(" = ");
		sql.append(column.name + " - " + amount);
		setCount++;
		lastIncrement = sql.length() - previousLength;
		return this;
	}
	
	public Update onlyIf(boolean condition) {
		int start = sql.length() - lastIncrement;
		if (!condition){
			sql.delete(start, sql.length());
			setCount--;			
		}
		return this;
	}

	public Update where(Condition condition) {
		sql.append(" WHERE ").append(condition.getFor());
		return this;
	}

	public Update where(String condition) {
		sql.append(" WHERE ").append(condition);
		return this;
	}
	
	public Update and(Condition condition) {
		sql.append(" AND ").append(condition.getFor());
		return this;
	}
	
	public Update and(String condition) {
		sql.append(" AND ").append(condition);
		return this;
	}
	
	public Update or(Condition condition) {
		sql.append(" OR ").append(condition.getFor());
		return this;
	}

	public Update or(String condition) {
		sql.append(" OR ").append(condition);
		return this;
	}
	
}
