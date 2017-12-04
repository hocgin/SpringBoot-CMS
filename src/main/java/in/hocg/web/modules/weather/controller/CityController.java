package in.hocg.web.modules.weather.controller;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.ResultCode;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update;
import in.hocg.web.modules.base.filter.lang.IdsFilter;
import in.hocg.web.modules.system.domain.IFile;
import in.hocg.web.modules.system.service.IFileService;
import in.hocg.web.modules.weather.domain.City;
import in.hocg.web.modules.weather.filter.CityDataTablesInputFilter;
import in.hocg.web.modules.weather.filter.CityFilter;
import in.hocg.web.modules.weather.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hocgin on 2017/11/21.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/weather/city")
public class CityController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/admin/weather/city/%s";
    
    private CityService cityService;
    private IFileService iFileService;
    
    @Autowired
    public CityController(CityService cityService,
                          IFileService iFileService) {
        this.cityService = cityService;
        this.iFileService = iFileService;
    }
    
    @GetMapping({"/index.html"})
    public String vIndex() {
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
    @GetMapping({"/upload-modal.html"})
    public String vUpload() {
        return String.format(BASE_TEMPLATES_PATH, "upload-modal");
    }
    
    @GetMapping({"/query-modal.html"})
    public String vQuery() {
        return String.format(BASE_TEMPLATES_PATH, "query-modal");
    }
    
    @GetMapping({"/add-view.html"})
    public String vAdd() {
        return String.format(BASE_TEMPLATES_PATH, "add-view");
    }
    
    
    
    @PostMapping("/data")
    @ResponseBody
    public DataTablesOutput data(@RequestBody CityDataTablesInputFilter filter) {
        return cityService.findAll(filter);
    }
    
    @PostMapping("/deletes")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'weather.city.delete')")
    public Results deletes(@Validated IdsFilter filter) {
        cityService.deletes(filter.getId());
        return Results.success();
    }
    
    @PostMapping("/insert")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'weather.city.add')")
    public Results insert(@Validated(Insert.class) CityFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        cityService.insert(filter, checkError);
        return Results.check(checkError).setMessage("添加成功");
    }
    
    @PostMapping("/update")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'weather.city.edit')")
    public Results update(@Validated(Update.class) CityFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        cityService.update(filter, checkError);
        return Results.check(checkError);
    }
    
    
    @PostMapping("/upload")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'weather.city.add')")
    public Results upload(@RequestParam("fid") String fid) throws IOException {
        IFile iFile = iFileService.findById(fid);
        File file = iFile.getFile();
        if (!file.exists()) {
            return Results.error(ResultCode.INTERNAL_SERVER_ERROR, "未找到上传文件");
        }
        // todo 校验文件
        FileReader reader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(reader);
        Set<City> citySet = new HashSet<>();
        bufferedReader.lines().forEach(line -> {
            String[] args = line.split(",");
            if (args.length == 6) {
                City city = new City();
                city.setCode(args[0]);
                city.setCounty(args[1]);
                city.setCity(args[2]);
                city.setProvince(args[3]);
                city.setLatitude(args[4]);
                city.setLongitude(args[5]);
                city.createdAt();
                citySet.add(city);
            }
        });
        bufferedReader.close();
        reader.close();
        cityService.inserts(citySet);
        
        return Results.success();
    }
    
    @PostMapping("/clear")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'weather.city.delete')")
    public Results clear() {
        cityService.deleteAll();
        return Results.success()
                .setMessage("清空成功");
    }
}
