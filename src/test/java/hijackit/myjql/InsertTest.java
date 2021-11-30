package hijackit.myjql;

import org.junit.Test;

import static hijackit.myjql.schema.Tables.Users;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class InsertTest {

	@Test
	public void insertWithSubstitutionVariables() {
		String statement = SQL.create()
				.insertInto(Users)
				.columns(Users.email, Users.password, Users.creationTime)
				.values("?", "?", "?")
				.toString();

		assertThat(statement, is("INSERT INTO Users (email, password, creation_time) VALUES (?, ?, ?)"));
	}

	@Test
	public void insertWithFixedValues() {
		String statement = SQL.create()
				.insertInto(Users)
				.columns(Users.email, Users.password)
				.values("guesswhat@email.com", "secret")
				.toString();

		assertThat(statement, is("INSERT INTO Users (email, password) VALUES ('guesswhat@email.com', 'secret')"));
	}

	@Test
	public void insertWithFixedValuesAndSubstitutionVariables() {
		String statement = SQL.create()
				.insertInto(Users)
				.columns(Users.email, Users.password, Users.creationTime)
				.values("guesswhat@email.com", "secret", "?")
				.toString();

		assertThat(statement, is("INSERT INTO Users (email, password, creation_time) VALUES ('guesswhat@email.com', 'secret', ?)"));
	}

	@Test
	public void insertWithSuppliers() {
		String statement = SQL.create()
				.insertInto(Users)
				.columns(Users.email, Users.password, Users.creationTime)
				.values(TextField.value("guesswhat@email.com"), Nulls.nullValue(), DateTimeField::now)
				.toString();

		assertThat(statement, is("INSERT INTO Users (email, password, creation_time) VALUES ('guesswhat@email.com', null, now())"));
	}

	@Test
	public void insertWithNullableValues() {
		String nullValue = null;
		String notNullValue = "something";

		String statement = SQL.create()
				.insertInto(Users)
				.columns(Users.email, Users.password)
				.values(Nulls.nullable(nullValue), Nulls.nullable(notNullValue))
				.toString();

		assertThat(statement, is("INSERT INTO Users (email, password) VALUES (null, 'something')"));
	}

}
