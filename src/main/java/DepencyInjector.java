import com.google.inject.AbstractModule;
import datastore.FBPostRepository;
import datastore.csv.FBPostCsvRepository;

/**
 * Created by ajay.meena on 16/06/17.
 */
public class DepencyInjector extends AbstractModule {
    @Override
    protected void configure() {
        bind(FBPostRepository.class).to(FBPostCsvRepository.class);
    }
}
