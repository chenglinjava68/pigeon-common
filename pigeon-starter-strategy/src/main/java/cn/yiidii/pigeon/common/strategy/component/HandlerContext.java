package cn.yiidii.pigeon.common.strategy.component;

import cn.yiidii.pigeon.common.strategy.exception.NonBizProcessorException;
import cn.yiidii.pigeon.common.strategy.exception.StrategyExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Optional;

/**
 * 业务处理策略上下文
 *
 * @author YiiDii Wang
 * @create 2021-06-06 12:04
 */
@Setter
@AllArgsConstructor
public class HandlerContext {

    private Map<String, String> handlerMap;

    public String getBeanName(String bizCode) {
        final Optional<Map.Entry<String, String>> first = handlerMap.entrySet().stream().filter(e -> e.getKey().equals(bizCode)).findFirst();
        if(!first.isPresent()){
            throw new NonBizProcessorException(StrategyExceptionCode.NON_BIZ_PROCESSOR.getCode(), StrategyExceptionCode.NON_BIZ_PROCESSOR.getMsg(), bizCode);
        }
        return first.get().getValue();
    }

}
