
package cn.yiidii.pigeon.common.security.service;

import cn.yiidii.pigeon.rbac.api.dto.ResourceDTO;
import cn.yiidii.pigeon.rbac.api.dto.UserDTO;
import lombok.Getter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;
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
        //根据用户的id查询用户的权限
        List<ResourceDTO> resourceDTOs = userDTO.getResources();
        List<String> resources = resourceDTOs.stream().map(ResourceDTO::getCode).collect(Collectors.toList());
        //将permissions转成数组
        String[] permissionArray = new String[resources.size()];
        resources.toArray(permissionArray);

        return new PigeonUser(userDTO.getId(), userDTO.getUsername(), userDTO.getPassword(),
                true, true, true, true, AuthorityUtils.createAuthorityList(permissionArray));
    }

}
