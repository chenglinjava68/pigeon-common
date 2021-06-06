package cn.yiidii.pigeon.common.strategy.exception;

import cn.yiidii.pigeon.common.core.exception.BaseUncheckedException;

/**
 * 重复业务代码异常
 *
 * @author YiiDii Wang
 * @create 2021-06-06 19:25
 */
public class DuplicateBizCodeException extends BaseUncheckedException {
    public DuplicateBizCodeException(int code, String format, Object... args) {
        super(code, format, args);
    }
}
