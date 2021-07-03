package cn.yiidii.pigeon.common.es.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
* @author: lujie
* @create: 2021/4/12
* @description: 修改文档入参
**/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "修改文档入参实体",description = "暂无")
public class UpdateDocumentDTO {
    @ApiModelProperty(name = "index",value = "索引名称",required = true)
    private String index;
    @ApiModelProperty(name = "documentId",value = "文档ID",required = true)
    private String documentId;
    @ApiModelProperty(name = "document",value = "文档对象",required = true)
    private Map<String,Object> document;
}
