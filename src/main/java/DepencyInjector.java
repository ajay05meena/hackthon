import com.google.inject.AbstractModule;
import datastore.FBPageRepository;
import datastore.FBPostRepository;
import datastore.csv.FBPageCsvRepository;
import datastore.csv.FBPostCsvRepository;


public class DepencyInjector extends AbstractModule {
    @Override
    protected void configure() {
        bind(FBPostRepository.class).to(FBPostCsvRepository.class).asEagerSingleton();
        bind(FBPageRepository.class).to(FBPageCsvRepository.class).asEagerSingleton();
    }
}
