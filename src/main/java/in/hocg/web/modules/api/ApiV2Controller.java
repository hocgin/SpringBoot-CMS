package in.hocg.web.modules.api;

import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.Results;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hocgin on 2017/11/28.
 * email: hocgin@gmail.com
 */
@Api(value = "sd",description="简单的计算服务API")
@RestController
@RequestMapping("/api/v1")
public class ApiV2Controller extends BaseController {
    
    @ApiOperation(value = "一个测试API", notes = "第一个测试API")
    @PostMapping("/ok")
    @ResponseBody
    public Results ok() {
        return Results.success();
    }
    
    @ApiOperation(value = "一个测试ok2", notes = "第一个测试API")
    @PostMapping("/entriy")
    @ResponseBody
    public ResponseEntity<Results> ok2() {
        return Results.success().asOkResponseEntity();
    }
    
    @GetMapping("/ok/{id}")
    public String ok3(@PathVariable("id") String id) {
        return "/sd/sd.html";
    }
}
