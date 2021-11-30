package hijackit.myjql;

import org.junit.Test;

import static hijackit.myjql.schema.Tables.Posts;
import static hijackit.myjql.schema.Tables.Users;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DeleteTest {

	@Test
	public void delete() {
		String sql = SQL.create()
				.deleteFrom(Posts)
				.where(Posts.title.equal("title"))
				.and(Posts.pk.equal("?"))
				.toString();
		
		assertThat(sql, is("DELETE FROM Posts WHERE Posts.title = 'title' AND Posts.pk = ?"));
	}

	@Test
	public void deleteFrom_whereXinY() {
		String sql = SQL.create()
				.deleteFrom(Posts)
				.where(Posts.userFk.in(12, 13))
				.toString();
		
		assertThat(sql, is("DELETE FROM Posts WHERE Posts.user_fk IN (12, 13)"));
	}

	@Test
	public void deleteFrom_whereXinSelect() {
		Select select = SQL.create()
				.select(Users.pk)
				.from(Users)
				.where(Users.email.equal("user@email.com"));

		String sql = SQL.create()
				.deleteFrom(Posts)
				.where(Posts.userFk.in(select))
				.toString();

		assertThat(sql, is("DELETE FROM Posts WHERE Posts.user_fk IN (SELECT Users.pk FROM Users WHERE Users.email = 'user@email.com')"));
	}

}
