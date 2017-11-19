package in.hocg.web.modules.system.domain.repository.custom;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
public interface RoleRepositoryCustom {
    /**
     * 从所有角色中移除指定权限
     * @param id
     */
    void removePermissionForAllRole(String... id);
}
