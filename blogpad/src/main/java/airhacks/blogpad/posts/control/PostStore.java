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
        System.out.println("post.title " + post.title);
        var fileName = this.normalize(post.title);
        System.out.println("after normalize - fileName " + fileName);
        String stringified =  serialize(post); 
        try {
            System.out.println("save stringified--> " +  stringified);
            System.out.println("save post.title --> " + post.title);
            System.out.println("save post.content --> " + post.content);
            write(fileName, stringified);
        } catch (IOException e) {
            throw new StorageException("Cannot save post --> " + post.title, e);
        }
       
    }

    String normalize(String title) {
          return title.codePoints().
                  map(this::replaceWithDigitOrLetter).collect
                  (StringBuffer::new, StringBuffer::appendCodePoint, StringBuffer::append).toString();
    }

    int replaceWithDigitOrLetter(int codePoint) {
        if(Character.isLetterOrDigit(codePoint)) {
            return  codePoint;
        } else{
            return "-".codePoints().findFirst().orElseThrow();
        }
    }

    void write(String fileName, String content) throws IOException {
        System.out.println("write fileName --> " + fileName );
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
