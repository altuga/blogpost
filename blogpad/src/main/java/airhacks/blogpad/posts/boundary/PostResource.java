package airhacks.blogpad.posts.boundary;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import airhacks.blogpad.posts.control.PostStore;
import airhacks.blogpad.posts.entity.Post;
import org.eclipse.microprofile.metrics.annotation.Counted;

import java.net.URI;

@Path("posts")
public class PostResource {

    @Inject
    PostStore store ; 

    @Counted
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(@Context UriInfo info, Post post) {
        Post postWithFileName =  this.store.save(post);

        URI uri = info.getAbsolutePathBuilder().path(postWithFileName.fileName).build();
        //URI uri = URI.create("/" + postWithFileName.fileName);
        return Response.created(uri).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{title}")
    public Post findPost(@PathParam("title") String title) {
        return this.store.read(title);
    }



}
