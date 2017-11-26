package in.hocg.web.modules.system.service.impl;

import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.utils.RequestKit;
import in.hocg.web.modules.system.domain.Member;
import in.hocg.web.modules.system.domain.Role;
import in.hocg.web.modules.system.domain.SysMenu;
import in.hocg.web.modules.system.domain.repository.MemberRepository;
import in.hocg.web.modules.system.filter.MemberDataTablesInputFilter;
import in.hocg.web.modules.system.filter.MemberFilter;
import in.hocg.web.modules.system.service.DepartmentService;
import in.hocg.web.modules.system.service.MemberService;
import in.hocg.web.modules.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 */
@Service
public class MemberServiceImpl implements MemberService {
    private MemberRepository memberRepository;
    private DepartmentService departmentService;
    private RoleService roleService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private HttpServletRequest request;
    
    @Autowired
    MemberServiceImpl(MemberRepository memberRepository,
                      RoleService roleService,
                      BCryptPasswordEncoder bCryptPasswordEncoder,
                      DepartmentService departmentService,
                      HttpServletRequest request) {
        this.memberRepository = memberRepository;
        this.departmentService = departmentService;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.request = request;
    }
    
    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }
    
    @Override
    public Collection<Role> findRoleByMember(String id) {
        Member member = memberRepository.findOne(id);
        if (ObjectUtils.isEmpty(member)) {
            return Collections.emptyList();
        }
        return member.getRole();
    }
    
    @Override
    public Collection<SysMenu> findSysMenuByMember(String id) {
        Collection<Role> roles = findRoleByMember(id);
        // 一般角色处理
        HashSet<SysMenu> menus = new HashSet<>();
        roles.forEach(role -> {
            Collection<SysMenu> menuCollection = role.getPermissions();
            if (!CollectionUtils.isEmpty(menuCollection)) {
                menus.addAll(menuCollection);
            }
        });
        return menus.stream()
                .sorted(Comparator.comparing(SysMenu::getLocation))
                .collect(Collectors.toList());
    }
    
    
    @Override
    public DataTablesOutput<Member> data(MemberDataTablesInputFilter input) {
        Criteria criteria = new Criteria();
        if (!StringUtils.isEmpty(input.getRegexEmail())) {
            criteria.andOperator(Criteria.where("email").regex(String.format(".*%s.*", input.getRegexEmail())));
        }
        if (!StringUtils.isEmpty(input.getRegexNickname())) {
            criteria.andOperator(Criteria.where("nickname").regex(String.format(".*%s.*", input.getRegexNickname())));
        }
        DataTablesOutput<Member> all = memberRepository.findAll(input, criteria);
        all.setDraw(0);
        return all;
    }
    
    @Override
    public void delete(CheckError checkError, String... id) {
        memberRepository.deleteAllByIdIn(id);
    }
    
    @Override
    public void insert(MemberFilter filter, CheckError checkError) {
        Member member = filter.get();
        
        // 检测用户名是否已被使用
        if (!ObjectUtils.isEmpty(memberRepository.findByEmail(filter.getEmail()))) {
            checkError.putError("账号已存在");
            return;
        }
        
        member.setToken(Member.Token.gen(memberRepository.count())); // 分配 Token
        member.setIsVerifyEmail(false);
        member.setRole(Collections.singleton(roleService.findByRole(Role.ROLE_USER)));
        member.setSignUpIP(RequestKit.getClientIP(request));
        // 密码加密
        member.setPassword(bCryptPasswordEncoder.encode(filter.getPassword()));
        memberRepository.save(member);
    }
    
    @Override
    public void updateAvailable(String id, boolean b) {
        Member member = memberRepository.findOne(id);
        if (!ObjectUtils.isEmpty(member)) {
            member.setAvailable(b);
            memberRepository.save(member);
        }
    }
    
    @Override
    public Member find(String id) {
        return memberRepository.findOne(id);
    }
    
    @Override
    public List<Member> findAllById(String... ids) {
        return memberRepository.findAllByIdIn(ids);
    }
    
    @Override
    public void update(MemberFilter filter, CheckError checkError) {
    
        Member member = memberRepository.findOne(filter.getId());
        if (ObjectUtils.isEmpty(member)) {
            checkError.putError("会员不存在");
            return;
        }
        
        memberRepository.save(filter.update(member));
    }
    
    @Override
    public void update(Member member) {
        memberRepository.save(member);
    }
}
