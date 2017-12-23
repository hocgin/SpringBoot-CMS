package in.hocg.web.modules.system.service.impl;

import in.hocg.web.global.component.MailService;
import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.utils.RequestKit;
import in.hocg.web.lang.utils.SecurityKit;
import in.hocg.web.modules.security.details.IUser;
import in.hocg.web.modules.system.domain.MailVerify;
import in.hocg.web.modules.system.domain.user.Member;
import in.hocg.web.modules.system.domain.Role;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.domain.Variable;
import in.hocg.web.modules.system.domain.repository.UserRepository;
import in.hocg.web.modules.system.filter.MemberDataTablesInputFilter;
import in.hocg.web.modules.system.filter.MemberFilter;
import in.hocg.web.modules.system.service.MailVerifyService;
import in.hocg.web.modules.system.service.MemberService;
import in.hocg.web.modules.system.service.RoleService;
import in.hocg.web.modules.system.service.VariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 * 会员
 */
@Service
public class MemberServiceImpl implements MemberService {
    private UserRepository memberRepository;
    private RoleService roleService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private HttpServletRequest request;
    private MailService mailService;
    private VariableService variableService;
    private MailVerifyService mailVerifyService;
    
    @Autowired
    MemberServiceImpl(UserRepository memberRepository,
                      RoleService roleService,
                      MailService mailService,
                      MailVerifyService mailVerifyService,
                      VariableService variableService,
                      BCryptPasswordEncoder bCryptPasswordEncoder,
                      HttpServletRequest request) {
        this.memberRepository = memberRepository;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.request = request;
        this.mailService = mailService;
        this.mailVerifyService = mailVerifyService;
        this.variableService = variableService;
    }
    
    @Override
    public List<User> findAll() {
        return memberRepository.findAll();
    }
    
    
    /**
     * 1. 三天内点击有效
     *
     * @param id
     * @param checkError
     */
    @Override
    public void verifyMail(String id, CheckError checkError) {
        MailVerify mailVerify = mailVerifyService.findOne(id);
        if (ObjectUtils.isEmpty(mailVerify)) {
            checkError.putError("校验异常");
            return;
        }
        if (mailVerifyService.verify(mailVerify, checkError)) {
            User member = mailVerify.getUser();
            member.setVerifyEmailAt(new Date());
            member.setIsVerifyEmail(true);
            member.setToken(Member.Token.gen(member.getId())); // 分配 Token
            memberRepository.save(member);
        }
    }
    
    @Override
    public void sendVerifyEmail(String id, CheckError checkError) {
        User member = memberRepository.findOne(id);
        if (ObjectUtils.isEmpty(member)) {
            checkError.putError("会员异常");
            return;
        }
        if (member.getIsVerifyEmail()) {
            checkError.putError("已经校验过了");
            return;
        }
        sendVerifyEmail(member);
    }
    
    @Override
    public User findOneByToken(String token) {
        return memberRepository.findOneByTokenForMember(token);
    }
    
    @Override
    public void updateTokenAvailable(String id, boolean available) {
        User member = memberRepository.findOne(id);
        if (!ObjectUtils.isEmpty(member)) {
            Member.Token token = member.getToken();
            if (!ObjectUtils.isEmpty(token)) {
                token.setAvailable(available);
                memberRepository.save(member);
            }
        }
    }
    
    @Override
    public void resumeToken() {
        memberRepository.resumeTokenForMember();
    }
    
    /**
     * @param roleIds
     * @return
     */
    @Override
    public List<User> findAllByRoles(String... roleIds) {
        return memberRepository.findAllByRole(roleIds);
    }
    
    @Override
    public User findByEmail(String email) {
        return memberRepository.findByEmailForMember(email);
    }
    
    @Override
    public void resetPassword(String mail, CheckError checkError) {
        User member = memberRepository.findByEmailForMember(mail);
        if (ObjectUtils.isEmpty(member)) {
            checkError.putError("邮箱未被注册");
            return;
        }
        if (!member.getIsVerifyEmail()) {
            checkError.putError("邮箱未验证");
            return;
        }
        MailVerify mailVerify = mailVerifyService.create(member);
        // 邮箱认证
        Map<String, Object> params = new HashMap<>();
        params.put("nickname", member.getNickname());
        params.put("restPasswordUrl", String.format("%s/set-new-password.html?id=%s",
                variableService.getValue(Variable.HOST, "http://127.0.0.1:8080"),
                mailVerify.getId()));
        try {
            mailService.sendUseTemplate(member.getEmail(), String.format("重置密码 (%s)",
                    member.getNickname()),
                    "reset-password",
                    params, null, null);
        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void setNewPassword(String id, String newPassword, CheckError checkError) {
        MailVerify mailVerify = mailVerifyService.findOne(id);
        if (ObjectUtils.isEmpty(mailVerify)) {
            checkError.putError("校验异常");
            return;
        }
        if (mailVerifyService.verify(mailVerify, checkError)) {
            User member = mailVerify.getUser();
            member.setPassword(bCryptPasswordEncoder.encode(newPassword));
            member.updatedAt();
            memberRepository.save(member);
            mailVerify.setVerified(true);
            
        }
    }
    
    @Override
    public String toggleToken(CheckError checkError) {
        IUser iUser = SecurityKit.iUser();
        User member = memberRepository.findOne(iUser.getId());
        
        Member.Token token = member.getToken();
        if (ObjectUtils.isEmpty(member) || ObjectUtils.isEmpty(token)) {
            checkError.putError("异常");
            return null;
        }
        token.setToken(Member.Token.genToken(member.getId()));
        member.setToken(token);
        member.updatedAt();
        memberRepository.save(member);
        return token.getToken();
    }
    
    
    @Override
    public DataTablesOutput<User> data(MemberDataTablesInputFilter input) {
        Criteria criteria = new Criteria();
        List<Criteria> andOperators = new ArrayList<>();
        if (!StringUtils.isEmpty(input.getRegexEmail())) {
            andOperators.add(Criteria.where("email").regex(String.format(".*%s.*", input.getRegexEmail())));
        }
        if (!StringUtils.isEmpty(input.getRegexNickname())) {
            andOperators.add(Criteria.where("nickname").regex(String.format(".*%s.*", input.getRegexNickname())));
        }
        if (!ObjectUtils.isEmpty(input.getIds())) {
            andOperators.add(Criteria.where("_id").in(input.getIds()));
        }
        if (!ObjectUtils.isEmpty(input.getNoIds())) {
            andOperators.add(Criteria.where("_id").nin(input.getNoIds()));
        }
        andOperators.add(Criteria.where("type").is(User.Type.Member.getCode()));
    
        criteria.andOperator(andOperators.toArray(new Criteria[]{}));
        DataTablesOutput<User> all = memberRepository.findAll(input, criteria);
        all.setDraw(0);
        return all;
    }
    
    @Override
    public void delete(CheckError checkError, String... id) {
        memberRepository.deleteAllByIdInAndTypeIs(id, User.Type.Member.getCode());
    }
    
    @Override
    public void insert(MemberFilter filter, CheckError checkError) {
        User member = filter.get();
        
        // 检测用户名是否已被使用
        if (Objects.nonNull(memberRepository.findOneByEmail(filter.getEmail()))) {
            checkError.putError("邮箱已被注册");
            return;
        }
        if (Objects.nonNull(memberRepository.findOneByUsername(member.getUsername()))) {
            checkError.putError("用户名已被注册");
            return;
        }
        
        member.setIsVerifyEmail(false);
        member.setRole(Collections.singleton(roleService.findByRole(Role.ROLE_USER)));
        member.setSignUpIP(RequestKit.getClientIP(request));
        // 密码加密
        member.setPassword(bCryptPasswordEncoder.encode(filter.getPassword()));
        member = memberRepository.insert(member);
        // 验证邮箱
        sendVerifyEmail(member);
        
    }
    
    @Override
    public void updateAvailable(String id, boolean b) {
        User member = memberRepository.findOne(id);
        if (!ObjectUtils.isEmpty(member)) {
            member.setAvailable(b);
            memberRepository.save(member);
        }
    }
    
    @Override
    public User find(String id) {
        return memberRepository.findOne(id);
    }
    
    @Override
    public List<User> findAllById(String... ids) {
        return memberRepository.findAllByIdInAndTypeIs(ids, User.Type.Member.getCode());
    }
    
    @Override
    public void update(MemberFilter filter, CheckError checkError) {
        
        User member = memberRepository.findOne(filter.getId());
        if (ObjectUtils.isEmpty(member)) {
            checkError.putError("会员不存在");
            return;
        }
        
        memberRepository.save(filter.update(member));
    }
    
    @Override
    public void update(User member) {
        memberRepository.save(member);
    }
    
    /**
     * 发送校验邮件
     * - _todo 后期迁移出内置邮箱模版
     *
     * @param member
     */
    private void sendVerifyEmail(User member) {
        MailVerify mailVerify = mailVerifyService.create(member);
        // 邮箱认证
        Map<String, Object> params = new HashMap<>();
        params.put("verifyUrl", String.format("%s/public/verify-mail.html?id=%s",
                variableService.getValue(Variable.HOST, "http://127.0.0.1:8080"),
                mailVerify.getId()));
        try {
            mailService.sendUseTemplate(member.getEmail(), String.format("邮箱验证 (%s)", member.getNickname()), "verify-email",
                    params, null, null);
        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
        }
    }
}
