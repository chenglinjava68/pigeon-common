package cn.yiidii.pigeon.common.ide.exception;

import cn.yiidii.pigeon.common.core.exception.BaseUncheckedException;

/**
 * 幂等异常
 *
 * @author: YiiDii Wang
 * @create: 2021-04-28 22:22
 */
public class IdeException extends BaseUncheckedException {

    public IdeException() {
        super(IdeExceptionCode.IDE_TARGET.getCode(), IdeExceptionCode.IDE_TARGET.getMsg());
    }
}
