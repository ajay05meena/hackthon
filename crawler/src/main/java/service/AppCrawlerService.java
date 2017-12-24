package service;


import com.google.inject.Inject;
import fb.crawler.FBCrawlerService;

public class AppCrawlerService {
    private final FBCrawlerService fbCrawlerService;

    @Inject
    public AppCrawlerService(FBCrawlerService fbCrawlerService) {
        this.fbCrawlerService = fbCrawlerService;
    }



    public String getFbPageDetail(String pageId) {
        return fbCrawlerService.getFBPageDetail(pageId);
    }
}
