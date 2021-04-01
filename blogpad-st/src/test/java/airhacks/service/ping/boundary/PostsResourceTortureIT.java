package airhacks.service.ping.boundary;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostsResourceTortureIT {

    private PostsResourceClient client;
    public String title;

    @BeforeEach
    public void init() {
        System.out.println(" started " );
        URI uri = URI.create("http://localhost:8080/blogpad/resources/");
        this.client = RestClientBuilder.
                newBuilder().
                baseUri(uri).
                build(PostsResourceClient.class);

        String title_key = "title";
        this.title = "torture-"+System.currentTimeMillis();

        String content_key = "content" ;
        String content_value  = "torture" +System.currentTimeMillis();

        JsonObject post = Json.createObjectBuilder()
                .add(title_key,  this.title)
                .add(content_key, content_value)
                .build();

        Response response = this.client.createNew(post);
        int status = response.getStatus();
        assertEquals(201, status);

    }

    //TODO
    public JsonObject findPost() {
        Response response = this.client.findPost(this.title);
        return response.readEntity(JsonObject.class);
    }
}
