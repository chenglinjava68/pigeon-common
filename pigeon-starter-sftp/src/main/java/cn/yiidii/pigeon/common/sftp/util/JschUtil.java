package cn.yiidii.pigeon.common.sftp.util;

import cn.yiidii.pigeon.common.sftp.SftpConnection;
import cn.yiidii.pigeon.common.sftp.exception.SftpException;
import cn.yiidii.pigeon.common.sftp.exception.SftpExceptionCode;
import cn.yiidii.pigeon.common.sftp.factory.SftpPool;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * jsch工具类
 *
 * @author YiiDii Wang
 * @create 2021-05-08 14:17
 */
@Component
@RequiredArgsConstructor
public class JschUtil {

    private static final String NO_SUCH_FILE = "no such file";

    public final SftpPool pool;

    /**
     * home目录
     *
     * @param conn sftp连接信息
     * @return 用户home目录
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

    /**
     * 执行一条命令
     *
     * @param conn    sftp连接信息
     * @param command 命令
     * @return 命令执行成功的结果集
     * @throws Exception 异常
     */
    public List<String> execCmd(SftpConnection conn, String command) throws Exception {
        // 执行结果
        List<String> resultList = new ArrayList<>();
        ChannelSftp channelSftp = null;
        BufferedReader reader = null;
        ChannelExec channel = null;
        Session session = null;
        int returnCode = -1;
        try {
            channelSftp = pool.borrowObject(conn);
            session = channelSftp.getSession();
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            InputStream in = channel.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            // 连接通道
            channel.connect();
            // 读取结果
            String buf;
            while ((buf = reader.readLine()) != null) {
                if (StringUtils.isNotBlank(buf)) {
                    resultList.add(buf);
                }
            }
            if (channel.isClosed()) {
                returnCode = channel.getExitStatus();
            }
            // returnCode非正常就清空result
            if (0 != returnCode) {
                resultList.clear();
            }
        } catch (Exception e) {
            throw new SftpException();
        } finally {
            if (Objects.nonNull(channelSftp)) {
                pool.returnObject(conn, channelSftp);
            }
            if (Objects.nonNull(reader)) {
                reader.close();
            }
            if (Objects.nonNull(channel) && channel.isConnected()) {
                channel.disconnect();
            }
            if (Objects.nonNull(session) && session.isConnected()) {
                session.disconnect();
            }
        }
        return resultList;
    }

    /**
     * 切换工作目录
     *
     * @param conn    sftp连接信息
     * @param dirPath 目录路径
     * @return 切换目录是否成功
     */
    public boolean cd(SftpConnection conn, String dirPath) {
        if (StringUtils.isBlank(dirPath)) {
            return false;
        }
        ChannelSftp channelSftp = null;
        try {
            channelSftp = pool.borrowObject(conn);
            channelSftp.cd(dirPath.replaceAll("\\\\", "/"));
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (Objects.nonNull(channelSftp)) {
                pool.returnObject(conn, channelSftp);
            }
        }
    }

    /**
     * 创建一个文件目录，mkdir每次只能创建一个文件目录
     * 或者可以使用命令mkdir -p 来创建多个文件目录
     *
     * @param conn    sftp连接信息
     * @param dirPath 目录路径
     */
    public void mkdir(SftpConnection conn, String dirPath) {
        ChannelSftp channelSftp = null;
        try {
            channelSftp = pool.borrowObject(conn);
            if (isDirExist(conn, dirPath)) {
                channelSftp.cd(dirPath);
                return;
            }
            String[] pathArray = dirPath.split("/");
            StringBuilder filePath = new StringBuilder("/");
            for (String path : pathArray) {
                if (StringUtils.isBlank(path)) {
                    continue;
                }
                filePath.append(path).append("/");
                if (!isDirExist(conn, filePath.toString())) {
                    // 建立目录
                    channelSftp.mkdir(filePath.toString());
                }
                // 进入并设置为当前目录
                channelSftp.cd(filePath.toString());
            }
            channelSftp.cd(dirPath);
        } catch (Exception e) {
            throw new SftpException(SftpExceptionCode.MKDIR_PATH_ERROR);
        } finally {
            if (Objects.nonNull(channelSftp)) {
                pool.returnObject(conn, channelSftp);
            }
        }
    }

    /**
     * 判断目录是否存在
     *
     * @param conn    sftp连接信息
     * @param dirPath 目录路径
     * @return 文件夹是否存在
     */
    public boolean isDirExist(SftpConnection conn, String dirPath) {
        boolean isDirExistFlag;
        ChannelSftp channelSftp = null;
        try {
            channelSftp = pool.borrowObject(conn);
            SftpATTRS sftpAttrS = channelSftp.lstat(dirPath);
            isDirExistFlag = sftpAttrS.isDir();
        } catch (Exception e) {
            isDirExistFlag = false;
        } finally {
            if (Objects.nonNull(channelSftp)) {
                pool.returnObject(conn, channelSftp);
            }
        }
        return isDirExistFlag;
    }


}
