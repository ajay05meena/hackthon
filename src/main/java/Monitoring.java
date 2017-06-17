import datastore.FBPostCommentRepository;
import datastore.FBPostLikeRepository;
import datastore.FBPostRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
public class Monitoring implements Runnable{
    private final FBPostLikeRepository fbPostLikeRepository;
    private final FBPostRepository fbPostRepository;
    private final FBPostCommentRepository fbPostCommentRepository;

    @Inject
    public Monitoring(FBPostLikeRepository fbPostLikeRepository, FBPostRepository fbPostRepository, FBPostCommentRepository fbPostCommentRepository) {
        this.fbPostLikeRepository = fbPostLikeRepository;

        this.fbPostRepository = fbPostRepository;
        this.fbPostCommentRepository = fbPostCommentRepository;
    }

    @Override
    public void run() {
        Metrics m = new Metrics(fbPostLikeRepository.totalUsers(), fbPostCommentRepository.totalUserCommented(), fbPostRepository.count());
        log.info("Metrics " + m);
    }

    @AllArgsConstructor
    @Data
    private static class Metrics{
        private Integer totalUserWhoLikedThePosts;
        private Integer totalUserWhoCommentedOnPosts;
        private Integer totalNumberOfPosts;
    }
}
