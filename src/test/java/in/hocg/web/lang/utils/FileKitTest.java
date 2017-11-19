package in.hocg.web.lang.utils;

import org.junit.Test;

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
    
}