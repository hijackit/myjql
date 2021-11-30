package hijackit.myjql;

import org.junit.Test;

import static hijackit.myjql.schema.Tables.Posts;
import static hijackit.myjql.schema.Tables.Users;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SelectTest {

	@Test
	public void simpleSelect() {
		String sql = SQL.create()
				.selectDistinct(Users.email, Users.password)
				.from(Users)
				.where(Users.username.equalUpper("ROBY456"))
				.toString();
		
		assertThat(sql, is("SELECT DISTINCT Users.email, Users.password " +
				"FROM Users " +
				"WHERE Users.username = UPPER ('ROBY456')"));
	}

	@Test
	public void selectWithInnerJoin() {
		String sql = SQL.create()
				.selectDistinct(Users.email, Posts.title)
				.from(Posts)
				.innerJoin(Posts).on(Posts.userFk.equal(Users.pk))
				.where(Users.username.equalUpper("ROBY456"))
				.toString();
		
		assertThat(sql, is("SELECT DISTINCT Users.email, Posts.title " +
				"FROM Posts " +
				"INNER JOIN Posts ON Posts.user_fk = Users.pk " +
				"WHERE Users.username = UPPER ('ROBY456')"));
	}

	@Test
	public void selectWithLikeUpperCondition() {
		String sql = SQL.create()
				.selectDistinct(Users.username)
				.from(Users)
				.where(Users.username.likeUpper("%OBY%"))
				.and(Users.email.like("%email.com%"))
				.toString();
		
		assertThat(sql, is("SELECT DISTINCT Users.username " +
				"FROM Users " +
				"WHERE Users.username LIKE UPPER ('%OBY%') " +
				"AND Users.email LIKE ('%email.com%')"));
	}

	@Test
	public void selectWithDateRange() {
		String sql = SQL.create()
				.selectDistinct(Users.username)
				.from(Users)
				.where(Users.birthDate.between("20011216", "20011218"))
				.toString();
		
		assertThat(sql, is("SELECT DISTINCT Users.username " +
				"FROM Users " +
				"WHERE Users.birth_date >= '20011216' " +
				"AND Users.birth_date <= '20011218'"));
	}

	@Test
	public void selectWithExactTime() {
		String sql = SQL.create()
				.selectDistinct(Users.username)
				.from(Users)
				.where(Users.birthDate.equal("20011216"))
				.toString();

		assertThat(sql, is("SELECT DISTINCT Users.username " +
				"FROM Users " +
				"WHERE Users.birth_date = '20011216'"));
	}
	
	@Test
	public void selectWhereNotNull() {
		String statement = SQL.create()
				.select(Users.username)
				.from(Users)
				.where(Users.username.notEqual(null))
				.toString();
		
		assertThat(statement, is("SELECT Users.username " +
				"FROM Users " +
				"WHERE Users.username IS NOT NULL"));
	}
	
	@Test
	public void selectWhereExactText() {
		String statement = SQL.create()
				.select(Posts.pk)
				.from(Posts)
				.where(Posts.title.equal("the one"))
				.toString();
		
		assertThat(statement, is ("SELECT Posts.pk FROM Posts WHERE Posts.title = 'the one'"));
	}

	@Test
	public void selectWhereTextNotEqualTo() {
		String statement = SQL.create()
				.select(Posts.pk)
				.from(Posts)
				.where(Posts.title.notEqual("the one"))
				.toString();

		assertThat(statement, is ("SELECT Posts.pk FROM Posts WHERE Posts.title <> 'the one'"));
	}

	@Test
	public void selectWhereTextNotEqualToSubstitutionVariable() {
		String statement = SQL.create()
				.select(Posts.pk)
				.from(Posts)
				.where(Posts.title.notEqual("?"))
				.toString();
		
		assertThat(statement, is ("SELECT Posts.pk FROM Posts WHERE Posts.title <> ?"));
	}

	@Test
	public void selectWithSubtitutionVariable() {
		String statement = SQL.create()
				.select(Posts.pk)
				.from(Posts)
				.where(Posts.title.equal("?"))
				.toString();
		
		assertThat(statement, is("SELECT Posts.pk FROM Posts WHERE Posts.title = ?"));
	}
	
	@Test
	public void selectOlderThan() {
		String statement = SQL.create()
				.select(Posts.pk)
				.from(Posts)
				.where(Posts.modified.olderThan(30))
				.toString();
		
		assertThat(statement, is("SELECT Posts.pk " +
				"FROM Posts " +
				"WHERE TIMESTAMPDIFF (SECOND, Posts.modified, NOW()) > 30"));
	}

	@Test
	public void selectYoungerThan() {
		String statement = SQL.create()
				.select(Posts.pk)
				.from(Posts)
				.where(Posts.modified.youngerThan(30))
				.toString();

		assertThat(statement, is("SELECT Posts.pk " +
				"FROM Posts " +
				"WHERE TIMESTAMPDIFF (SECOND, Posts.modified, NOW()) < 30"));
	}

	
	@Test
	public void insertWithSelect() {
		Select select = SQL.create()
				.select(Users.pk)
				.from(Users)
				.where(Users.username.equalUpper("ROBY456"));
		
		String sql = SQL.create()
				.insertInto(Posts)
				.columns(Posts.title, Posts.message, Posts.userFk)
				.values("New post", "?", select)
				.toString();
		
		assertThat(sql, is("INSERT INTO Posts (title, message, user_fk) " +
				"VALUES ('New post', ?, (SELECT Users.pk FROM Users WHERE Users.username = UPPER ('ROBY456')))"));
	}
	
	@Test
	public void selectCount() {
		String select = SQL.create()
			.selectCount()
			.from(Posts)
			.where(Posts.userFk.equal(34))
			.toString();
		
		assertThat(select, is("SELECT COUNT(*) FROM Posts WHERE Posts.user_fk = 34"));
	}
}
