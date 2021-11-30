package hijackit.myjql;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Insert {

	private StringBuilder sql = new StringBuilder();

	Insert() {
	}

	public Insert insertInto(Table table) {
		sql.append("INSERT INTO " + table + " ");
		return this;
	}
	
	public String toString() {
		return sql.toString();
	}
	
	public Insert columns(Column ... columns) {
		sql.append("(");
		sql.append(Arrays.stream(columns).map((c) -> c.name).collect(Collectors.joining(", ")));
		sql.append(")");
		return this;
	}
	
	public Insert values(Object... values) {
		sql.append(" VALUES (");
		sql.append(Arrays.stream(values).map((v) -> "?".equals(v) ? "?" : enclose(v)).collect(Collectors.joining(", ")));
		sql.append(")");
		return this;
	}

	public Insert values(Supplier<String>... valueSuppliers) {
		sql.append(" VALUES (");
		sql.append(Arrays.stream(valueSuppliers).map(s -> s.get()).collect(Collectors.joining(", ")));
		sql.append(")");
		return this;
	}

	private String enclose(Object v) {
		if (v == null)
			return "NULL";

		if (v instanceof Select)
			return "(" + v.toString() + ")";
		
		if (v instanceof String)
			return "'" + v + "'";

		if (v instanceof Date)
			return "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(v) + "'";

		return v.toString();
	}
}
