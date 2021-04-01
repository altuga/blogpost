package airhacks.blogpad.posts.boundary;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;

@org.eclipse.microprofile.health.Readiness
public class Readiness implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up("blogpad");
    }
}
