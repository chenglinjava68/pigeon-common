package cn.yiidii.pigeon.common.feign.config;

import cn.yiidii.pigeon.common.core.constant.ContextConstants;
import cn.yiidii.pigeon.common.core.util.TraceUtil;
import feign.RequestInterceptor;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * feign拦截器
 *
 * @author pangu
 * @date 2020-9-9
 */
@Slf4j
public class FeignInterceptorConfiguration {

    /**
     * 使用feign client发送请求时，传递tenantId和traceId
     *
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            //传递日志traceId
            String traceId = MDC.get(ContextConstants.LOG_TRACE_ID);
            if (StringUtils.isBlank(traceId)) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    Enumeration<String> headerNames = request.getHeaderNames();
                    if (headerNames != null) {
                        String headerName = null;
                        while (headerNames.hasMoreElements()) {
                            headerName = headerNames.nextElement();
                            if (headerName.equalsIgnoreCase(ContextConstants.LOG_TRACE_ID)) {
                                traceId = request.getHeader(headerName);
                                requestTemplate.header(ContextConstants.LOG_TRACE_ID, traceId);
                                TraceUtil.mdcTraceId(traceId);
                            }
                            String values = request.getHeader(headerName);
                            requestTemplate.header(headerName, values);
                        }
                    }
                }
            } else {
                if (StringUtils.isNotBlank(traceId)) {
                    requestTemplate.header(ContextConstants.LOG_TRACE_ID, traceId);
                }
            }
        };
    }
}
