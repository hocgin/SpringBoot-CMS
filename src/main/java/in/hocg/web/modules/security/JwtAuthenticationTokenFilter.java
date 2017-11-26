package in.hocg.web.modules.security;

import in.hocg.web.modules.security.details.user.IUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    
    @Autowired
    private IUserDetailsService userDetailsService;
    
    @Autowired
    private JwtTokenUtil tokenUtil;
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        
        if (!StringUtils.isEmpty(authHeader)
                && authHeader.startsWith("Bearer ")) {
            final String authToken = authHeader.substring("Bearer ".length()); // The part after "Bearer "
            String username = tokenUtil.getUsernameFromToken(authToken);
            
            logger.info("checking authentication " + username);
            
            if (!ObjectUtils.isEmpty(username)
                    && !ObjectUtils.isEmpty(SecurityContextHolder.getContext().getAuthentication())) {
                
                // 如果我们足够相信token中的数据，也就是我们足够相信签名token的secret的机制足够好
                // 这种情况下，我们可以不用再查询数据库，而直接采用token中的数据
                // 本例中，我们还是通过Spring Security的 @UserDetailsService 进行了数据查询
                // 但简单验证的话，你可以采用直接验证token是否合法来避免昂贵的数据查询
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                
                if (tokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                            request));
                    logger.info("authenticated user " + username + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        
        chain.doFilter(request, response);
    }
}
