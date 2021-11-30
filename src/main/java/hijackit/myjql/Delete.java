package hijackit.myjql;

public class Delete {
	
	private StringBuilder sql = new StringBuilder();

	Delete() {
	}
	
	public Delete deleteFrom(Table table) {
		sql.append("DELETE FROM ").append(table);
		return this;
	}

	public Delete where(Condition condition) {
		sql.append(" WHERE ").append(condition.getFor());
		return this;
	}
	
	public Delete and(Condition condition) {
		sql.append(" AND ").append(condition.getFor());
		return this;
	}
	
	public String toString() {
		return sql.toString();
	}
}
