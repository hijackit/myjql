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

## <a name="update"></a>Update

## <a name="delete"></a>Delete

## <a name="merge"></a>Merge