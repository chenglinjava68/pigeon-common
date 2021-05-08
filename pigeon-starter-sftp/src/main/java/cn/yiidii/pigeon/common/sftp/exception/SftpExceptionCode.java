package cn.yiidii.pigeon.common.sftp.exception;

import cn.yiidii.pigeon.common.core.exception.code.BaseExceptionCode;
import lombok.AllArgsConstructor;

/**
 * sftp异常码，91****
 *
 * @author: YiiDii Wang
 * @create: 2021-04-28 22:25
 */
@AllArgsConstructor
public enum SftpExceptionCode implements BaseExceptionCode {

    SFTP_CONNECTION_FAILED(91001, "Sftp连接失败");

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
