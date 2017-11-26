package in.hocg.web;

import in.hocg.web.global.component.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hocgin on 2017/11/26.
 * email: hocgin@gmail.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMail {
    @Autowired
    MailService mailService;
    
    @Test
    public void testMail2() throws MessagingException, IOException, com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException {
        Map<String, Object> param = new HashMap<>();
        param.put("today", "2017");
        param.put("tup", "tup");
        Map<String, File> fileMap = new HashMap<>();
        fileMap.put("f", new File("/Users/hocgin/Desktop/FileUpload/b3d1920dcbe38c64ca6250af3a2ca985"));
        Map<String, File> img = new HashMap<>();
        img.put("tup", new File("/Users/hocgin/Pictures/hocgin/hocgin.png"));
        mailService.sendUseTemplate("578797748@qq.com", "有内容的标题","_mail/verify-email",
                param, img, fileMap);
    }
}
