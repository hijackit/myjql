package hijackit.myjql;

import org.junit.Test;

import static hijackit.myjql.schema.Tables.Posts;
import static hijackit.myjql.schema.Tables.Users;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UpdateTest {
	
	@Test
	public void simpleUpdate() {
		String statement = SQL.create()
				.update(Users)
				.set(Users.email, "sameForAll@email.com")
				.toString();

		assertThat(statement, is("UPDATE Users SET Users.email = 'sameForAll@email.com'"));
	}

	@Test
	public void updateMultipleColumn() {
		String statement = SQL.create()
				.update(Users)
				.set(Users.email, "sameForAll@email.com")
				.set(Users.password, "notSoSecret")
				.toString();

		assertThat(statement, is(
				"UPDATE Users " +
						"SET Users.email = 'sameForAll@email.com', " +
						"Users.password = 'notSoSecret'"));
	}

	@Test
	public void updateWithWhereCondition() {
		String statement = SQL.create()
				.update(Users)
				.set(Users.email, "new@email.com")
				.where(Users.email.equal("old@email.com"))
				.toString();

		assertThat(statement, is(
				"UPDATE Users " +
						"SET Users.email = 'new@email.com' " +
						"WHERE Users.email = 'old@email.com'"));
	}


	@Test
	public void updateWithIncrement() {
		String statement = SQL.create()
				.update(Posts)
				.increment(Posts.likes)
				.toString();
		
		assertThat(statement, is("UPDATE Posts SET likes = likes + 1"));
	}

	@Test
	public void updateWithConditionalIncrement() {
		String statement = SQL.create()
				.update(Posts)
				.increment(Posts.likes).onlyIf(false)
				.increment(Posts.dislikes).onlyIf(true)
				.toString();

		assertThat(statement, is("UPDATE Posts SET dislikes = dislikes + 1"));
	}

	@Test
	public void updateWithMultipleConditionalIncrement() {
		String statement = SQL.create()
				.update(Posts)
				.increment(Posts.likes).onlyIf(true)
				.increment(Posts.dislikes).onlyIf(true)
				.toString();

		assertThat(statement, is("UPDATE Posts SET likes = likes + 1, dislikes = dislikes + 1"));
	}

	@Test
	public void updateWithConditionals() {
		String newTitle = null;
		String newMessage = "New message";

		String statement = SQL.create()
				.update(Posts)
				.set(Posts.title, newTitle).onlyIf(newTitle != null)
				.set(Posts.message, newMessage).onlyIf(newMessage != null)
				.toString();

		assertThat(statement, is("UPDATE Posts SET Posts.message = 'New message'"));
	}


	@Test
	public void updateDateTime() {
		String statement = SQL.create()
				.update(Posts)
				.set(Posts.modified, DateTimeField::now)
				.toString();
		
		assertThat(statement, is("UPDATE Posts SET Posts.modified = now()"));
	}

	@Test
	public void conditionalUpdateDateTime() {
		String oldMessage = "message";
		String newMessage = "message";

		String statement = SQL.create()
				.update(Posts)
				.set(Posts.message, newMessage)
				.set(Posts.modified, DateTimeField::now).onlyIf(!newMessage.equals(oldMessage))
				.toString();

		assertThat(statement, is("UPDATE Posts SET Posts.message = 'message'"));
	}

	@Test
	public void updateDateFieldWIthExplicitValue() {
		String statement = SQL.create()
				.update(Users)
				.set(Users.birthDate, DateField.from("19800101"))
				.where(Users.email.equal("user@email.com"))
				.toString();

		assertThat(statement, is("UPDATE Users SET Users.birth_date = '19800101' WHERE Users.email = 'user@email.com'"));
	}

	@Test
	public void updateDateFieldWIthExplicitValueWithConditional() {
		String statement = SQL.create()
				.update(Users)
				.set(Users.birthDate, DateField.from("19800101")).onlyIf(false)
				.set(Users.password, "secret")
				.where(Users.email.equal("user@email.com"))
				.toString();

		assertThat(statement, is("UPDATE Users SET Users.password = 'secret' WHERE Users.email = 'user@email.com'"));
	}

	@Test
	public void updateWithSubstitutionParam() {
		String statement = SQL.create()
				.update(Users)
				.set(Users.password, "?")
				.where(Users.email.equal("user@email.com"))
				.toString();

		assertThat(statement, is("UPDATE Users SET Users.password = ? WHERE Users.email = 'user@email.com'"));
	}

	@Test
	public void updateWithNullableValues() {
		String statement = SQL.create()
				.update(Posts)
				.set(Posts.title, Nulls.nullable("test"))
				.set(Posts.message, Nulls.nullable(null))
				.where(Posts.pk.equal(12))
				.toString();
		
		assertThat(statement, is("UPDATE Posts SET Posts.title = 'test', Posts.message = null WHERE Posts.pk = 12"));
	}
	
	@Test
	public void testUpdateWithNullValue() {
		String statement = SQL.create()
				.update(Posts)
				.set(Posts.message, Nulls.nullValue())
				.where(Posts.pk.equal(12))
				.toString();
		
		assertThat(statement, is("UPDATE Posts SET Posts.message = null WHERE Posts.pk = 12"));
	}
}
