package datastore.dao;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentDao {
    private String id;
    private String userId;
    private String userName;
    private String message;
    private String postId;
}
