package airhacks.service.posts.boundary;

import airhacks.service.posts.control.PostResourceClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author airhacks.com
 */
@Path("post")
public class PostsResource {


    @Inject
    @RestClient
    PostResourceClient postResourceClient;

    @GET
    @Path("{title}")
    @Produces(MediaType.TEXT_HTML)
    public String findPost(@PathParam("title") String title) {
        Response response = postResourceClient.findPost("initial");
        return "<h1>hello</hello> " + title + " " + response.readEntity(JsonObject.class) ;
    }

}
