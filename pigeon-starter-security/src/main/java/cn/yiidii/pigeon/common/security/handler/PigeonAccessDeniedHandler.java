package cn.yiidii.pigeon.common.security.handler;

import cn.hutool.http.HttpStatus;
import cn.yiidii.pigeon.common.core.base.R;
import cn.yiidii.pigeon.common.core.constant.CommonConstants;
import cn.yiidii.pigeon.common.core.exception.code.ExceptionCode;
import cn.yiidii.pigeon.common.core.util.WebUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证过的用户访问无权限资源时的异常
 *
 * @author: YiiDii Wang
 * @create: 2021-02-15 17:02
 */
@Slf4j
public class PigeonAccessDeniedHandler extends OAuth2AccessDeniedHandler {

    @PostConstruct
    public void init() {
        log.info("===== pigeon AccessDeniedHandler init");
    }

    /**
     * 授权拒绝处理，使用R包装
     * @param request request
     * @param response response
     * @param authException authException
     */
    @Override
    @SneakyThrows
    public void handle(HttpServletRequest request, HttpServletResponse response, org.springframework.security.access.AccessDeniedException authException) {
        log.info("授权失败，禁止访问 {}", request.getRequestURI());
        response.setCharacterEncoding(CommonConstants.UTF8);
        response.setContentType(CommonConstants.CONTENT_TYPE);
        response.setStatus(HttpStatus.HTTP_FORBIDDEN);
        R result = R.failed(ExceptionCode.UNAUTHORIZED.getCode(), ExceptionCode.UNAUTHORIZED.getMsg());
        WebUtils.renderJson(response, result);
    }

}
