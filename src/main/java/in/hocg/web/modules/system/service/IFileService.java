package in.hocg.web.modules.system.service;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.system.domain.IFile;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by hocgin on 2017/11/19.
 * email: hocgin@gmail.com
 */
public interface IFileService {
    DataTablesOutput data(DataTablesInput filter);
    
    IFile save(IFile.Src src, MultipartFile file, CheckError checkError);
    
    
    IFile findById(String id);
    
    IFile findByKeepName(String keepName);
    
    void delete(String... id);
    
    void updateAvailable(String id, boolean b);
    
    void clear(boolean isRemoveFile) throws IOException;
    
    List<IFile> findAll();
}
