package datastore.dao;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCommentDao {
    private String id;
    private String userId;
    private String userName;
    private String message;
    private String postId;
}
