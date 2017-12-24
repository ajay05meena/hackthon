package datastore;


import datastore.dao.PostCommentDao;

import java.util.List;

public interface FBPostCommentRepository {
    int persist(List<PostCommentDao> postCommentDaos);
    List<PostCommentDao> totalCommentOnPost(String post);
    List<PostCommentDao> totalCommentByUser(String userId);
    int totalUserCommented();
}
