package in.hocg.web.modules.web.api;

import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.body.response.Results;
import in.hocg.web.modules.system.domain.User;
import in.hocg.web.modules.security.JwtAuthenticationRequest;
import in.hocg.web.modules.security.JwtAuthenticationResponse;
import in.hocg.web.modules.system.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by hocgin on 2017/10/24.
 * email: hocgin@gmail.com
 */
@RestController
@RequestMapping("/api")
public class AuthController {
    private AuthService authService;
    
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public Results createAuthenticationToken(JwtAuthenticationRequest authenticationRequest, HttpSession session) throws AuthenticationException{
        final String token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        // Return the token
        return Results.success(token)
                .setMessage("申请 Token 成功");
    }
    
    @RequestMapping(value = "/auth/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) throws AuthenticationException{
        String token = request.getHeader("Authorization");
        String refreshedToken = authService.refresh(token);
        if(refreshedToken == null) {
            return ResponseEntity.badRequest()
                    .body(null);
        } else {
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        }
    }
    
    @RequestMapping(value = "/auth/register", method = RequestMethod.POST)
    public Results register(User user) throws AuthenticationException{
        CheckError checkError = CheckError.get();
        User register = authService.register(user, checkError);
        return Results.check(checkError)
                .setData(register);
    }
}
