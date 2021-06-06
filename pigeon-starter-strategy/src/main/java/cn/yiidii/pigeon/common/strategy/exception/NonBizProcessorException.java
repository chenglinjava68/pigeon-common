package cn.yiidii.pigeon.common.strategy.exception;

import cn.yiidii.pigeon.common.core.exception.BaseUncheckedException;

/**
 * 无业务处理器异常
 *
 * @author YiiDii Wang
 * @create 2021-06-06 19:36
 */
public class NonBizProcessorException extends BaseUncheckedException {
    public NonBizProcessorException(int code, String format, Object... args) {
        super(code, format, args);
    }
}
