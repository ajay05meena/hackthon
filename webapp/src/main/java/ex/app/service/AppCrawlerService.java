package ex.app.service;


import com.google.inject.Inject;
import ex.app.model.request.UpdateTokenRequest;
import fb.crawler.FBCrawlerService;

public class AppCrawlerService {
    private final FBCrawlerService fbCrawlerService;

    @Inject
    public AppCrawlerService(FBCrawlerService fbCrawlerService) {
        this.fbCrawlerService = fbCrawlerService;
    }

    public void updateToken(UpdateTokenRequest request) {
        fbCrawlerService.updateToken(request.getAccessToken());
    }

    public String getFbPageDetail(String pageId) {
        return fbCrawlerService.getFBPageDetail(pageId);
    }
}
