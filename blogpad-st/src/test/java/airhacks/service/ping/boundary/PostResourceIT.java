package airhacks.service.ping.boundary;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    public void save() {

        String title_key = "title";
        String title_value = "GS";

        String content_key = "content" ;
        String content_value  = "I love GS";

        JsonObject post = Json.createObjectBuilder()
                .add(title_key,  title_value)
                .add(content_key, content_value)
                .build();

        Response response = this.client.save(post);
        int status = response.getStatus();
        assertEquals(204, status);
        System.out.println(" -----> " + status);

        response = this.client.findPost(title_value);
        status = response.getStatus();
        assertEquals(200, status);



    }

}