package airhacks.blogpad.posts.boundary;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import airhacks.blogpad.posts.control.PostStore;
import airhacks.blogpad.posts.entity.Post;
import org.eclipse.microprofile.metrics.annotation.Counted;

@Path("posts")
public class PostResource {

    @Inject
    PostStore store ; 

    @Counted
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void save(Post post) {
        this.store.save(post);
     
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{title}")
    public Post findPost(@PathParam("title") String title) {
        return this.store.read(title);
    }



}
