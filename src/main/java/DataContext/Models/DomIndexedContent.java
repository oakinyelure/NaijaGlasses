package DataContext.Models;
import dev.morphia.annotations.*;
import org.bson.types.ObjectId;

import java.util.HashSet;

@Entity("indexedContents")
@Indexes(
        @Index(value = "url", fields = @Field("url"))
)
public class DomIndexedContent {
    @Id
    private ObjectId id;

    public DomIndexedContent() {
        this.links = new HashSet<>();
        this.images = new HashSet<>();
        this.contents = new HashSet<>();
    }

    public HashSet<String> images;

    public HashSet<String> links;

    public HashSet<String> contents;

    public String url;

}
