package airhacks.blogpad.posts.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import airhacks.blogpad.posts.entity.Post;

public class PostStoreTest {
    

    PostStore cut ; 

    @BeforeEach
    public void init() {
        this.cut = new PostStore();
        this.cut.storageDir= "target";
        this.cut.init();
    }


    @Test
    public void serializePost() {
        String stringField = this.cut.serialize(new Post("Hello", "World"));   
        assertNotNull(stringField); 
        System.out.println("->" + stringField); 
    }

    @Test
    public void writeString() throws IOException {

        String path = "firstPost"; 
        String expected  = "hello, duke"; 

        this.cut.write(path, expected);
        String actual = this.cut.readString(path); 
        assertEquals (expected,  actual);
    }

    @Test
    public void savePost() throws IOException {
       // this.cut.createNew(new Post("first", "hello, duke"));
    }

    @Test
    public void savePostThenRead() throws IOException{
        /*String title = "first";
        String content = "hello, duke"; 
        Post actual = new Post(title, content); 

        this.cut.createNew(actual);
        Post expected =  this.cut.read(title); 
        assertEquals(actual.title, expected.title);
        assertEquals(actual.content, expected.content); */
    }
}
