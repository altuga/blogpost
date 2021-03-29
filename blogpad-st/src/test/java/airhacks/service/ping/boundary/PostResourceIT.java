package airhacks.service.ping.boundary;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author airhacks.com
 */
public class PostResourceIT {

    private PostsResourceClient client;

    @BeforeEach
    public void init() {
        System.out.println(" started " );
        URI uri = URI.create("http://localhost:9080/blogpad/resources/");
        this.client = RestClientBuilder.
                newBuilder().
                baseUri(uri).
                build(PostsResourceClient.class);

    }

    @Test
    public void createNew() {

        String title_key = "title";
        String title_value = "Galatasaray-"+System.currentTimeMillis();;

        String content_key = "content" ;
        String content_value  = "I love GS";

        JsonObject post = Json.createObjectBuilder()
                .add(title_key,  title_value)
                .add(content_key, content_value)
                .build();

        Response response = this.client.createNew(post);
        int status = response.getStatus();
        assertEquals(201, status);
        System.out.println(" -----> " + status);

        response = this.client.findPost(title_value);
        status = response.getStatus();
        assertEquals(200, status);

    }

    @Test
    public void update() {

        String title_key = "title";
        String title_value = "Galatasaray";

        String content_key = "content" ;
        String content_value  = "I love GS";

        JsonObject post = Json.createObjectBuilder()
                .add(title_key,  title_value)
                .add(content_key, content_value)
                .build();

        Response response = this.client.update(post);
        assertEquals(200, response.getStatus());


    }


    @Test
    public void findPost() {

        String title_key = "title";
        String title_value = "Galatasaray";

        String content_key = "content" ;
        String content_value  = "I love GS";

        JsonObject post = Json.createObjectBuilder()
                .add(title_key,  title_value)
                .add(content_key, content_value)
                .build();

        Response response = this.client.update(post);
        int status = response.getStatus();
        assertEquals(200, status);
        System.out.println(" -----> " + status);

        response = this.client.findPost(title_value);
        status = response.getStatus();
        assertEquals(200, status);

    }

    @Test
    public void updateTitleWithInvalidFileName() {
        // send IllegalChar
        JsonObject post = Json.createObjectBuilder()
                .add("title",  "hello/world")
                .add("content", "Illegal")
                .build();
        System.out.println(" Post " + post);
        this.client.update(post);

        Response response = this.client.findPost("-");
        int status = response.getStatus();
        JsonObject jsonObject = response.readEntity(JsonObject.class);
        assertEquals(200, status);
        assertEquals("-", jsonObject.getString("title"));
        System.out.println(" jsonObject " + jsonObject);

    }


    @Test
    public void saveWithTooShortTitle() {
        JsonObject post = Json.createObjectBuilder()
                .add("title",  "no")
                .add("content", "Illegal")
                .build();
        System.out.println(" Post " + post);
        try {
            this.client.createNew(post);
        } catch(WebApplicationException ex) {
            var response = ex.getResponse();
            var status = response.getStatus();
            assertEquals(400, status);

        }


    }
}

