package in.hocg.web.modules.system.service.impl;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.base.BaseService;
import in.hocg.web.modules.system.domain.IFile;
import in.hocg.web.modules.system.domain.MailTemplate;
import in.hocg.web.modules.system.domain.repository.MailTemplateRepository;
import in.hocg.web.modules.system.filter.MailTemplateDataTablesInputFilter;
import in.hocg.web.modules.system.filter.MailTemplateFilter;
import in.hocg.web.modules.system.service.IFileService;
import in.hocg.web.modules.system.service.MailTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * Created by hocgin on 2017/11/26.
 * email: hocgin@gmail.com
 */
@Service
public class MailTemplateServiceImpl extends BaseService implements MailTemplateService {
    
    private MailTemplateRepository mailTemplateRepository;
    private IFileService iFileService;
    @Autowired
    public MailTemplateServiceImpl(MailTemplateRepository mailTemplateRepository,
                                   IFileService iFileService) {
        this.mailTemplateRepository = mailTemplateRepository;
        this.iFileService = iFileService;
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
    
    
}
