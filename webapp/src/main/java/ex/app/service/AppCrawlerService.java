package ex.app.service;


import com.google.inject.Inject;
import ex.app.model.request.UpdateTokenRequest;
import fb.crawler.FBCrawler;

public class AppCrawlerService {
    private final FBCrawler fbCrawler;

    @Inject
    public AppCrawlerService(FBCrawler fbCrawler) {
        this.fbCrawler = fbCrawler;
    }

    public void updateToken(UpdateTokenRequest request) {
        fbCrawler.updateToken(request.getAccessToken());
    }

    public String getFbPageDetail(String pageId) {
        return fbCrawler.getFBPageDetail(pageId);
    }
}
