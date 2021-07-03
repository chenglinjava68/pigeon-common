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
@ApiModel(value = "多关键字检索",description = "暂无")
public class TermsSearchDTO {


    @ApiModelProperty(name = "index",value = "索引名称",required = true)
    private String index;
    @ApiModelProperty(name = "field",value = "检索字段",required = true)
    private String field;
    @ApiModelProperty(name = "vals",value = "多个关键字",required = true)
    private List<String> vals;
}
