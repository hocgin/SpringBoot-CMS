package in.hocg.web.modules.system.service.impl;

import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.utils.StringKit;
import in.hocg.web.modules.base.Base2Service;
import in.hocg.web.modules.system.domain.ShortUrl;
import in.hocg.web.modules.system.domain.repository.ShortUrlRepository;
import in.hocg.web.modules.system.filter.ShortUrlFilter;
import in.hocg.web.modules.system.service.ShortUrlService;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
@Service
public class ShortUrlServiceImpl extends Base2Service<ShortUrl, String, ShortUrlRepository>
        implements ShortUrlService {
    @Override
    public ShortUrl findOneByCode(String code) {
        return repository().findTopByCodeAndAvailable(code, true);
    }
    
    @Override
    public DataTablesOutput data(DataTablesInput input) {
        return repository().findAll(input);
    }
    
    @Override
    public void insert(ShortUrlFilter filter, CheckError checkError) {
        ShortUrl shortUrl = filter.get();
        if (!StringUtils.isEmpty(filter.getCode())) { // 填 Code
            if (!ObjectUtils.isEmpty(repository.findTopByCode(shortUrl.getCode()))) {
                checkError.putError("code 已经存在");
                return;
            }
        } else { // 未填 Code
            if (!ObjectUtils.isEmpty(repository.findTopByOriginalUrl(filter.getOriginalUrl()))) {
                checkError.putError("短链已经存在");
                return;
            }
            // 生成短链
            shortUrl.setCode(String.format("_%s", _shortUrl(filter.getOriginalUrl())[0]));
        }
        repository().insert(shortUrl);
    }
    
    @Override
    public void update(ShortUrlFilter filter, CheckError checkError) {
        ShortUrl shortUrl = repository.findOne(filter.getId());
        if (ObjectUtils.isEmpty(shortUrl)) {
            checkError.putError("短链接异常");
            return;
        }
        if (repository.alreadyExists(filter.getCode(), shortUrl.getId())) {
            checkError.putError("code 已经存在");
            return;
        }
        repository().save(filter.update(shortUrl));
    }
    
    @Override
    public void delete(String[] id, CheckError checkError) {
        repository.deleteAllByIdIn(id);
    }
    
    @Override
    public void available(String id, boolean available) {
        ShortUrl shortUrl = repository.findOne(id);
        if (!ObjectUtils.isEmpty(shortUrl)) {
            shortUrl.setAvailable(available);
            shortUrl.updatedAt();
            repository.save(shortUrl);
        }
    }
    
    
    /**
     * 短链生成算法
     *
     * @param url
     * @return
     */
    public String[] _shortUrl(String url) {
        // 可以自定义生成 MD5 加密字符传前的混合 KEY
        String key = "hocgin";
        // 要使用生成 URL 的字符
        String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h",
                "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
                "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
                "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z"
            
        };
        // 对传入网址进行 MD5 加密
        String hex = StringKit.md5(key + url);
        String[] resUrl = new String[4];
        for (int i = 0; i < 4; i++) {
            // 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
            String sTempSubString = hex.substring(i * 8, i * 8 + 8);
            
            // 这里需要使用 long 型来转换，因为 Inteper .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用 long ，则会越界
            long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
            StringBuilder outChars = new StringBuilder();
            for (int j = 0; j < 6; j++) {
                // 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
                long index = 0x0000003D & lHexLong;
                // 把取得的字符相加
                outChars.append(chars[(int) index]);
                // 每次循环按位右移 5 位
                lHexLong = lHexLong >> 5;
            }
            // 把字符串存入对应索引的输出数组
            resUrl[i] = outChars.toString();
        }
        return resUrl;
    }
}
