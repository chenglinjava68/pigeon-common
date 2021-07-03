package cn.yiidii.pigeon.common.es.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* @author: lujie
* @create: 2021/4/13
* @description: IdsSearchDTO
**/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("多id查询入参")
public class IdsSearchDTO {


    @ApiModelProperty(name = "ids",value = "文档id集合")
    private List<String> ids;
    @ApiModelProperty(name = "index",value = "索引名称")
    private String index;
}
