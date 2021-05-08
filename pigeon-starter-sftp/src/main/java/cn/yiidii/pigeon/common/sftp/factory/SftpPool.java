package cn.yiidii.pigeon.common.sftp.factory;

import cn.yiidii.pigeon.common.sftp.SftpConnection;
import com.jcraft.jsch.ChannelSftp;
import lombok.Getter;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

/**
 * sftp连接池
 *
 * @author: YiiDii Wang
 * @create: 2021-05-08 13:46
 */
@Getter
public class SftpPool extends GenericKeyedObjectPool<SftpConnection, ChannelSftp> {

    public SftpPool(KeyedPooledObjectFactory<SftpConnection, ChannelSftp> factory, GenericKeyedObjectPoolConfig<ChannelSftp> config) {
        super(factory, config);
    }

}
