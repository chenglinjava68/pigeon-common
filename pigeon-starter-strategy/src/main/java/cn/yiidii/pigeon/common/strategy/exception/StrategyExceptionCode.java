package cn.yiidii.pigeon.common.strategy.exception;

import cn.yiidii.pigeon.common.core.exception.code.BaseExceptionCode;
import lombok.AllArgsConstructor;

/**
 * 策略模块异常码
 *
 * @author YiiDii Wang
 * @create 2021-04-28 22:25
 */
@AllArgsConstructor
public enum StrategyExceptionCode implements BaseExceptionCode {

    DUPLICATE_BIZ_CODE(90101, "重复业务代码(%s)"),
    NON_BIZ_PROCESSOR(90102, "无业务处理器(%s)");

    private final int code;
    private String msg;

    /**
     * 异常编码
     *
     * @return 异常编码
     */
    @Override
    public int getCode() {
        return this.code;
    }

    /**
     * 异常消息
     *
     * @return 异常消息
     */
    @Override
    public String getMsg() {
        return this.msg;
    }
}
