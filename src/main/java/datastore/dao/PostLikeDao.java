package datastore.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostLikeDao {
    private String userId;
    private String userName;
    private String postId;
}
