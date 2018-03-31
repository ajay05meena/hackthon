package fb.crawler.fb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class Posts {
    private List<Post> data;
    private Paging paging;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Post implements Serializable{
        private String id;
        private String message;
        private From from;
        private String picture;
        private String link;
        private String source;
        private Likes likes;
        private Comments comments;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Paging{
        private String next;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class From{
        private String name;
        private String category;
        private String id;

    }


    @Data
    public static class Likes{
        private List<UserInfo> data;
        private Paging paging;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserInfo{
        private String id;
        private String category;
        private String name;
    }

    @Data
    public static class Comments{
        private List<Comment> data;
        private Paging paging;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)

    public static class Comment{
        private UserInfo from;
        private String message;
        @JsonProperty("like_count")
        private String likeCount;
        private String id;
        @JsonProperty("created_time")
        private String createdTime;
    }
}
