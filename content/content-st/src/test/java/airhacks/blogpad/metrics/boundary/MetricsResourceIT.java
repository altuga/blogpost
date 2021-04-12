package airhacks.blogpad.metrics.boundary;

import airhacks.blogpad.ping.boundary.PostResourceIT;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author airhacks.com
 */
public class MetricsResourceIT {

    private MetricsResourceClient client;

    @BeforeEach
    public void initMetricsWithBusinessCall() {
        var test =  new PostResourceIT();
        test.init();
        test.update();
        test.update();
    }

    @BeforeEach
    public void init() {
        System.out.println("started metric testing" );
        URI uri = URI.create("http://localhost:8080/");
        this.client = RestClientBuilder.
                newBuilder().
                baseUri(uri).
                build(MetricsResourceClient.class);

    }

    @Test
    public void getMetrics() {

        var metrics = this.client.getApplicationMetrics();
        assertNotNull(metrics);
        assertFalse(metrics.isEmpty());
        System.out.println(" -----> " + metrics.toString());
        int saveCounter =
                metrics.getJsonNumber("airhacks.blogpad.posts.boundary.PostResource.createNew").intValue();

        System.out.println(" saveCounter -----> " +saveCounter );
        assertTrue(saveCounter >= 0 );


    }




}