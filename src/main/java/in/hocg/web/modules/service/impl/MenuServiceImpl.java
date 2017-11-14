package in.hocg.web.modules.service.impl;

import in.hocg.web.filter.MenuFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.domain.Menu;
import in.hocg.web.modules.domain.repository.MenuRepository;
import in.hocg.web.modules.service.MenuService;
import in.hocg.web.modules.service.RoleService;
import in.hocg.web.modules.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
public class MenuServiceImpl extends BaseService implements MenuService {
    private MenuRepository menuRepository;
    private RoleService roleService;
    private UserService userService;
    
    @Autowired
    @Lazy
    public MenuServiceImpl(MenuRepository menuRepository,
                           UserService userService,
                           RoleService roleService) {
        this.menuRepository = menuRepository;
        this.roleService = roleService;
        this.userService = userService;
    }
    
    @Override
    public void insert(MenuFilter filter, CheckError checkError) {
        Menu permission = filter.get();
        String path = "";
        
        // 检测上级权限是否存在
        if (!StringUtils.isEmpty(permission.getParent())) {
            Menu parent = menuRepository.findOne(permission.getParent());
            if (ObjectUtils.isEmpty(parent)) {
                checkError.putError("上级权限不存在");
                return;
            }
            path = StringUtils.isEmpty(parent.getPath()) ? "" : parent.getPath();
            parent.setHasChildren(true);
            menuRepository.save(parent);
        }
        
        // 检测权限标识是否已经存在
        if (!ObjectUtils.isEmpty(menuRepository.findByPermission(permission.getPermission()))) {
            checkError.putError("该权限标识已存在");
            return;
        }
        
        permission.setPath(getSubPath(path));
        menuRepository.save(permission);
    }
    
    @Override
    public void delete(String id) {
        Menu permission = menuRepository.findOne(id);
        if (ObjectUtils.isEmpty(permission)) {
            return;
        }
        List<Menu> all = menuRepository.findAllByPathRegex(String.format("%s.*", (StringUtils.isEmpty(permission.getPath()) ? "" : permission.getPath())));
        String[] ids = all
                .stream()
                .map(Menu::getId)
                .toArray(String[]::new);
        // 删除此权限 及 子权限
        menuRepository.deleteAllByIdIn(ids);
        if (!StringUtils.isEmpty(permission.getParent())
                && menuRepository.countByParent(permission.getParent()) < 1) { // 判断是否把父节点设置为根结点
            menuRepository.updateHasChildren(permission.getParent(), false);
        }
        // 移除角色 与 此权限 及 子权限的关联
        roleService.removePermissionForAllRole(ids);
    }
    
    @Override
    public void update(MenuFilter filter, CheckError checkError) {
        Menu permission = menuRepository.findOne(filter.getId());
        
        // 检测权限是否存在
        if (ObjectUtils.isEmpty(permission)) {
            checkError.putError("更新的权限异常");
            return;
        }
        
        // 检测权限标识是否已经存在
        if (!Objects.equals(filter.getPermission(), permission.getPermission())) {
            Menu byPermission = menuRepository.findByPermission(filter.getPermission());
            if (!ObjectUtils.isEmpty(byPermission)
                    && Objects.equals(byPermission.getId(), permission.getId())) {
                checkError.putError("该权限标识已存在");
                return;
            }
        }
        
        menuRepository.save(filter.update(permission));
    }
    
    @Override
    public List<Menu> queryChildren(String parentId) {
        return menuRepository.findAllByParentIn(parentId);
    }
    
    @Override
    public List<Menu> queryRoot() {
        return menuRepository.findAllByParentIn(null, "");
    }
    
    @Override
    public Menu findById(String id) {
        return menuRepository.findOne(id);
    }
    
    @Override
    public List<Menu> queryById(String... id) {
        return menuRepository.findAllByIdIn(id);
    }
    
    @Override
    public List<Menu> queryAllByIdOrderByPathAes(String... id) {
        return menuRepository.findAllByIdOrderByPathAes(id);
    }
    
    @Override
    public void updateAvailable(String id, boolean b) {
        Menu permission = menuRepository.findOne(id);
        if (!ObjectUtils.isEmpty(permission)) {
            permission.setAvailable(b);
            permission.updatedAt();
            menuRepository.save(permission);
        }
    }
    
    /**
     * 获取子路径结构算法
     *
     * @param parentPath
     * @return
     */
    public String getSubPath(String parentPath) {
        List<Menu> all = menuRepository.findAllByPathRegexOrderByPathDesc(parentPath + ".{4}");
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