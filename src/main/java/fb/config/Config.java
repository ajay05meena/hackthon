package fb.config;


import com.fasterxml.jackson.databind.ObjectMapper;

public class Config {
    public static final String FB_POST_COMMENTS_CSV = "postComments";
    public static final String FB_POST_LIKE_CSV = "postLikes";
    public static final String FB_POST_CSV = "fbpost";
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
}
