package cn.yiidii.pigeon.common.es.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "新增文档入参实体",description = "暂无")
public class CreateDocumentDTO {
    @ApiModelProperty(name = "index",value = "索引名称1",required = true)
    private String index;
    @ApiModelProperty(name = "document",value = "文档对象",required = true)
    private Map<String,Object> document;
}
