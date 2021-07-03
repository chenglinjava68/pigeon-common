package cn.yiidii.pigeon.common.es.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @author: lujie
* @create: 2021/4/12
* @description: 分页检索对象
**/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "文档分页查询对象",description = "暂无")
public class PageSearchDTO {

    @ApiModelProperty(name = "index",value = "索引名称")
    private String index;
    @ApiModelProperty(name = "pageNo",value = "页码")
    private Integer pageNo;
    @ApiModelProperty(name = "pageSize",value = "页限制数量")
    private Integer pageSize;
    @ApiModelProperty(name = "keyword",value = "搜索关键字")
    private String keyword;
    @ApiModelProperty(name = "orderField",value = "排序字段")
    private  String orderField;
    @ApiModelProperty(name = "orderBy",value = "排序方式 ASC|DESC")
    private  String orderBy;
}
