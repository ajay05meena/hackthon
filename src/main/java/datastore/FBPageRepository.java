package datastore;


import java.io.FileNotFoundException;
import java.util.List;

public interface FBPageRepository {
    List<String> getAllFBPageIds() throws FileNotFoundException;
}
