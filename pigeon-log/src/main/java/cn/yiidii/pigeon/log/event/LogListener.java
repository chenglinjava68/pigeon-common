package cn.yiidii.pigeon.log.event;

import cn.yiidii.pigeon.common.core.base.R;
import cn.yiidii.pigeon.common.core.util.SpringContextHolder;
import cn.yiidii.pigeon.rbac.api.feign.SysLogFeign;
import cn.yiidii.pigeon.rbac.api.form.OptLogForm;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 注解形式，异步监听事件
 *
 * @author pangu 7333791@qq.com
 * @since 2020-7-15
 */
@Slf4j
@Setter
public class LogListener {

    private SysLogFeign sysLogFeign;

    public LogListener() {

    }

    public LogListener(SysLogFeign sysLogFeign) {
        this.sysLogFeign = sysLogFeign;
    }

    @Async
    @Order
    @EventListener(LogEvent.class)
    public void saveSysLog(LogEvent event) {
        OptLogForm optLogForm = (OptLogForm) event.getSource();
        log.info("日志监听: {}", JSONObject.toJSONString(optLogForm));
        R resp = sysLogFeign.createOptLog(optLogForm);
        log.info("日志resp: {}", JSONObject.toJSONString(resp));
    }

}
