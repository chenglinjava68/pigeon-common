package cn.yiidii.pigeon.common.strategy.handler;

/**
 * 策略抽象类
 *
 * @author YiiDii Wang
 * @create 2021-06-06 12:02
 */
public interface AbstractHandler<R, T> {

    /**
     * 业务处理
     *
     * @param t 业务实体返回参数
     * @return R　结果
     */
    R handle(T t);

}
