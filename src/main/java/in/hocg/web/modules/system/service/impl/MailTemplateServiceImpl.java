package in.hocg.web.modules.system.service.impl;

import in.hocg.web.global.component.MailService;
import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.utils.FileKit;
import in.hocg.web.modules.base.BaseService;
import in.hocg.web.modules.system.domain.*;
import in.hocg.web.modules.system.domain.repository.MailTemplateRepository;
import in.hocg.web.modules.system.filter.MailTemplateDataTablesInputFilter;
import in.hocg.web.modules.system.filter.MailTemplateFilter;
import in.hocg.web.modules.system.filter.SendGroupMailFilter;
import in.hocg.web.modules.system.filter.SendMailFilter;
import in.hocg.web.modules.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2017/11/26.
 * email: hocgin@gmail.com
 */
@Service
public class MailTemplateServiceImpl extends BaseService implements MailTemplateService {
    
    private MailTemplateRepository mailTemplateRepository;
    private IFileService iFileService;
    private UserService userService;
    private MemberService memberService;
    private MailService mailService;
    private SysLogService sysLogService;
    private DepartmentService departmentService;
    private RoleService roleService;
    
    @Autowired
    public MailTemplateServiceImpl(MailTemplateRepository mailTemplateRepository,
                                   UserService userService,
                                   MailService mailService,
                                   RoleService roleService,
                                   DepartmentService departmentService,
                                   SysLogService sysLogService,
                                   MemberService memberService,
                                   IFileService iFileService) {
        this.mailTemplateRepository = mailTemplateRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.departmentService = departmentService;
        this.memberService = memberService;
        this.iFileService = iFileService;
        this.sysLogService = sysLogService;
        this.roleService = roleService;
    }
    
    @Override
    public DataTablesOutput<MailTemplate> data(MailTemplateDataTablesInputFilter filter) {
        Criteria criteria = new Criteria();
        if (!StringUtils.isEmpty(filter.getRegexSearch())) {
            criteria.orOperator(
                    Criteria.where("name").regex(String.format(".*%s.*", filter.getRegexSearch())),
                    Criteria.where("defSubject").regex(String.format(".*%s.*", filter.getRegexSearch())),
                    Criteria.where("description").regex(String.format(".*%s.*", filter.getRegexSearch())),
                    Criteria.where("template.keepName").regex(String.format(".*%s.*", filter.getRegexSearch()))
            );
        }
        return mailTemplateRepository.findAll(filter, criteria);
    }
    
    @Override
    public void delete(CheckError checkError, String... id) {
        mailTemplateRepository.deleteAllByIdIn(id);
    }
    
    @Override
    public void insert(MailTemplateFilter filter, CheckError checkError) {
        MailTemplate mailTemplate = filter.get();
        if (!ObjectUtils.isEmpty(mailTemplateRepository.findFirstByName(filter.getName()))) {
            checkError.putError("模版名称已存在");
            return;
        }
        // 处理模版文件
        IFile file = iFileService.findById(filter.getFid());
        if (ObjectUtils.isEmpty(file)
                || !file.exists()) {
            checkError.putError("模版文件丢失");
            return;
        }
        
        try {
            mailTemplate.setTemplateString(FileKit.read(file.getFile()));
        } catch (IOException e) {
            e.printStackTrace();
            checkError.putError("读取文件出现异常");
            return;
        }
        // 处理图片及UID
        HashMap<String, IFile> imageMap = new HashMap<>();
        iFileService.findByIdIn(filter.getImagesId()).forEach(image -> {
            if (image.exists()) {
                imageMap.put(image.getUploadName(), image);
            }
        });
        mailTemplate.setImages(imageMap);
    
        // 处理附件
        HashMap<String, IFile> fileMap = new HashMap<>();
        iFileService.findByIdIn(filter.getFilesId()).forEach(f -> {
            if (f.exists()) {
                fileMap.put(f.getUploadName(), f);
            }
        });
        mailTemplate.setFiles(fileMap);
        mailTemplateRepository.insert(mailTemplate);
    }
    
    @Override
    public MailTemplate find(String id) {
        return mailTemplateRepository.findOne(id);
    }
    
    @Override
    public void update(MailTemplateFilter filter, CheckError checkError) {
        MailTemplate mailTemplate = mailTemplateRepository.findOne(filter.getId());
        if (ObjectUtils.isEmpty(mailTemplate)) {
            checkError.putError("邮件模版不存在");
            return;
        }
        IFile file = iFileService.findById(filter.getFid());
        if (ObjectUtils.isEmpty(file) || !file.exists()) {
            checkError.putError("邮件模版文件不存在");
            return;
        }
        try {
            mailTemplate.setTemplateString(FileKit.read(file.getFile()));
        } catch (IOException e) {
            e.printStackTrace();
            checkError.putError("读取文件出现异常");
            return;
        }
        mailTemplateRepository.save(filter.update(mailTemplate));
    }
    
    /**
     * 群发邮件
     * - 角色为最细颗粒
     *
     * @param id
     * @param filter
     * @param checkError
     */
    @Override
    public void sendGroup(String id, SendGroupMailFilter filter, CheckError checkError) {
        MailTemplate template = mailTemplateRepository.findOne(id);
        if (ObjectUtils.isEmpty(template)) {
            checkError.putError("邮件模版异常");
            return;
        }
        
        String[] roles;
        // 提取角色列表
        if (StringUtils.isEmpty(filter.getRole())) {
            List<Role> roleList;
            if (StringUtils.isEmpty(filter.getDepartment())) {
                roleList = roleService.findAll();
            } else {
                roleList = roleService.findByDepartmentAndChildren(filter.getDepartment());
            }
            roles = roleList.stream()
                    .map(Role::getId)
                    .toArray(String[]::new);
        } else {
            roles = new String[]{filter.getRole()};
        }
        
        List<String> emailAll;
        // 提取邮箱地址
        if (filter.isWeb()) {
            emailAll = memberService.findAllByRoles(roles)
                    .stream()
                    .map(Member::getEmail)
                    .collect(Collectors.toList());
            
        } else if (filter.isAdmin()) {
            emailAll = userService.findAllByRoles(roles)
                    .stream()
                    .map(User::getEmail)
                    .collect(Collectors.toList());
        } else {
            emailAll = Collections.emptyList();
        }
        
        if (emailAll.isEmpty()) {
            checkError.putError("未匹配到接收人");
            return;
        }
        // 处理图片及UID
        HashMap<String, File> imageMap = new HashMap<>();
        iFileService.findByIdIn(filter.getImagesId()).forEach(image -> {
            if (image.exists()) {
                imageMap.put(image.getUploadName(), image.getFile());
            }
        });
        
        // 处理附件
        HashMap<String, File> fileMap = new HashMap<>();
        iFileService.findByIdIn(filter.getFilesId()).forEach(file -> {
            if (file.exists()) {
                fileMap.put(file.getUploadName(), file.getFile());
            }
        });
        
        // 发送
        try {
            mailService.sendUseThymeleafText(emailAll.toArray(new String[]{}),
                    filter.getDefSubject(),
                    template.getTemplateString(),
                    filter.getParams(),
                    imageMap, fileMap);
            sysLogService.aInfo("邮件模版群发", String.format("邮件模版(%s) 接收者 %s", id, Arrays.toString(emailAll.toArray())));
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
            checkError.putError("发送失败");
        }
    }
    
    
    @Override
    public void send(String id, SendMailFilter filter, CheckError checkError) {
        MailTemplate template = mailTemplateRepository.findOne(id);
        if (ObjectUtils.isEmpty(template)) {
            checkError.putError("邮件模版异常");
            return;
        }
        List<String> emailAll = new ArrayList<>();
        if (filter.isWeb()) {
            memberService.findAllById(filter.getIds())
                    .forEach(member -> emailAll.add(member.getEmail()));
        } else if (filter.isAdmin()) {
            userService.findAllById(filter.getIds())
                    .forEach(user -> emailAll.add(user.getEmail()));
        }
        // 处理图片及UID
        HashMap<String, File> imageMap = new HashMap<>();
        iFileService.findByIdIn(filter.getImagesId()).forEach(image -> {
            if (image.exists()) {
                imageMap.put(image.getUploadName(), image.getFile());
            }
        });
    
        // 处理附件
        HashMap<String, File> fileMap = new HashMap<>();
        iFileService.findByIdIn(filter.getFilesId()).forEach(file -> {
            if (file.exists()) {
                fileMap.put(file.getUploadName(), file.getFile());
            }
        });
        
        // 发送
        try {
            mailService.sendUseThymeleafText(emailAll.toArray(new String[]{}),
                    filter.getDefSubject(),
                    template.getTemplateString(),
                    filter.getParams(),
                    imageMap, fileMap);
            sysLogService.aInfo("邮件模版指定发送", String.format("邮件模版(%s) 接收者 %s", id, Arrays.toString(emailAll.toArray())));
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
            checkError.putError("发送失败");
        }
    }
    
}
