import com.google.inject.AbstractModule;
import datastore.FBPageRepository;
import datastore.FBPostRepository;
import datastore.FBTokenReposistory;
import datastore.csv.FBPageCsvRepository;
import datastore.csv.FBPostCsvRepository;
import datastore.csv.FBTokenCsvReposistory;


public class DepencyInjector extends AbstractModule {
    @Override
    protected void configure() {
        bind(FBPostRepository.class).to(FBPostCsvRepository.class).asEagerSingleton();
        bind(FBPageRepository.class).to(FBPageCsvRepository.class).asEagerSingleton();
        bind(FBTokenReposistory.class).to(FBTokenCsvReposistory.class).asEagerSingleton();
    }
}
