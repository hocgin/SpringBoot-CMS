package in.hocg.web.modules.system.controller.system;

import in.hocg.web.global.aspect.ILog;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.base.filter.lang.IdsFilter;
import in.hocg.web.modules.system.domain.IFile;
import in.hocg.web.modules.system.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by hocgin on 2017/11/19.
 * email: hocgin@gmail.com
 * /admin/system/file/upload
 */
@Controller
@RequestMapping("/admin/system/file")
public class IFileController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/admin/system/file/%s";
    
    private IFileService iFileService;
    
    @Autowired
    public IFileController(IFileService iFileService) {
        this.iFileService = iFileService;
    }
    
    
    @GetMapping({"/index.html", "/"})
    public String vIndex(Model model) {
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
    @GetMapping({"/upload-view.html"})
    public String vUpload(Model model) {
        return String.format(BASE_TEMPLATES_PATH, "upload-view");
    }
    
    @PostMapping("/data")
    @ResponseBody
    public DataTablesOutput data(@RequestBody DataTablesInput filter) {
        return iFileService.data(filter);
    }
    
    @PostMapping("/upload")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.file.add')")
    public Results upload(@RequestParam("file") MultipartFile file) {
        CheckError checkError = CheckError.get();
        IFile iFile = iFileService.save(IFile.Src.ADMIN, file, checkError);
        return Results.check(checkError).setData(iFile);
    }
    
    @PostMapping("/delete")
    @ResponseBody
    @ILog(value = "删除文件引用", msg = "'删除文件引用ID[' + #args[0].id +']'")
    @PreAuthorize("hasPermission(null, 'sys.file.delete')")
    public Results delete(@Validated IdsFilter idsFilter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        iFileService.delete(idsFilter.getId());
        return Results.success().setMessage("删除成功");
    }
    
    
    @PostMapping("/available/{id}")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.file.edit')")
    public Results available(@PathVariable("id") String id, boolean available) {
        iFileService.updateAvailable(id, available);
        return Results.success()
                .setMessage(String.format("%s成功", available ? "开启" : "禁用"));
    }
    
    @GetMapping("/download/{id}")
    public ResponseEntity download(@PathVariable String id,
                                   @RequestParam(value = "rename", required = false) String rename) throws IOException {
        IFile iFile = iFileService.findById(id);
        if (!iFile.getFile().exists()) {
            return ResponseEntity.notFound().build();
        }
        FileSystemResource file = new FileSystemResource(iFile.getFile());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", StringUtils.isEmpty(rename) ? file.getFilename() : rename));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
        
    }
    
    @PostMapping("/clear")
    @ResponseBody
    @ILog(value = "清理文件", msg = "'清理选项[' + #args[0] +']'")
    @PreAuthorize("hasPermission(null, 'sys.file.delete')")
    public Results clear(@RequestParam(value = "removeFile") boolean removeFile) throws IOException {
        iFileService.clear(removeFile);
        return Results.success()
                .setMessage("清理成功");
    }
    
}
