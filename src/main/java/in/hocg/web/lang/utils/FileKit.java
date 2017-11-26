package in.hocg.web.lang.utils;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hocgin on 2017/11/19.
 * email: hocgin@gmail.com
 */
public class FileKit {
    
    /**
     * 获取文件MD5
     *
     * @param file
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static String MD5(File file) throws NoSuchAlgorithmException,
            IOException {
        return MD5(new FileInputStream(file));
    }
    
    public static String MD5(String filePath) throws IOException, NoSuchAlgorithmException {
        return MD5(Files.newInputStream(Paths.get(filePath)));
    }
    
    public static String MD5(InputStream is) throws NoSuchAlgorithmException, IOException {
        byte[] bytes = DigestUtils.md5Digest(is);
        char[] encode = Hex.encode(bytes);
        return String.valueOf(encode);
    }
    
    public static void removeIn(String dirPath, String... keepName) {
        
        for (String name : keepName) {
            try {
                Files.deleteIfExists(Paths.get(dirPath, name));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static String read(File file) throws IOException {
        byte[] buffer = new byte[(int) file.length()];
        FileInputStream is = new FileInputStream(file);
        is.read(buffer, 0, buffer.length);
        is.close();
        return new String(buffer);
    }
}
