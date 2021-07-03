package cn.yiidii.pigeon.common.es.factory;

import cn.yiidii.pigeon.common.es.consts.HighlightSearchConst;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

/**
 * @author lujie
 * @create 2021/4/12
 * @description ES 对象工厂
 **/
public class ElasticsearchFactory {

    public static HighlightBuilder builderHighlight() {
        return new HighlightBuilder()
                .preTags(HighlightSearchConst.PRE_TAGS)
                .postTags(HighlightSearchConst.POST_TAGS)
                .field("*")
                //关闭多个高亮
                .requireFieldMatch(false);
    }
}
