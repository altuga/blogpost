package airhacks.blogpad.posts.control;


import airhacks.blogpad.posts.entity.Post;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Startup
@Singleton
public class Initialzer {

    @Inject
    PostStore postStore;

    @PostConstruct
    public void installFirstPost() {
        Post post =     this.createInitialPost();
        this.postStore.createNew(post);
    }

    Post createInitialPost() {
        return new  Post("initial" , "welcome to blogpad");
    }
}
