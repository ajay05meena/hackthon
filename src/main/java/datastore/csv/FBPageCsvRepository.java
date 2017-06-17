package datastore.csv;


import datastore.FBPageRepository;


import java.util.List;

public class FBPageCsvRepository implements FBPageRepository{
    final private static String CSV_FILE = "pageIds";
    @Override
    public List<String> getAllFBPageIds()  {
        return FileUtil.reader(CSV_FILE);
    }
}
