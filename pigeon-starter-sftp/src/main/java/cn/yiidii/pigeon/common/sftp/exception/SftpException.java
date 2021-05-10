package cn.yiidii.pigeon.common.sftp.exception;

import cn.yiidii.pigeon.common.core.exception.BaseUncheckedException;
import cn.yiidii.pigeon.common.core.exception.code.BaseExceptionCode;

/**
 * sftp异常
 *
 * @author YiiDii Wang
 * @create 2021-05-08 13:10
 */
public class SftpException extends BaseUncheckedException {

    public SftpException() {
        super(SftpExceptionCode.SFTP_CONNECTION_FAILED.getCode(), SftpExceptionCode.SFTP_CONNECTION_FAILED.getMsg());
    }

    public SftpException(BaseExceptionCode baseExceptionCode) {
        super(baseExceptionCode);
    }
}
