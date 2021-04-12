package airhacks.service.posts.boundary;

import airhacks.service.posts.control.PostResourceClient;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.annotation.RegistryType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;

public class Reactor {

    @Inject
    @RestClient
    PostResourceClient postResourceClient;


    @Inject
    @RegistryType(type =  MetricRegistry.Type.APPLICATION)
    MetricRegistry registery;

    public String render(String title) {
        Response response = this.postResourceClient.findPost(title);
        var status = response.getStatus();
        registery.counter("content_find_post_status_" + status ).inc();
        return "rendered " + response.readEntity(JsonObject.class);
    }
}
