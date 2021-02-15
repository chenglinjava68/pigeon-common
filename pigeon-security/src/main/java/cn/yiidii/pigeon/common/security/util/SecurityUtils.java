package cn.yiidii.pigeon.common.security.util;

import cn.yiidii.pigeon.common.core.base.R;
import cn.yiidii.pigeon.common.core.util.SpringContextHolder;
import cn.yiidii.pigeon.common.security.service.PigeonUser;
import cn.yiidii.pigeon.rbac.api.dto.UserDTO;
import cn.yiidii.pigeon.rbac.api.feign.UserFeign;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 安全工具类
 *
 * @author L.cm
 */
@UtilityClass
public class SecurityUtils {

    private UserFeign userFeign = null;

    /**
     * 获取Authentication
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户
     */
    public PigeonUser getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof PigeonUser) {
            return (PigeonUser) principal;
        } else {
            // 通过用户名查询用户
            String username = authentication.getName();
            if (StringUtils.isNotBlank(username)) {
                if(Objects.isNull(userFeign)){
                    userFeign = SpringContextHolder.getBean(UserFeign.class);
                }
                R<UserDTO> userDTOByUsername = userFeign.getUserDTOByUsername(username);
                UserDTO userDTO = userDTOByUsername.getData();
                return PigeonUser.transPigeonUser(userDTO);
            } else {
                return null;
            }
        }

    }

    /**
     * 获取用户
     */
    public PigeonUser getUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return getUser(authentication);
    }

    /**
     * 获取用户角色信息
     *
     * @return 角色集合
     */
    public List<Integer> getRoles() {
        Authentication authentication = getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        List<Integer> roleIds = new ArrayList<>();
//		authorities.stream().filter(granted -> StrUtil.startWith(granted.getAuthority(), SecurityConstants.ROLE))
//				.forEach(granted -> {
//					String id = StrUtil.removePrefix(granted.getAuthority(), SecurityConstants.ROLE);
//					roleIds.add(Integer.parseInt(id));
//				});
        return roleIds;
    }

}
