package hijackit.myjql;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Merge {

	private Table table;
	private Condition[] keyConditions;
	
	private Column[] columns;
	private Object[] values;

	Merge() {
	}

	public String toString() {
		return mysql();
	}

	public Merge mergeInto(Table table) {
		this.table = table;
		return this;
	}

	public Merge on(Condition ... conditions) {
		this.keyConditions = conditions;
		return this;
	}
	
	public Merge columns(Column... columns) {
		this.columns = columns;
		return this;
	}

	public Merge values(Object... values) {
		this.values = values;
		return this;
	}
	
	private String mysql() {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO " + table);

		sql.append("(");
		sql.append(Arrays.stream(columns).map((c) -> c.name).collect(Collectors.joining(", ")));
		sql.append(")");

		sql.append(" VALUES (");
		sql.append(Arrays.stream(values).map((v) -> "?".equals(v) ? "?" : enclose(v)).collect(Collectors.joining(", ")));
		sql.append(")");

		sql.append(" ON DUPLICATE KEY UPDATE ");

		for (int i = 0; i < columns.length; i++) {
			if (isKeyColumn(columns[i]))
				continue;
			sql.append(columns[i] + "='" + values[i] + "'");
			sql.append(", ");
		}
		sql.delete(sql.length() - 2, sql.length());
		return sql.toString();
	}
	
	private String enclose(Object v) {
		if (v instanceof String)
			return "'" + v + "'";
		
		return v.toString();
	}

	private boolean isKeyColumn(Column column) {
		for(Condition condition : keyConditions) {
			if(column.equals(condition.getColumn()))
				return true;
		}
		return false;
	}
}
