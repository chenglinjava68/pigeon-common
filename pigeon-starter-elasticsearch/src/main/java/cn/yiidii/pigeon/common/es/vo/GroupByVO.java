package cn.yiidii.pigeon.common.es.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
* @author: lujie
* @create: 2021/4/13
* @description: 分组查询返回值
**/


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("分组查询返回值")
public class GroupByVO {


    private String key;
    private Long val;
}
