package cn.yiidii.pigeon.common.security.service;

import cn.yiidii.pigeon.common.core.base.R;
import cn.yiidii.pigeon.common.core.exception.BizException;
import cn.yiidii.pigeon.rbac.api.dto.ResourceDTO;
import cn.yiidii.pigeon.rbac.api.dto.UserDTO;
import cn.yiidii.pigeon.rbac.api.feign.UserFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: YiiDii Wang
 * @create: 2021-02-11 22:38
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PigeonUserDetailsService implements UserDetailsService {

    private final UserFeign userFeign;

    @PostConstruct
    public void init() {
        log.info("PigeonUserDetailsService init...");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername username: {}", username);
        R<UserDTO> result = userFeign.getUserDTOByUsername(username);
        UserDTO userDTO = result.getData();
        if (Objects.isNull(userDTO)) {
            throw new BizException("用户不存在");
        }

        return getUserDetails(result);
    }

    /**
     * 构建userdetails
     *
     * @param result 用户信息
     * @return
     */
    private UserDetails getUserDetails(R<UserDTO> result) {
        if (result == null || result.getData() == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        UserDTO userDTO = result.getData();
        //根据用户的id查询用户的权限
        List<ResourceDTO> resourceDTOs = userDTO.getResources();
        List<String> resources = resourceDTOs.stream().map(ResourceDTO::getCode).collect(Collectors.toList());
        //将permissions转成数组
        String[] permissionArray = new String[resources.size()];
        resources.toArray(permissionArray);
        // 构造security用户
        return new PigeonUser(userDTO.getId(), userDTO.getUsername(), userDTO.getPassword(),
                true, true, true, true, AuthorityUtils.createAuthorityList(permissionArray));
    }
}
