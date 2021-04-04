package airhacks.metrics.boundary;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("metrics")
public interface MetricsResourceClient {


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    JsonObject getMetrics() ;


    @GET
    @Path("application")
    @Produces(MediaType.APPLICATION_JSON)
    JsonObject getApplicationMetrics() ;
    
}