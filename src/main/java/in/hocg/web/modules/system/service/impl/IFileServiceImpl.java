package in.hocg.web.modules.system.service.impl;

import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.utils.FileKit;
import in.hocg.web.modules.base.BaseService;
import in.hocg.web.modules.system.domain.IFile;
import in.hocg.web.modules.system.domain.Variable;
import in.hocg.web.modules.system.domain.repository.IFileRepository;
import in.hocg.web.modules.system.service.IFileService;
import in.hocg.web.modules.system.service.VariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2017/11/19.
 * email: hocgin@gmail.com
 */
@Service
public class IFileServiceImpl extends BaseService
        implements IFileService {
    private VariableService variableService;
    private IFileRepository iFileRepository;
    
    @Autowired
    public IFileServiceImpl(IFileRepository iFileRepository,
                            VariableService variableService) {
        this.iFileRepository = iFileRepository;
        this.variableService = variableService;
    }
    
    @Override
    public DataTablesOutput data(DataTablesInput filter) {
        return iFileRepository.findAll(filter);
    }
    
    @Override
    public IFile save(IFile.Src src, MultipartFile file, CheckError checkError) {
        if (file.isEmpty()) {
            checkError.putError("上传文件丢失");
            return null;
        }
        String dirPath = variableService.getValue(Variable.FILE_UPLOAD_DIR, null);
        if (StringUtils.isEmpty(dirPath)) {
            checkError.putError("上传文件夹路径未设置");
            return null;
        }
        // todo 大小限制？
        String MD5;
        try {
            MD5 = FileKit.MD5(file.getInputStream());
        } catch (NoSuchAlgorithmException | IOException e) {
            checkError.putError(e.getMessage());
            return null;
        }
        
        IFile ife = iFileRepository.findFirstByMd5(MD5);
        
        // 检测文件是否已经存在
        if (ObjectUtils.isEmpty(ife)
                || !ife.getFile().exists()) {
            try {
                file.transferTo(new File(dirPath, MD5));
            } catch (IOException e) {
                checkError.putError(e.getMessage());
                return null;
            }
        }
        
        IFile iFile = new IFile();
        iFile.setSrc(src.name());
        iFile.setUploadName(file.getOriginalFilename());
        iFile.setMd5(MD5);
        iFile.setSuffix(file.getContentType());
        iFile.setSize(file.getSize());
        iFile.setKeepPath(dirPath);
        iFile.setKeepName(MD5);
        iFile.createdAt();
        iFile.setAvailable(Boolean.TRUE);
        return iFileRepository.save(iFile);
    }
    
    @Override
    public IFile findById(String id) {
        return iFileRepository.findOne(id);
    }
    
    @Override
    public IFile findByKeepName(String keepName) {
        return iFileRepository.findByKeepName(keepName);
    }
    
    @Override
    public void delete(String... id) {
        iFileRepository.deleteAllByIdIn(id);
    }
    
    @Override
    public void updateAvailable(String id, boolean b) {
        IFile iFile = iFileRepository.findOne(id);
        if (!ObjectUtils.isEmpty(iFile)) {
            iFile.setAvailable(b);
            iFile.updatedAt();
            iFileRepository.save(iFile);
        }
    }
    
    /**
     * 清理
     * - 文件不存在的数据
     * - 失去引用的文件
     */
    @Override
    public void clear(boolean isRemoveFile) throws IOException {
        List<IFile> all = iFileRepository.findAll();
        List<String> keepList = all.stream()
                .map(IFile::getKeepName)
                .collect(Collectors.toList());
        String dirPath = variableService.getValue(Variable.FILE_UPLOAD_DIR, null);
        Set<String> fileList = Files.list(Paths.get(dirPath))
                .map(Path::getFileName)
                .map(Path::toString)
                .collect(Collectors.toSet());
        keepList.retainAll(fileList);
        iFileRepository.removeAllByKeepNameNotIn(keepList.toArray(new String[]{}));
        if (isRemoveFile) {
            fileList.removeAll(keepList);
            FileKit.removeIn(dirPath, fileList.toArray(new String[]{}));
        }
    }
}
