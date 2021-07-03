package cn.yiidii.pigeon.common.es.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("权重字段对象")
public class BoostField {
    @ApiModelProperty(name = "field",value = "权重字段对象")
    private  String field;

    @ApiModelProperty(name = "权重",value = "权重字段对象")
    private  Float boost;
}
