package cn.yiidii.pigeon.common.security.support;

import cn.yiidii.pigeon.common.security.annotation.CurrentUser;
import cn.yiidii.pigeon.common.security.util.SecurityUtils;
import cn.yiidii.pigeon.rbac.api.entity.User;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author: YiiDii Wang
 * @create: 2021-03-06 20:37
 */
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean b = parameter.getParameterType().isAssignableFrom(User.class)
                && parameter.hasParameterAnnotation(CurrentUser.class);
        return b;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        return SecurityUtils.getUser();
    }
}
