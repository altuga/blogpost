package airhacks.blogpad.health.boundary;

import airhacks.blogpad.ping.boundary.PostResourceIT;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author airhacks.com
 */
public class HealthResourceIT {

    private HealthResourceClient client;

    @BeforeEach
    public void initMetricsWithBusinessCall() {
        var test =  new PostResourceIT();
        test.init();
        test.update();
        test.update();
    }

    @BeforeEach
    public void init() {
        System.out.println("started health testing" );
        URI uri = URI.create("http://localhost:8080/");
        this.client = RestClientBuilder.
                newBuilder().
                baseUri(uri).
                build(HealthResourceClient.class);

    }

    @Test
    public void readiness() {
        var response = this.client.readiness();
        assertEquals( 200, response.getStatus() );


    }

    @Test
    public void liveness() {
        var response = this.client.liveness();
        assertEquals( 200, response.getStatus() );


    }



}