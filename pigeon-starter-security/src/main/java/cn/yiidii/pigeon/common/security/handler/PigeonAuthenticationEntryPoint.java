package cn.yiidii.pigeon.common.security.handler;

import cn.yiidii.pigeon.common.core.base.R;
import cn.yiidii.pigeon.common.core.exception.code.ExceptionCode;
import cn.yiidii.pigeon.common.core.util.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 匿名用户访问无权限资源时的异常
 *
 * @author: YiiDii Wang
 * @create: 2021-02-15 17:28
 */
public class PigeonAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        R result = R.failed(ExceptionCode.JWT_NOT_LOGIN.getCode(), ExceptionCode.JWT_NOT_LOGIN.getMsg());
        WebUtils.renderJson(httpServletResponse, result);
    }

}
