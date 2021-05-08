package cn.yiidii.pigeon.common.sftp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * sftp连接信息
 *
 * @author: YiiDii Wang
 * @create: 2021-05-08 13:06
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class SftpConnection {

    private String host;
    private int port = 22;
    private String username;
    private String password;

}
