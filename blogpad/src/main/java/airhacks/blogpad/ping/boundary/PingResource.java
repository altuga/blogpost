
package airhacks.blogpad.ping.boundary;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 *
 * @author airhacks.com
 */
@Path("ping")
public class PingResource {

    @Inject
    @ConfigProperty(name = "message")
    String message;    

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String ping() {
        return this.message + " MicroProfile 3....***+*!";
    }

}
