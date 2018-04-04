package service;


import com.google.inject.Inject;
import fb.crawler.FBCrawlerService;
import fb.crawler.fb.model.FBPageDetail;

import java.io.IOException;

public class AppCrawlerService {
    private final FBCrawlerService fbCrawlerService;

    @Inject
    public AppCrawlerService(FBCrawlerService fbCrawlerService) {
        this.fbCrawlerService = fbCrawlerService;
    }



    public FBPageDetail getFbPageDetail(String pageId) {
        return fbCrawlerService.getFBPageDetail(pageId);
    }

    public String getFbPageFeed(String pageId){
         fbCrawlerService.fetchFeedDataForPost(pageId);
         return "done";
    }

    public void populateComment() {
        fbCrawlerService.populateComment();
    }
}
