package airhacks.blogpad.health.boundary;

import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("health")
public interface HealthResourceClient {


    @GET
    @Path("live")
    Response liveness() ;


    @GET
    @Path("ready")
    Response readiness() ;
    
}