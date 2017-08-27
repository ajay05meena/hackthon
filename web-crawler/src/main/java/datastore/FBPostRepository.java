package datastore;

import datastore.dao.PostDao;
import fb.crawler.fb.Posts;

import java.util.List;

public interface FBPostRepository {
    int perist(List<PostDao> posts);
    int persist(Posts.Post post);
    Posts.Post find(String postId);
    List<Posts.Post> findPostForFBPage(String fbpagedId);
    int count();
}
