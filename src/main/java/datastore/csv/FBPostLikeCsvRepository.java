package datastore.csv;


import datastore.FBPostLikeRepository;
import datastore.dao.PostLikeDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class FBPostLikeCsvRepository implements FBPostLikeRepository{
    private HashMap<String, List<String>> userToPostIds = new HashMap<>();
    private HashMap<String, List<PostLikeDao>> postIdsToUser = new HashMap<>();
    @Override
    public int persist(List<PostLikeDao> postLikeDaoList) {
        postLikeDaoList.forEach(l -> {
            buildpostIdsToUser(l);
            buildUserToPostIds(l);
        });
        return postLikeDaoList.size();
    }



    @Override
    public Optional<List<String>> postLikesByUser(String userId) {
        return Optional.ofNullable(userToPostIds.get(userId));
    }

    @Override
    public Optional<List<PostLikeDao>> postLikesByPostId(String postId) {
        return Optional.ofNullable(postIdsToUser.get(postId));
    }

    @Override
    public int totalUsers() {
        return userToPostIds.size();
    }


    private void buildUserToPostIds(PostLikeDao l) {
        List<String> postIds = userToPostIds.getOrDefault(l.getUserId(), new ArrayList<>());
        postIds.add(l.getPostId());
        userToPostIds.put(l.getUserId(), postIds);
    }

    private void buildpostIdsToUser(PostLikeDao l) {
        List<PostLikeDao> postIds = postIdsToUser.getOrDefault(l.getPostId(), new ArrayList<>());
        postIds.add(l);
        postIdsToUser.put(l.getPostId(), postIds);
    }
}
