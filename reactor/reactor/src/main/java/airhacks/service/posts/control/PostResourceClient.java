package airhacks.service.posts.control;


import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RegisterRestClient(configKey = "content")
@Path("posts")
public interface PostResourceClient {

    @GET
    @Path("{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPost(@PathParam("title") String title) ;

}
