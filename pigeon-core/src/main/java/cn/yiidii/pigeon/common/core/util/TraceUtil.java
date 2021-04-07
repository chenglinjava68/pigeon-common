package cn.yiidii.pigeon.common.core.util;

import cn.yiidii.pigeon.common.core.constant.ContextConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;

/**
 * 链路追踪工具类
 *
 * @author xuzhanfu
 */
public class TraceUtil {

    /**
     * 从header和参数中获取traceId
     * 从前端传入数据
     *
     * @param request 　HttpServletRequest
     * @return traceId
     */
    public static String getTraceId(HttpServletRequest request) {
        String traceId = request.getParameter(ContextConstants.PIGEON_TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            traceId = request.getHeader(ContextConstants.PIGEON_TRACE_ID);
        }
        return traceId;
    }

    /**
     * 传递traceId至MDC
     *
     * @param traceId 　跟踪ID
     */
    public static void mdcTraceId(String traceId) {
        if (StringUtils.isNotBlank(traceId)) {
            MDC.put(ContextConstants.LOG_TRACE_ID, traceId);
        }
    }
}
