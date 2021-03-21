
package cn.yiidii.pigeon.common.security.service;

import cn.yiidii.pigeon.rbac.api.dto.MenuDTO;
import cn.yiidii.pigeon.rbac.api.dto.PermissionDTO;
import cn.yiidii.pigeon.rbac.api.dto.UserDTO;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2019/2/1 扩展用户信息
 */
public class PigeonUser extends User {

    /**
     * 用户ID
     */
    @Getter
    private Long id;

    public PigeonUser(Long id, String username, String password, boolean enabled,
                      boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                      Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }

    public static PigeonUser transPigeonUser(UserDTO userDTO) {
        // 将permissions转成数组
        List<PermissionDTO> permissions = userDTO.getPermissions();
        Set<String> permissionSet = permissions.stream().map(PermissionDTO::getCode).collect(Collectors.toSet());
        String[] permissionArray = permissionSet.toArray(new String[permissionSet.size()]);

        return new PigeonUser(userDTO.getId(), userDTO.getUsername(), userDTO.getPassword(),
                true, true, true, true, AuthorityUtils.createAuthorityList(permissionArray));
    }

}
