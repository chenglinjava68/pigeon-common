package cn.yiidii.pigeon.common.sftp.factory;

import cn.yiidii.pigeon.common.sftp.SftpConnection;
import cn.yiidii.pigeon.common.sftp.exception.SftpException;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.Properties;

/**
 * sftp连接工厂
 *
 * @author: YiiDii Wang
 * @create: 2021-05-08 13:03
 */
public class SftpFactory implements KeyedPooledObjectFactory<SftpConnection, ChannelSftp> {


    @Override
    public PooledObject<ChannelSftp> makeObject(SftpConnection sftpConnection) throws Exception {
        try {
            JSch jsch = new JSch();
            Session sshSession = jsch.getSession(sftpConnection.getUsername(), sftpConnection.getHost(), sftpConnection.getPort());
            sshSession.setPassword(sftpConnection.getPassword());
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            ChannelSftp channel = (ChannelSftp) sshSession.openChannel("sftp");
            return new DefaultPooledObject<>(channel);
        } catch (JSchException e) {
            throw new SftpException();
        }
    }

    @Override
    public void destroyObject(SftpConnection sftpConnection, PooledObject<ChannelSftp> pooledObject) throws Exception {
        ChannelSftp channelSftp = pooledObject.getObject();
        channelSftp.disconnect();
    }

    @Override
    public boolean validateObject(SftpConnection sftpConnection, PooledObject<ChannelSftp> pooledObject) {
        return false;
    }

    @Override
    public void activateObject(SftpConnection sftpConnection, PooledObject<ChannelSftp> pooledObject) throws Exception {
        ChannelSftp channelSftp = pooledObject.getObject();
        channelSftp.connect();
    }

    @Override
    public void passivateObject(SftpConnection sftpConnection, PooledObject<ChannelSftp> pooledObject) throws Exception {
        // do nothing ...
    }
}
