package in.hocg.web.modules.system.service.impl;

import in.hocg.web.modules.system.filter.MenuFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.system.domain.SysMenu;
import in.hocg.web.modules.system.domain.repository.SysMenuRepository;
import in.hocg.web.modules.system.service.SysMenuService;
import in.hocg.web.modules.system.service.RoleService;
import in.hocg.web.modules.system.service.UserService;
import in.hocg.web.modules.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
public class SysMenuServiceImpl extends BaseService implements SysMenuService {
    private SysMenuRepository sysMenuRepository;
    private RoleService roleService;
    private UserService userService;
    
    @Autowired
    @Lazy
    public SysMenuServiceImpl(SysMenuRepository sysMenuRepository,
                              UserService userService,
                              RoleService roleService) {
        this.sysMenuRepository = sysMenuRepository;
        this.roleService = roleService;
        this.userService = userService;
    }
    
    @Override
    public void insert(MenuFilter filter, CheckError checkError) {
        SysMenu permission = filter.get();
        String path = "";
        
        // 检测上级权限是否存在
        if (!StringUtils.isEmpty(permission.getParent())) {
            SysMenu parent = sysMenuRepository.findOne(permission.getParent());
            if (ObjectUtils.isEmpty(parent)) {
                checkError.putError("上级权限不存在");
                return;
            }
            path = StringUtils.isEmpty(parent.getPath()) ? "" : parent.getPath();
            parent.setHasChildren(true);
            sysMenuRepository.save(parent);
        }
        
        // 检测权限标识是否已经存在
        if (!ObjectUtils.isEmpty(sysMenuRepository.findByPermission(permission.getPermission()))) {
            checkError.putError("该权限标识已存在");
            return;
        }
        
        permission.setPath(getSubPath(path));
        sysMenuRepository.save(permission);
    }
    
    @Override
    public void delete(String id) {
        SysMenu permission = sysMenuRepository.findOne(id);
        if (ObjectUtils.isEmpty(permission)) {
            return;
        }
        List<SysMenu> all = sysMenuRepository.findAllByPathRegex(String.format("%s.*", (StringUtils.isEmpty(permission.getPath()) ? "" : permission.getPath())));
        String[] ids = all
                .stream()
                .map(SysMenu::getId)
                .toArray(String[]::new);
        // 删除此权限 及 子权限
        sysMenuRepository.deleteAllByIdIn(ids);
        if (!StringUtils.isEmpty(permission.getParent())
                && sysMenuRepository.countByParent(permission.getParent()) < 1) { // 判断是否把父节点设置为根结点
            sysMenuRepository.updateHasChildren(permission.getParent(), false);
        }
        // 移除角色 与 此权限 及 子权限的关联
        roleService.removePermissionForAllRole(ids);
    }
    
    @Override
    public void update(MenuFilter filter, CheckError checkError) {
        SysMenu permission = sysMenuRepository.findOne(filter.getId());
        
        // 检测权限是否存在
        if (ObjectUtils.isEmpty(permission)) {
            checkError.putError("更新的权限异常");
            return;
        }
        
        // 检测权限标识是否已经存在
        if (!Objects.equals(filter.getPermission(), permission.getPermission())) {
            SysMenu byPermission = sysMenuRepository.findByPermission(filter.getPermission());
            if (!ObjectUtils.isEmpty(byPermission)
                    && Objects.equals(byPermission.getId(), permission.getId())) {
                checkError.putError("该权限标识已存在");
                return;
            }
        }
        
        sysMenuRepository.save(filter.update(permission));
    }
    
    @Override
    public List<SysMenu> queryChildren(String parentId) {
        return sysMenuRepository.findAllByParentIn(parentId);
    }
    
    @Override
    public List<SysMenu> queryRoot() {
        return sysMenuRepository.findAllByParentIn(null, "");
    }
    
    @Override
    public SysMenu findById(String id) {
        return sysMenuRepository.findOne(id);
    }
    
    @Override
    public List<SysMenu> queryById(String... id) {
        return sysMenuRepository.findAllByIdIn(id);
    }
    
    @Override
    public List<SysMenu> queryAllByIdOrderByPathAsc(String... id) {
        return sysMenuRepository.findAllByIdOrderByPathAsc(id);
    }
    
    @Override
    public void updateAvailable(String id, boolean b) {
        SysMenu permission = sysMenuRepository.findOne(id);
        if (!ObjectUtils.isEmpty(permission)) {
            permission.setAvailable(b);
            permission.updatedAt();
            sysMenuRepository.save(permission);
        }
    }
    
    @Override
    public void sort(String... ids) {
        sysMenuRepository.updateLocation(ids);
    }
    
    @Override
    public List<SysMenu> queryAllOrderByLocationAscAndPathAsc() {
        return sysMenuRepository.findAllOrderByLocationAscAndPathAsc();
    }
    
    /**
     * 获取子路径结构算法
     *
     * @param parentPath
     * @return
     */
    public String getSubPath(String parentPath) {
        List<SysMenu> all = sysMenuRepository.findAllByPathRegexOrderByPathDesc(String.format("^%s.{4}$", parentPath));
        String rsvalue = parentPath + "0001";
        if (all.size() > 0) {
            rsvalue = all.get(0).getPath();
            int newvalue = Integer.parseInt(rsvalue.substring(rsvalue.length() - 4)) + 1;
            rsvalue = rsvalue.substring(0, rsvalue.length() - 4)
                    + new java.text.DecimalFormat("0000")
                    .format(newvalue);
        }
        return rsvalue;
    }
}