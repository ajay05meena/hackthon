package datastore.csv;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FileUtil {
    static ObjectMapper OBJECTMAPPER = new ObjectMapper();
    public static void writer(Object object, String fileName){
        synchronized (fileName){
            try {
                File file = new File(fileName);
                FileWriter fileWriter = new FileWriter(file, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(OBJECTMAPPER.writeValueAsString(object));
                bufferedWriter.newLine();
                bufferedWriter.flush();
                bufferedWriter.close();
            }catch (Exception e){
                log.error("Error in writing {}", e);
                throw new RuntimeException(e);
            }

        }
    }

    public static List<String> reader(String fileName){
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            return bufferedReader.lines().collect(Collectors.toList());
        }catch (Exception e){
            log.error("Error in reading {}", e);
            throw new RuntimeException(e);
        }

    }
}
