package datastore.csv;


import datastore.FBPostLikeRepository;
import datastore.dao.PostLikeDao;
import fb.config.Config;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
public class FBPostLikeCsvRepository implements FBPostLikeRepository{
    private HashMap<String, List<String>> userToPostIds = new HashMap<>();
    private HashMap<String, List<PostLikeDao>> postIdsToUser = new HashMap<>();

    public FBPostLikeCsvRepository(){
        List<String> comments = FileUtil.reader(Config.FB_POST_LIKE_CSV);
        comments.forEach(o -> {
            try {
                PostLikeDao dao = Config.OBJECT_MAPPER.readValue(o, PostLikeDao.class);
                buildpostIdsToUser(dao);
                buildUserToPostIds(dao);
            } catch (IOException e) {
                log.error("Error in creating map from file {}" ,e);
                e.printStackTrace();
            }
        });
    }

    @Override
    public int persist(List<PostLikeDao> postLikeDaoList) {
        postLikeDaoList.forEach(l -> {
            buildpostIdsToUser(l);
            buildUserToPostIds(l);
            FileUtil.writer(l, Config.FB_POST_LIKE_CSV);
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
