package DataContext.Models;

import dev.morphia.annotations.*;
import dev.morphia.utils.IndexType;
import org.bson.types.ObjectId;

@Entity("indexedLinks")
@Indexes(@Index(fields = @Field(value = "$**", type = IndexType.TEXT)))

public class DomLink {
    @Id
    private ObjectId linkId;

    public String parentSite;
    public String Link;
}
