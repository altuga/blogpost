package airhacks.blogpad.posts.control;

import airhacks.blogpad.posts.entity.Post;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.metrics.annotation.RegistryType;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class PostStore {


    @Inject
    @ConfigProperty(name="root.storage.dir")
    String storageDir;
    private Path storagePath = null;

    @Inject
    TitleNormalizer titleNormalizer;

    @Inject
    @ConfigProperty(name="minimum.storage.space", defaultValue = "50")
    private long storageThreshold;

    @PostConstruct
    public void init() {
        this.storagePath = Path.of(this.storageDir);
    }

    @Inject
    @RegistryType(type = MetricRegistry.Type.APPLICATION)
    MetricRegistry metricRegistry;

    @Produces
    @Liveness
    public HealthCheck checkPostsDirectoryExists() {
        return () ->
                 HealthCheckResponse.named("post-directory-exists")
                .state(Files.exists(this.storagePath)).build();

    }

    @Produces
    @Liveness
    public HealthCheck checkEnoughSpace() {

        var size = this.getPostsStorageSpaceInMB();
        var enoughSpace = size >= this.storageThreshold;
        return () ->
                HealthCheckResponse.named("post-directory-has-space")
                        .state(enoughSpace).build();

    }

    @Gauge(unit = "mb")
    public long getPostsStorageSpaceInMB() {
        try {
            return Files.getFileStore(this.storagePath).getUsableSpace() / 1024 / 1024 ;
        } catch (IOException e) {
            throw new StorageException("cannot fetch size information from ->"
                    + this.storagePath, e );
        }
    }


    public Post createNew(Post post) throws IllegalStateException   {
        System.out.println(" Post " + post );
        var fileName = titleNormalizer.normalize(post.title);
        if (this.fileExists(fileName)) {
            throw new StorageException("Post with name " + fileName + " already exits");
        }

        post.setCreatedAt();
        post.title = titleNormalizer.normalize(post.title);
        String stringified =  serialize(post); 
        try {
            post.fileName = fileName;
            write(fileName, stringified);
        } catch (Exception e) {
            throw new StorageException("Cannot save post --> " + post.title, e);
        }

        return post;
    }


    boolean fileExists(String fileName) {
        Path fqn = this.storagePath.resolve(fileName);
        return Files.exists(fqn);
    }

    public void update(Post post) {
        var fileName = titleNormalizer.normalize(post.title);
        if (!this.fileExists(fileName)) {
            throw new BadRequestException("Post doesn't exists " + fileName
                    + " nothing to update, use POST to create Post ");
        }
        post.updatedModifiedAt();
        post.title = titleNormalizer.normalize(post.title);
        String stringified =  serialize(post);
        try {
            write(fileName, stringified);
        } catch (Exception e) {
            throw new StorageException("Cannot save post --> " + post.title, e);
        }

    }



    void write(String fileName, String content) throws IOException {
        System.out.println("write fileName ---> " + fileName );
        var path = this.storagePath.resolve(fileName);
        Files.writeString(path,content);
    } 

    String serialize(Post post) {
        var jsonb = JsonbBuilder.create();
        return jsonb.toJson(post);
    }

    public Post read(String fileName){
        String path = null;
        if (!this.fileExists(fileName)) {
            this.incrementPostNotExists();
            return null;   // in order to throw 204
        }
        try {
            System.out.println("read fileName --> " + fileName);
            path = readString(fileName);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read post " + fileName);
        }
        return deserialize(path);

    }

    private void incrementPostNotExists() {
        metricRegistry.counter("post_not_exists").inc();
    }

    Post deserialize(String fileLocation) {
        var jsonb = JsonbBuilder.create();
        return jsonb.fromJson(fileLocation, Post.class); 
    }    

    String readString(String fileName) throws IOException {
        var path = this.storagePath.resolve(fileName);
        return Files.readString(path) ; 
    }



}
