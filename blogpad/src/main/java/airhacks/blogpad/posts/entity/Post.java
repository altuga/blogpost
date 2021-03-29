package airhacks.blogpad.posts.entity;

public class Post {

    public String title; 
    public String content; 

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
}
