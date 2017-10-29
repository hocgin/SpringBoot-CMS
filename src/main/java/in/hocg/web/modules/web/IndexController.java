package in.hocg.web.modules.web;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin")
public class IndexController extends BaseController {

// Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob2NnaW4iLCJjcmVhdGVkIjoxNTA4OTQyNDgyMjEyLCJleHAiOjE1MDk1NDcyODJ9.NKeNuv_510BfnxAi3XvOOsn9g1xeTeg7QFnDGf3Ebx3wj7bcg3Tv5iyxAxPrzjtgwEnG0DVL2PLqYg3blDZ0MQ
    @PostAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/permission")
    public String tpl() {
        return "admin/tpl";
    }
}
