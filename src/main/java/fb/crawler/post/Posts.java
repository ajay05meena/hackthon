package fb.crawler.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Paging{
        private String next;
    }
}
