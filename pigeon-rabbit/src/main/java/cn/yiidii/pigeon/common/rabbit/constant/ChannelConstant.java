package cn.yiidii.pigeon.common.rabbit.constant;

import cn.yiidii.pigeon.common.core.constant.StringPool;

/**
 * channel常量
 *
 * @author: YiiDii Wang
 * @create: 2021-03-12 14:47
 */
public interface ChannelConstant {

    /**
     * 邮件消息
     */
    String EMAIL_MESSAGE = "email";

    /**
     * 测试
     */
    String TEST_PREFIX = "test";

    /**
     * 生产者标识
     */
    String OUTPUT = "output";

    /**
     * 消费者标识
     */
    String INPUT = "input";

    String EMAIL_TEST_OUTPUT = TEST_PREFIX + StringPool.DASH + EMAIL_MESSAGE + StringPool.DASH + OUTPUT;

    String EMAIL_TEST_INPUT = TEST_PREFIX + StringPool.DASH + EMAIL_MESSAGE + StringPool.DASH + INPUT;


}
