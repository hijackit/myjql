# MyJQL
## A simple SQL query builder for MySQL

A simple tool to write queries programmatically taking advantage of a type safety system.
Once you declare your tables with column types, then you can start writing queries with SQL, but using Java instead of plain text.
This is not an ORM!
The goal is to let you write the SQL that your already know, inside your programs, knowing that the resulting SQL won't be of any surprise.

* [SELECT](#select)
* [INSERT](#insert)
* [UPDATE](#update)
* [DELETE](#delete)
* [MERGE](#merge)
* [Table declarations](#table)

## <a name="select"></a>Select
__A simple select__
```java
SQL.create()
	.selectDistinct(Users.email, Users.password)
	.from(Users)
	.where(Users.username.equalUpper("ROBERT"))
	.toString();
```
translates into:
```sql
SELECT DISTINCT Users.email, Users.password 
FROM Users
WHERE Users.username = UPPER ('ROBERT')
```

__Join tables__
```java
SQL.create()
	.selectDistinct(Users.email, Posts.title)
	.from(Posts)
	.innerJoin(Posts).on(Posts.userFk.equal(Users.pk))
        .where(Users.username.equalUpper("ROBERT21"))
	.toString();
```
translates into:
```sql
SELECT DISTINCT Users.email, Posts.title
FROM Posts
INNER JOIN Posts ON Posts.user_fk = Users.pk
WHERE Users.username = UPPER ('ROBERT21')
```

__Date ranges__
```java
SQL.create()
	.selectDistinct(Users.username)
	.from(Users)
	.where(Users.birthDate.between("20011216", "20011218"))
	.toString();
```
translates into:
```sql
SELECT DISTINCT Users.username
FROM Users
WHERE Users.birth_date >= '20011216'
AND Users.birth_date <= '20011218'
```

Younger than / older than
```java
SQL.create()
	.select(Posts.pk)
	.from(Posts)
	.where(Posts.modified.youngerThan(30))
	.and(Posts.modified.olderThan(15)
	.toString()
```
translates into:
```sql
SELECT Posts.pk
FROM Posts
WHERE TIMESTAMPDIFF (SECOND, Posts.modified, NOW()) < 30
AND TIMESTAMPDIFF (SECOND, Posts.modified, NOW()) > 15
```

__Select count__
```java
SQL.create()
	.selectCount()
	.from(Posts)
	.where(Posts.userFk.equal(34))
	.toString();
```
translates into:
```sql
SELECT COUNT(*) FROM Posts WHERE Posts.user_fk = 34
```

__Substitution variables__
```java
SQL.create()
	.select(Posts.pk)
	.from(Posts)
	.where(Posts.title.equal("?"))
	.toString();
```
translates into:
```sql
SELECT Posts.pk FROM Posts WHERE Posts.title = ?
```

## <a name="insert"></a>Insert

```java
String nullValue = null;
String notNullValue = "something";
        
SQL.create()
    .insertInto(Users)
    .columns(Users.email, Users.password, Users.creationTime, Users.address, Users.notes)
    .values("guesswhat@email.com", "?", DateTimeField::now, Nulls.nullable(nullValue), Nulls.nullable(notNullValue))
    .toString();
```
translates into:
```sql
INSERT INTO Users (email, password, creation_time) 
VALUES ('guesswhat@email.com', ?, now(), NULL, 'something')
```

## <a name="update"></a>Update

```java
SQL.create()
    .update(Users)
    .set(Users.email, "new@email.com")
    .where(Users.email.equal("old@email.com"))
    .toString();
```
translates into:
```sql
UPDATE Users SET Users.email = 'new@email.com' WHERE Users.email = 'old@email.com'
```

__Updates with conditionals__
```java
String newTitle = null;
String newMessage = "New message";
        
String statement = SQL.create()
    .update(Posts)
    .set(Posts.title, newTitle).onlyIf(newTitle != null)
    .set(Posts.message, newMessage).onlyIf(newMessage != null)
    .toString();
```
translates into:
```sql
UPDATE Posts SET Posts.message = 'New message'
```

## <a name="delete"></a>Delete

```java
Select select = SQL.create()
    .select(Users.pk)
    .from(Users)
    .where(Users.email.equal("user@email.com"));

String sql = SQL.create()
    .deleteFrom(Posts)
    .where(Posts.userFk.in(select))
    .toString();
```
translates into:
```sql
DELETE FROM Posts 
WHERE Posts.user_fk IN (
    SELECT Users.pk FROM Users WHERE Users.email = 'user@email.com'
)
```

## <a name="merge"></a>Merge

```java
String upsert = new Merge()
        .mergeInto(Users)
        .on(Users.username.equal("roby456"), Users.email.equal("roby456@email.com"))
        .columns(Users.username, Users.email, Users.password)
        .values("roby456", "roby456@email.com", "newsecret")
        .toString();
```
translates into:
```sql
INSERT INTO Users(username, email, password)
VALUES ('roby456', 'roby456@email.com', 'newsecret')
ON DUPLICATE KEY UPDATE Users.password='newsecret'
```

## <a name="tables"></a>Tables declarations and usage
To use this library you have to define your tables in Java too. 
Nothing fancy here, just create a class that implements the Table interface.
The toString() shall return the name of the table as defined in the schema.
Every column has its own type too.
```java
public class Users implements Table {
    public NumberField pk = new NumberField(this, "pk");
    public TextField username = new TextField(this, "username");
    public TextField password = new TextField(this, "password");
    public DateTimeField creationTime = new DateTimeField(this, "creation_time");

    public String toString() { return "Users"; }
}
```

Supported column types are:
* TextField
* NumberField
* DateField
* TimeField
* DateTimeField

Once you have declared your tables, you can create a Tables class like the following
```java
package hijackit.myjql.schema;

public class Tables {
	public static Users Users = new Users();
	public static Posts Posts = new Posts();
}
```

and using static imports, you can write your queries like SQL:
```java
import static hijackit.myjql.schema.Tables.Users;

String sql = SQL.create()
        .selectDistinct(Users.email, Users.password)
        .from(Users)
        .where(Users.username.equalUpper("ROBY456"))
        .toString();
```

Once you have your SQL statement, you can use it to run the queries with plain JDBC
```java
Connection con = null;
PreparedStatement ps = null;
try {
    con = cp.getConnection();
    String statement = SQL.create()
        .insertInto(Users)
        .columns(Users.email, Users.password, Users.creationTime)
        .values("?", "?", DateTimeField::now)
        .toString();
    ps = con.prepareStatement(sql);
    ps.setString(1, "robert21")
    ps.setString(2, "secret")
    ps.executeUpdate();
} finally {
    close(ps, con);
}
```
