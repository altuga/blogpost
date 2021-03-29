package airhacks.blogpad.posts.control;

import airhacks.blogpad.posts.entity.Post;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
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

    @PostConstruct
    public void init() {
        this.storagePath = Path.of(this.storageDir);
    }

    public Post save(Post post) throws IllegalStateException   {
        var fileName = titleNormalizer.normalize(post.title);
        if (this.fileExists(fileName)) {
            throw new StorageException("Post with name " + fileName + " already exits");
        }
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
        try {
            System.out.println("read fileName --> " + fileName );
            path = readString(fileName);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read post " + fileName);
        }
        return deserialize(path);

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
