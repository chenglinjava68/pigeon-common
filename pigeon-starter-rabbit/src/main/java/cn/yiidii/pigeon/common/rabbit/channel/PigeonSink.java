package cn.yiidii.pigeon.common.rabbit.channel;

import cn.yiidii.pigeon.common.rabbit.constant.ChannelConstant;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 消费者channel
 *
 * @author: YiiDii Wang
 * @create: 2021-03-12 14:46
 */
public interface PigeonSink {

    /**
     * 邮件消费者
     *
     * @return SubscribableChannel
     */
    @Input(ChannelConstant.EMAIL_TEST_INPUT)
    SubscribableChannel emailInput();

}
