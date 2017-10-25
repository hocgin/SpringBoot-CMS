package in.hocg.web.modules.web;

import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.body.response.Results;
import in.hocg.web.modules.domain.User;
import in.hocg.web.modules.security.JwtAuthenticationRequest;
import in.hocg.web.modules.security.JwtAuthenticationResponse;
import in.hocg.web.modules.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by hocgin on 2017/10/24.
 * email: hocgin@gmail.com
 */
@RestController
public class AuthController {
    private final AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @Value("${jwt.header}")
    private String tokenHeader;
    
    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(JwtAuthenticationRequest authenticationRequest) throws AuthenticationException{
        final String token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        
        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }
    
    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) throws AuthenticationException{
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if(refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        }
    }
    
    @RequestMapping(value = "${jwt.route.authentication.register}", method = RequestMethod.POST)
    public Results register(User user) throws AuthenticationException{
        CheckError checkError = CheckError.get();
        User register = authService.register(user, checkError);
        if (checkError.isPass()) {
            return Results.success(register);
        }
        return Results.error(1, checkError.getFirstErrorMessage());
    }
}
