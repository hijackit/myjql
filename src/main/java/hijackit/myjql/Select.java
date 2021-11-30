package hijackit.myjql;


public class Select {

	private StringBuilder sql = new StringBuilder();
	private int lastIncrement;

	Select() {
	}

	public Select and(Condition condition) {
		int previousLength = sql.length();
		sql.append(" AND ").append(condition.getFor());
		lastIncrement = sql.length() - previousLength;
		return this;
	}

	public Select andUpper(TextField column) {
		sql.append(" AND UPPER (").append(column).append(")");
		return this;
	}
	
	public Select where(Condition condition) {
		int previousLength = sql.length();
		sql.append(" WHERE ").append(condition.getFor());
		lastIncrement = sql.length() - previousLength;
		return this;
	}

	public Select where(String condition) {
		int previousLength = sql.length();
		sql.append(" WHERE ").append(condition);
		lastIncrement = sql.length() - previousLength;
		return this;
	}

	public String toString() {
		return sql.toString();
	}
	
	public Select selectDistinct(Column... columns) {
		sql.append("SELECT DISTINCT ");
		sql.append(Strings.join(columns, ", "));
		return this;
	}

	public Select select(Column... columns) {
		sql.append("SELECT ");
		sql.append(Strings.join(columns, ", "));
		return this;
	}
	
	public Select selectCount() {
		sql.append("SELECT COUNT(*)");
		return this;
	}
	
	public Select from(Table table) {
		sql.append(" FROM ").append(table);
		return this;
	}
	
	public InnerJoin innerJoin(Table table) {
		return new InnerJoin(this, table);
	}
	
	public LeftJoin leftJoin(Table table) {
		return new LeftJoin(this, table);
	}
	
	public Select orderBy(SortField ... sortFields) {
		String columns = Strings.join(sortFields, ", ");
		sql.append(" ORDER BY ").append(columns);
		return this;
	}

	/**
	 * If condition is true, undo the last issued command (and, join)
	 */
	public Select onlyIf(boolean condition) {
		int start = sql.length() - lastIncrement;
		if (!condition)
			sql.delete(start, sql.length());
		return this;
	}

	public static class InnerJoin {
		private Select sqlStringBuilder;
		private Table table;

		public InnerJoin(Select sqlStringBuilder, Table table) {
			this.sqlStringBuilder = sqlStringBuilder;
			this.table = table;
		}

		public Select on(String condition) {
			int previousLength = sqlStringBuilder.sql.length();
			sqlStringBuilder.sql.append(" INNER JOIN ").append(table);
			sqlStringBuilder.sql.append(" ON ").append(condition);

			sqlStringBuilder.lastIncrement = sqlStringBuilder.sql.length() - previousLength;
			return sqlStringBuilder;
		}
	}
	
	public static class LeftJoin {
		private Select sqlStringBuilder;
		private Table table;

		public LeftJoin(Select sqlStringBuilder, Table table) {
			this.sqlStringBuilder = sqlStringBuilder;
			this.table = table;
		}

		public Select on(String condition) {
			int previousLength = sqlStringBuilder.sql.length();
			sqlStringBuilder.sql.append(" LEFT JOIN ").append(table);
			sqlStringBuilder.sql.append(" ON ").append(condition);

			sqlStringBuilder.lastIncrement = sqlStringBuilder.sql.length() - previousLength;
			return sqlStringBuilder;
		}
	}
	
	public static class SortField {
		private Column column;
		private SortOrder order;

		public SortField(Column column, SortOrder order) {
			this.column = column;
			this.order = order;
		}
		
		public enum SortOrder {
			DESC, ASC,
		}
		
		public String toString() {
			return column.toString() + " " + order;
		}
	}
}
