package in.hocg.web.modules;

import in.hocg.web.lang.PageKit;
import in.hocg.web.modules.system.domain.ShortUrl;
import in.hocg.web.modules.system.service.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
@Controller
public class IndexController {
    
    private ShortUrlService shortUrlService;
    @Autowired
    public IndexController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }
    
    /**
     * 短链解析
     * @param code
     * @return
     */
    @RequestMapping("/u/{code}")
    public String u(@PathVariable("code") String code) {
        ShortUrl shortUrl = shortUrlService.findOneByCode(code);
        if (ObjectUtils.isEmpty(shortUrl)
                || !shortUrl.getAvailable()) {
            return PageKit.v404;
        }
        return String.format("redirect:%s", shortUrl.getOriginalUrl());
    }
}
