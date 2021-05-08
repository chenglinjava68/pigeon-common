package cn.yiidii.pigeon.common.sftp.util;

import cn.yiidii.pigeon.common.sftp.SftpConnection;
import cn.yiidii.pigeon.common.sftp.exception.SftpException;
import cn.yiidii.pigeon.common.sftp.factory.SftpPool;
import com.jcraft.jsch.ChannelSftp;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * jsch工具类
 *
 * @author: YiiDii Wang
 * @create: 2021-05-08 14:17
 */
@Component
@RequiredArgsConstructor
public class JschUtil {

    public final SftpPool pool;

    /**
     * home目录
     *
     * @param conn
     * @return
     */
    @SneakyThrows(Exception.class)
    public String getHome(SftpConnection conn) {
        ChannelSftp channelSftp = null;
        try {
            channelSftp = pool.borrowObject(conn);
        } catch (Exception e) {
            throw new SftpException();
        } finally {
            if (Objects.nonNull(channelSftp)) {
                pool.returnObject(conn, channelSftp);
            }
        }
        return channelSftp.getHome();
    }

}
