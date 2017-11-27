package in.hocg.web.lang.utils;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2017/11/19.
 * email: hocgin@gmail.com
 */
public class FileKitTest {
    @Test
    public void MD5() throws Exception {
        
        
        List<String> fileList = Files.list(Paths.get("/Users/hocgin/Desktop/FileUpload"))
                .map(Path::getFileName)
                .map(Path::toString)
                .collect(Collectors.toList());
    
        List<String> dbList = new ArrayList<>();
        dbList.add("e9263af7f90e04ffb4d5dcc510ef6d6c");
        dbList.add("d41d8cd98f00b204e9800998ecf8427e");
        dbList.add("d41d8cd98f00b204e9800998ecf8427x");
    
        fileList.retainAll(dbList);
        
        System.out.println(fileList);
    }
    
    @Test
    public void testAdminHtml() throws IOException {
        File file = new File("/Users/hocgin/Desktop/cityidloc.csv");
        FileReader reader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(reader);
        final int[] count = {0};
        bufferedReader.lines().forEach(line -> {
            count[0]++;
            String[] args = line.split(",");
            System.out.println(String.format("区号: %s   县: %s  市: %s  省: %s (%s, %s)", args[0], args[1], args[2], args[3], args[4], args[5]));
        });
    
        System.out.println(String.format("数量: %s", String.valueOf(count[0])));
    }
    
    @Test
    public void testLines() {
        List<String> lines = StringKit.lines("sdsa\ndfas\r\nsda");
        System.out.println(lines);
    }
}