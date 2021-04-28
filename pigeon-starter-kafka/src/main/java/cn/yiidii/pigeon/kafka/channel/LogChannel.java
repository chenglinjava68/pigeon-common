package cn.yiidii.pigeon.kafka.channel;

import cn.yiidii.pigeon.kafka.constant.KafkaConstant;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 *  日志消息通道
 *
 * @author YiiDii Wang
 * @date 2021/4/5 22:52:35
 */
public interface LogChannel {

    /**
     * 发消息的通道
     * @return MessageChannel
     */
    @Output(KafkaConstant.LOG_OUTPUT)
    MessageChannel sendLogMessage();

    /**
     * 收消息的通道
     * @return SubscribableChannel
     */
    @Input(KafkaConstant.LOG_INPUT)
    SubscribableChannel receiveLogMessage();

}
