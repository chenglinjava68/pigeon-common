package cn.yiidii.pigeon.common.security.service;

import cn.hutool.core.util.StrUtil;
import cn.yiidii.pigeon.common.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * @author lengleng
 * @date 2019/2/1 接口权限判断工具
 */
@Slf4j
@Component("pms")
public class PermissionService {

    /**
     * 判断接口是否有xxx:xxx权限
     *
     * @param permission 权限
     * @return {boolean}
     */
    public boolean hasPermission(String permission) {
        if (StrUtil.isBlank(permission)) {
            return false;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean hasPermission = authorities.stream().map(GrantedAuthority::getAuthority).filter(StringUtils::hasText)
                .anyMatch(x -> PatternMatchUtils.simpleMatch(permission, x));
        PigeonUser user = SecurityUtils.getUser(authentication);
        log.info("用户[{}] {}权限[{}]", user.getUsername(), hasPermission ? "有" : "没有", permission);
        return hasPermission;
    }

}
