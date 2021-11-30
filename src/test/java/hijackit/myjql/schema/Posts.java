package hijackit.myjql.schema;

import hijackit.myjql.DateTimeField;
import hijackit.myjql.NumberField;
import hijackit.myjql.Table;
import hijackit.myjql.TextField;

public class Posts implements Table {
    public NumberField pk = new NumberField(this, "pk");
    public TextField title = new TextField(this, "title");
    public TextField message = new TextField(this, "message");
    public NumberField likes = new NumberField(this, "likes");
    public NumberField dislikes = new NumberField(this, "dislikes");
    public NumberField userFk = new NumberField(this, "user_fk");
    public DateTimeField modified = new DateTimeField(this, "modified");

    public String toString() { return "Posts"; }
}
