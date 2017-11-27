package in.hocg.web.modules.pub;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.system.domain.IFile;
import in.hocg.web.modules.system.service.IFileService;
import in.hocg.web.modules.system.service.MemberService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by hocgin on 2017/11/19.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/public")
public class PublicController extends BaseController {
    
    private IFileService iFileService;
    private MemberService memberService;
    
    @Autowired
    public PublicController(IFileService iFileService,
                            MemberService memberService) {
        this.iFileService = iFileService;
        this.memberService = memberService;
    }
    
    /**
     * 下载文件
     * @param id
     * @param src [Keep-Name, ID]
     * @param rename 重命名
     * @return
     * @throws IOException
     */
    @GetMapping("/download/{id}")
    public ResponseEntity download(@PathVariable String id,
                                   @RequestParam(value = "src", required = false) String src,
                                   @RequestParam(value = "rename", required = false) String rename) throws IOException {
        IFile iFile;
        switch (Optional.ofNullable(src).orElse("ID")) {
            case "Keep-Name":
                iFile = iFileService.findByKeepName(id);
                break;
            case "ID":
            default:
                iFile = iFileService.findById(id);
            
        }
        
        if (!iFile.getAvailable()
                || !iFile.getFile().exists()) {
            return ResponseEntity.notFound().build();
        }
        FileSystemResource file = new FileSystemResource(iFile.getFile());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", Optional.ofNullable(rename).orElse(file.getFilename())));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
        
    }
    
    
    /**
     * 图片自适应缩放
     *
     * @param id       图片ID
     * @param width
     * @param height
     * @param force    true 强制缩放
     * @param src      [Keep-Name, ID]
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping("/image/{id}")
    public ResponseEntity image(@PathVariable String id,
                                @RequestParam(value = "width", required = false) Integer width,
                                @RequestParam(value = "height", required = false) Integer height,
                                @RequestParam(value = "force", required = false) boolean force,
                                @RequestParam(value = "src", required = false) String src,
                                HttpServletResponse response) throws IOException {
        IFile iFile;
        switch (Optional.ofNullable(src).orElse("ID")) {
            case "Keep-Name":
                iFile = iFileService.findByKeepName(id);
                break;
            case "ID":
            default:
                iFile = iFileService.findById(id);
            
        }
        
        if (!iFile.getAvailable()
                || !iFile.getFile().exists()) {
            return ResponseEntity.notFound().build();
        }
        
        Thumbnails.Builder<File> builder = Thumbnails.of(iFile.getFile());
        if (width != null
                && height != null
                && force) {
            builder.forceSize(width, height);
        } else {
            BufferedImage bufferedImage = ImageIO.read(iFile.getFile());
            width = Optional.ofNullable(width).orElse(bufferedImage.getWidth());
            height = Optional.ofNullable(height).orElse(bufferedImage.getHeight());
            builder.size(width, height);
        }
        
        builder.toOutputStream(response.getOutputStream());
        return ResponseEntity.ok().build();
    }
    
    
    @GetMapping("/verify-email.html")
    @ResponseBody
    public Object vVerifyEmail(@RequestParam("id") String id) {
        // 认证成功
        CheckError checkError = CheckError.get();
        memberService.verifyEmail(id, checkError);
        return vRedirect("/");
    }
    
}
