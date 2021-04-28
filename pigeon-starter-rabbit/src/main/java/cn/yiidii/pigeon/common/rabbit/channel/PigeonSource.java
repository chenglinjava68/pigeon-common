package cn.yiidii.pigeon.common.rabbit.channel;

import cn.yiidii.pigeon.common.rabbit.constant.ChannelConstant;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 生产者channel
 *
 * @author: YiiDii Wang
 * @create: 2021-03-12 14:46
 */
public interface PigeonSource {

    /**
     * 邮件通道
     *
     * @return MessageChannel
     */
    @Output(ChannelConstant.EMAIL_TEST_OUTPUT)
    MessageChannel emailOutput();

}
