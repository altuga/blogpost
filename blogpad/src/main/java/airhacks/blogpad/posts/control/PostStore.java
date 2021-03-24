package airhacks.blogpad.posts.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;


import org.eclipse.microprofile.config.inject.ConfigProperty;

import airhacks.blogpad.posts.entity.Post;


public class PostStore {


    @Inject
    @ConfigProperty(name="root.storage.dir")
    String storageDir;
    private Path storagePath = null; 

    @PostConstruct
    public void init() {
        this.storagePath = Path.of(this.storageDir);
    }

    public void save(Post post) throws IllegalStateException   {
        String stringified =  serialize(post); 
        try {
            System.out.println("save stringified--> " +  stringified);
            System.out.println("save post.title --> " + post.title);
            System.out.println("save post.content --> " + post.content);
            write(post.title, stringified); 
        } catch (IOException e) {
            throw new IllegalStateException("Cannot save post " + post.title);
        }
       
    }

    void write(String fileName, String content) throws IOException {
        System.out.println("write fileName --> " + fileName );
        Path path = this.storagePath.resolve(fileName); 
        Files.writeString(path,content);
    } 

    String serialize(Post post) {
        Jsonb jsonb = JsonbBuilder.create();
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
        Jsonb jsonb = JsonbBuilder.create();
        return jsonb.fromJson(fileLocation, Post.class); 
    }    

    String readString(String fileName) throws IOException {
        Path path = this.storagePath.resolve(fileName);
        return Files.readString(path) ; 
    }




    
}
