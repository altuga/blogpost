package airhacks.blogpad.posts.control;


import airhacks.blogpad.posts.entity.Post;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@Startup
@Singleton
public class Initialzer {

    public static final String TITLE = "initial";

    @Inject
    PostStore postStore;

    @PostConstruct
    public void installFirstPost() {
        if(postExists()) {
            return;
        }
        Post post =    this.createInitialPost();
        this.postStore.createNew(post);
    }

    Post createInitialPost() {
        return new  Post(TITLE, "welcome to blogpad");
    }


    boolean postExists() {
        var post = this.fetchPost();
        if (post == null) {
            return false;
        }
        return TITLE.equalsIgnoreCase(post.title);
    }

    Post fetchPost() {
        return this.postStore.read(TITLE);
    }

    @Produces
    @Liveness
    public HealthCheck initialExisits() {
        return () -> HealthCheckResponse.named("initial-post-exists").state(this.postExists()).build();
    }


}
