package airhacks.blogpad.posts.entity;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class Post {

    public String fileName;

    @Size(min = 3, max = 255)
    public String title;

    @Size(min = 3)
    public String content;


    public LocalDateTime createdAt;
    public LocalDateTime modifiedAt;

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Post() {

    }

    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    public void updatedModifiedAt() {
        this.modifiedAt = LocalDateTime.now();
    }

}
