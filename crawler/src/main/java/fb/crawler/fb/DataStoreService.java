package fb.crawler.fb;


import com.google.inject.Inject;
import datastore.FBPostCommentRepository;
import datastore.FBPostLikeRepository;
import datastore.FBPostRepository;
import datastore.dao.PostCommentDao;
import datastore.dao.PostDao;
import datastore.dao.PostLikeDao;

import java.util.List;

public class DataStoreService {
    private final FBPostRepository fbPostRepository;
    private final FBPostLikeRepository fbPostLikeRepository;
    private final FBPostCommentRepository fbPostCommentRepository;

    @Inject
    public DataStoreService(FBPostRepository fbPostRepository, FBPostLikeRepository fbPostLikeRepository, FBPostCommentRepository fbPostCommentRepository) {
        this.fbPostRepository = fbPostRepository;
        this.fbPostLikeRepository = fbPostLikeRepository;
        this.fbPostCommentRepository = fbPostCommentRepository;
    }

    public int persistPosts(List<PostDao> postDaoList){
        return fbPostRepository.perist(postDaoList);
    }

    public int persistPostLikes(List<PostLikeDao> postLikeDaoList) {
        return fbPostLikeRepository.persist(postLikeDaoList);
    }

    public int persistPostComments(List<PostCommentDao> postCommentDaoList) {
        return fbPostCommentRepository.persist(postCommentDaoList);
    }
}
