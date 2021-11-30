package hijackit.myjql;

import org.junit.Test;

import static hijackit.myjql.schema.Tables.Users;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MergeTest {

	@Test
	public void upsert() {
		String upsert = new Merge()
				.mergeInto(Users)
				.on(Users.username.equal("roby456"))
				.columns(Users.username, Users.password)
				.values("roby456", "newsecret")
				.toString();

		assertThat(upsert, is("INSERT INTO Users(username, password) "
				+ "VALUES ('roby456', 'newsecret') "
				+ "ON DUPLICATE KEY UPDATE Users.password='newsecret'"));
	}

	@Test
	public void upsertMultipleColumns() {
		String upsert = new Merge()
				.mergeInto(Users)
				.on(Users.username.equal("roby456"))
				.columns(Users.username, Users.email, Users.password)
				.values("roby456", "roby456@email.com", "newsecret")
				.toString();

		assertThat(upsert, is("INSERT INTO Users(username, email, password) "
				+ "VALUES ('roby456', 'roby456@email.com', 'newsecret') "
				+ "ON DUPLICATE KEY UPDATE Users.email='roby456@email.com', Users.password='newsecret'"));
	}

	@Test
	public void upsertCompositePk() {
		String upsert = new Merge()
				.mergeInto(Users)
				.on(Users.username.equal("roby456"), Users.email.equal("roby456@email.com"))
				.columns(Users.username, Users.email, Users.password)
				.values("roby456", "roby456@email.com", "newsecret")
				.toString();

		System.out.println(upsert);

		assertThat(upsert, is("INSERT INTO Users(username, email, password) "
				+ "VALUES ('roby456', 'roby456@email.com', 'newsecret') "
				+ "ON DUPLICATE KEY UPDATE Users.password='newsecret'"));
	}

}
