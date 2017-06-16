package datastore.csv;

import datastore.FBTokenReposistory;


import java.util.Collections;
import java.util.List;

public class FBTokenCsvReposistory implements FBTokenReposistory{
    private final static String CSV_FILE = "tokens";
    @Override
    public String getRandomToken() {
        List<String> tokens = FileUtil.reader(CSV_FILE);
        Collections.shuffle(tokens);
        return tokens.get(0);
    }
}
