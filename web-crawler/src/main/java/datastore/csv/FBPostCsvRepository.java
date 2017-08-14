package datastore.csv;

import com.fasterxml.jackson.databind.ObjectMapper;
import datastore.FBPostRepository;
import datastore.dao.PostDao;
import fb.config.Config;
import fb.crawler.post.Posts;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Slf4j
public class FBPostCsvRepository implements FBPostRepository {

    private HashMap<String, PostDao> postsMap;

    public  FBPostCsvRepository(){
        postsMap = new HashMap<>();
        List<String> posts;
        try {
            posts = FileUtil.reader(Config.FB_POST_CSV);
        }catch (Exception f){
            posts = new ArrayList<>();
        }

        posts.forEach(p -> {
            try {
                Posts.Post post = Config.OBJECT_MAPPER.readValue(p, Posts.Post.class);
                postsMap.put(post.getId(), new PostDao(post.getId(), post.getMessage()));
            }catch (IOException e){
                log.error("Error in build Csv rep {}", e);
                throw new RuntimeException(e);
            }
                }
        );
    }

    @Override
    public int perist(List<PostDao> posts) {
        posts.stream().forEach(p -> {
            if(!postsMap.containsKey(p.getId())){
                FileUtil.writer(p, Config.FB_POST_CSV);
            }
            postsMap.put(p.getId(), p);
        });
        return posts.size();
    }

    @Override
    public int persist(Posts.Post post) {
        postsMap.put(post.getId(), new PostDao(post.getId(), post.getMessage()));
        FileUtil.writer(new PostDao(post.getId(), post.getMessage()), Config.FB_POST_CSV);
        return 1;
    }

    @Override
    public Posts.Post find(String postId) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public List<Posts.Post> findPostForFBPage(String fbpagedId) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public int count() {
        return postsMap.size();
    }
}
