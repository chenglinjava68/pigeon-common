package cn.yiidii.pigeon.common.security.service;

import cn.hutool.core.util.StrUtil;
import cn.yiidii.pigeon.common.core.base.R;
import cn.yiidii.pigeon.common.core.base.enumeration.Status;
import cn.yiidii.pigeon.common.core.exception.BizException;
import cn.yiidii.pigeon.rbac.api.dto.UserDTO;
import cn.yiidii.pigeon.rbac.api.feign.UserFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;

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
        log.info("=== security === PigeonUserDetailsService init...");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername username: {}", username);
        R<UserDTO> result = userFeign.getUserDTOByUsername(username);
        UserDTO userDTO = result.getData();
        if (Objects.isNull(userDTO) || userDTO.getStatus().equals(Status.DELETED)) {
            throw new BizException(StrUtil.format("用户[{}]不存在", username));
        }
        if (userDTO.getStatus().equals(Status.DISABLED)) {
            throw new BizException("账户已被禁用");
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
        return PigeonUser.transPigeonUser(userDTO);
    }
}
