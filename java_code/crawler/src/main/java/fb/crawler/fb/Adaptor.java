package fb.crawler.fb;

import datastore.dao.PostCommentDao;
import datastore.dao.PostDao;
import datastore.dao.PostLikeDao;
import fb.crawler.fb.model.Posts;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Adaptor {

     static List<PostDao> convertToPostDao(List<Posts.Post> postList){
        return postList.stream().map(p -> new PostDao(p.getId(), p.getMessage())).collect(Collectors.toList());
    }

     static List<PostLikeDao> convertToUserLikeDao(List<Posts.UserInfo> userInfos, String postId) {
        return userInfos.stream().map(l -> new PostLikeDao(l.getId(), l.getName(), postId)).collect(Collectors.toList());
    }

     static List<PostCommentDao> convertToCommentDao(List<Posts.Comment> comments, String postId) {
         if(comments ==null){
             return new ArrayList<>();
         }
         return comments.stream().map(c -> new PostCommentDao(c.getId(), c.getId().split("_")[0], null, c.getMessage(),postId)).collect(Collectors.toList());
    }
}
