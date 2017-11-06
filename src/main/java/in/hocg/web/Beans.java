package in.hocg.web;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
@Configuration
public class Beans {
    
    @Bean
    public Gson gson() {
        return new Gson();
    }
    
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
