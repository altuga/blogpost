package airhacks.ping.boundary;

import airhacks.metrics.boundary.MetricsResourceClient;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class PostsResourceTortureIT {

    private PostsResourceClient client;
    public String title;

    public ExecutorService threadPool;
    private MetricsResourceClient metricClient;

    @BeforeEach
    public void init() {
        System.out.println(" started ");
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
        this.threadPool = Executors.newFixedThreadPool(20);
        initMetric();
    }


    @Test
    public void startTorture() {
        assumeTrue(System.getProperty("torture", null) != null  );
        List<CompletableFuture<Void>> tasks =
                Stream.generate(this::runScenario).
                        limit(500).collect(Collectors.toList());
        tasks.forEach(CompletableFuture::join);
        verifyPerformance();
    }

    void initMetric() {
        URI uri = URI.create("http://localhost:8080/");
        this.metricClient = RestClientBuilder.
                newBuilder().
                baseUri(uri).
                build(MetricsResourceClient.class);
    }


    void verifyPerformance() {
        JsonObject jsonObject = this.metricClient.getApplicationMetrics()
                .getJsonObject("airhacks.blogpad.posts.boundary.PostResource.findPost");

        double oneMinRate = jsonObject.getJsonNumber("oneMinRate;_app=blogpad").doubleValue();
        System.out.println("#### ---- " + oneMinRate);
        assertTrue(oneMinRate > 5 );
    }



    CompletableFuture<Void> runScenario() {
        return CompletableFuture.runAsync(this::findPost, this.threadPool).
                thenRunAsync(this::findNonExistingPost, this.threadPool);
    }

    void findNonExistingPost() {
        try {
            Response response = this.client.findPost("non-existing-post-" + System.nanoTime());
            //204, response.getStatus());
        } catch (WebApplicationException ex) {

        }
    }

    void findPost() {
        Response response = this.client.findPost(this.title);
        JsonObject jsonObject = response.readEntity(JsonObject.class);
        assertNotNull(jsonObject);
    }
}
