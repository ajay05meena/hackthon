package datastore;

import fb.crawler.post.Posts;

import java.util.List;

public interface FBPostRepository {
    int perist(Posts posts);
    int persist(Posts.Post post);
    Posts.Post find(String postId);
    List<Posts.Post> findPostForFBPage(String fbpagedId);
    int count();
}
