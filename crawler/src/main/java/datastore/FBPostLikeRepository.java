package datastore;

import datastore.dao.PostLikeDao;

import java.util.List;
import java.util.Optional;

public interface FBPostLikeRepository {
    int persist(List<PostLikeDao> postLikeDaoList);
    Optional<List<String>> postLikesByUser(String userId);
    Optional<List<PostLikeDao>> postLikesByPostId(String postId);
    int totalUsers();
}
