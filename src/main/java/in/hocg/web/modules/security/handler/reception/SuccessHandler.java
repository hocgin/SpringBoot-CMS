package in.hocg.web.modules.security.handler.reception;

import com.google.gson.Gson;
import in.hocg.web.modules.base.body.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by hocgin on 2017/11/25.
 * email: hocgin@gmail.com
 */
@Component
public class SuccessHandler implements AuthenticationSuccessHandler {
    
    private Gson gson;
    @Autowired
    public SuccessHandler(Gson gson) {
        this.gson = gson;
    }
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            Results result = Results.success().setMessage("登陆成功");
            writer.write(gson.toJson(result));
        }
    }
}
