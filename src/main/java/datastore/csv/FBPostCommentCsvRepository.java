package datastore.csv;

import datastore.FBPostCommentRepository;
import datastore.dao.PostCommentDao;
import fb.config.Config;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FBPostCommentCsvRepository implements FBPostCommentRepository {

    private Map<String, List<PostCommentDao>> postToComment = new HashMap<>();
    private Map<String, List<PostCommentDao>> userToComment = new HashMap<>();

    public FBPostCommentCsvRepository(){
        List<String> comments = FileUtil.reader(Config.FB_POST_COMMENTS_CSV);
        comments.forEach(o -> {
            try {
                PostCommentDao dao = Config.OBJECT_MAPPER.readValue(o, PostCommentDao.class);
                buildPostToComment(dao);
                buildUserToComment(dao);
            } catch (IOException e) {
                log.error("Error in creating map from file {}" ,e);
                e.printStackTrace();
            }
    });
    }

    @Override
    public int persist(List<PostCommentDao> postCommentDaos) {
        postCommentDaos.stream().forEach(c -> {
            buildPostToComment(c);
            buildUserToComment(c);
            FileUtil.writer(c, Config.FB_POST_COMMENTS_CSV);
        });
        return 0;
    }

    private void buildUserToComment(PostCommentDao c) {
        List<PostCommentDao> postCommentDaos = userToComment.getOrDefault(c.getUserId(), new ArrayList<>());
        postCommentDaos.add(c);
        userToComment.put(c.getUserId(), postCommentDaos);
    }

    private void buildPostToComment(PostCommentDao c) {
        List<PostCommentDao> postCommentDaos = postToComment.getOrDefault(c.getPostId(), new ArrayList<>());
        postCommentDaos.add(c);
        postToComment.put(c.getPostId(), postCommentDaos);
    }

    @Override
    public List<PostCommentDao> totalCommentOnPost(String post) {
        return postToComment.getOrDefault(post, new ArrayList<>());
    }

    @Override
    public List<PostCommentDao> totalCommentByUser(String userId) {
        return userToComment.getOrDefault(userId, new ArrayList<>());
    }

    @Override
    public int totalUserCommented() {
        return userToComment.size();
    }


}
