package hijackit.myjql.schema;

import hijackit.myjql.*;

public class Users implements Table {
    public NumberField pk = new NumberField(this, "pk");
    public TextField email = new TextField(this, "email");
    public TextField username = new TextField(this, "username");
    public TextField password = new TextField(this, "password");
    public DateField birthDate = new DateField(this, "birth_date");
    public DateTimeField creationTime = new DateTimeField(this, "creation_time");

    public String toString() { return "Users"; }
}
