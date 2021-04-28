package cn.yiidii.pigeon.log.event;

import cn.yiidii.pigeon.rbac.api.form.OptLogForm;
import org.springframework.context.ApplicationEvent;

/**
 * 日志事件
 * @author pangu 7333791@qq.com
 * @since 2020-7-15
 */
public class LogEvent extends ApplicationEvent {

    public LogEvent(OptLogForm optLogForm) {
        super(optLogForm);
    }

}
