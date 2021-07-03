package cn.yiidii.pigeon.common.es.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* @author: lujie
* @create: 2021/4/12
* @description:  es 分页对象
**/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsPage<T> {
    private List<T> data;
    private Long totalSize;
    private Long pageSize;
    private long currentPageNo;
}
