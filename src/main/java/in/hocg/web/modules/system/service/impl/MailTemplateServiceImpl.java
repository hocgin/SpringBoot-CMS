package in.hocg.web.modules.system.service.impl;

import in.hocg.web.global.component.MailService;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.base.BaseService;
import in.hocg.web.modules.system.domain.IFile;
import in.hocg.web.modules.system.domain.MailTemplate;
import in.hocg.web.modules.system.domain.repository.MailTemplateRepository;
import in.hocg.web.modules.system.filter.MailTemplateDataTablesInputFilter;
import in.hocg.web.modules.system.filter.MailTemplateFilter;
import in.hocg.web.modules.system.filter.SendMailFilter;
import in.hocg.web.modules.system.filter.SendGroupMailFilter;
import in.hocg.web.modules.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    @Autowired
    public MailTemplateServiceImpl(MailTemplateRepository mailTemplateRepository,
                                   UserService userService,
                                   MailService mailService,
                                   SysLogService sysLogService,
                                   MemberService memberService,
                                   IFileService iFileService) {
        this.mailTemplateRepository = mailTemplateRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.memberService = memberService;
        this.iFileService = iFileService;
        this.sysLogService = sysLogService;
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
        IFile file = iFileService.findById(filter.getFid());
        if (ObjectUtils.isEmpty(file)
                || !file.exists()) {
            checkError.putError("模版文件丢失");
            return;
        }
        mailTemplate.setTemplate(file);
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
        mailTemplate.setTemplate(file);
        mailTemplateRepository.save(filter.update(mailTemplate));
    }
    
    /**
     * 群发邮件
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
        List<String> emailAll = new ArrayList<>();
        if (filter.isWeb()) {
            memberService.findAllByDepartmentAndRole(filter.getDepartment(), filter.getRole())
                    .forEach(member -> emailAll.add(member.getEmail()));
        } else if (filter.isAdmin()) {
            userService.findAllByDepartmentAndRole(filter.getDepartment(), filter.getRole())
                    .forEach(user -> emailAll.add(user.getEmail()));
        }
        try {
            mailService.sendUseThymeleafFile(emailAll,
                    template.getDefSubject(),
                    template.getTemplate().getPath(),
                    template.getParam(),
                    null, null);
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
        
        // 使用模版发送
        try {
            mailService.sendUseThymeleafFile(emailAll,
                    template.getDefSubject(),
                    template.getTemplate().getPath(),
                    template.getParam(),
                    null, null);
            sysLogService.aInfo("邮件模版指定发送", String.format("邮件模版(%s) 接收者 %s", id, Arrays.toString(emailAll.toArray())));
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
            checkError.putError("发送失败");
        }
    }
    
}
