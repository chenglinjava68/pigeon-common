package cn.yiidii.pigeon.common.es.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "queryString入参",description = "暂无")
public class QueryStringDTO {
    @ApiModelProperty(name ="index",value = "索引名称")
    private String index;
    @ApiModelProperty(name ="fieldList",value = "检索字段集合")
    private List<BoostField> fieldList;
    @ApiModelProperty(name ="fieldList",value = "检索内容")
    private  String queryString;
    @ApiModelProperty(name ="defaultOperator",value = "检索内容分隔符",example="AND || OR")
    private String defaultOperator;
}
