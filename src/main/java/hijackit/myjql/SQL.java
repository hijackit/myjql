package hijackit.myjql;

public class SQL {


	private SQL() {
	}

	public static SQL create() {
		return new SQL();
	}
	
	public Select select(Column ... columns) {
		Select select = new Select();
		return select.select(columns);
	}

	public Select selectDistinct(Column ... columns) {
		Select select = new Select();
		return select.selectDistinct(columns);
	}
	
	public Select selectCount() {
		Select select = new Select();
		return select.selectCount();
	}
	
	public Insert insertInto(Table table) {
		Insert insert = new Insert();
		return insert.insertInto(table);
	}
	
	public Update update(Table table) {
		Update update = new Update();
		return update.update(table);
	}

	public Merge mergeInto(Table table) {
		Merge merge = new Merge();
		return merge.mergeInto(table);
	}

	public Delete deleteFrom(Table table) {
		Delete delete = new Delete();
		return delete.deleteFrom(table);
	}

}
