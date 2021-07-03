package cn.yiidii.pigeon.common.es;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.yiidii.pigeon.common.es.consts.CompletionFieldConst;
import cn.yiidii.pigeon.common.es.consts.IndexConst;
import cn.yiidii.pigeon.common.es.dto.IdsSearchDTO;
import cn.yiidii.pigeon.common.es.dto.MultiMatchQueryDTO;
import cn.yiidii.pigeon.common.es.dto.PageSearchDTO;
import cn.yiidii.pigeon.common.es.dto.TermsSearchDTO;
import cn.yiidii.pigeon.common.es.factory.ElasticsearchFactory;
import cn.yiidii.pigeon.common.es.vo.EsPage;
import cn.yiidii.pigeon.common.es.vo.GroupByVO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.TermVectorsRequest;
import org.elasticsearch.client.core.TermVectorsResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.stereotype.Component;

/**
 * ES操作工具类
 *
 * @author YiiDii Wang
 * @create 2021-06-30 12:15
 */
@Slf4j
@Component("esUtil")
@RequiredArgsConstructor
public class EsUtil {

    private final RestHighLevelClient restHighLevelClient;

    /**
     * 创建索引
     *
     * @param index 索引名称
     * @return
     */
    public CreateIndexResponse createIndex(String index) throws IOException {
        //1.创建索引请求
        CreateIndexRequest request = new CreateIndexRequest(index);
        //2.执行客户端请求
        return restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
    }

    /**
     * 查询所有索引
     *
     * @return
     * @throws IOException
     */
    public Object findAllIndex() throws IOException {
        GetAliasesRequest request = new GetAliasesRequest();
        GetAliasesResponse getAliasesResponse = restHighLevelClient.indices().getAlias(request, RequestOptions.DEFAULT);
        Map<String, Set<AliasMetadata>> aliases = getAliasesResponse.getAliases();
        Set<String> indices = aliases.keySet();
        return indices;
    }

    /**
     * 判断索引是否存在
     *
     * @param index
     * @return
     */
    public boolean isIndexExist(String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest(index);
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    /**
     * 删除索引
     *
     * @param index
     * @return
     */
    public AcknowledgedResponse deleteIndex(String index) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        AcknowledgedResponse delete = restHighLevelClient.indices()
                .delete(request, RequestOptions.DEFAULT);
        return delete;
    }


    /**
     * 查看索引映射
     *
     * @param index
     * @return
     * @throws IOException
     */
    public Object catIndexMappings(String index) throws IOException {
        GetMappingsRequest getMappingsRequest = new GetMappingsRequest();
        getMappingsRequest.indices(index);
        GetMappingsResponse mapping = restHighLevelClient.indices().getMapping(getMappingsRequest, RequestOptions.DEFAULT);
        return mapping.mappings();
    }


    /**
     * 数据添加，自定义id
     *
     * @param object 要增加的数据
     * @param index  索引，类似数据库
     * @param id     数据ID,为null时es随机生成
     * @return
     */
    public IndexResponse addData(Object object, String index, String id) throws IOException {
        //创建请求
        IndexRequest request = new IndexRequest(index);
        //规则 put /test_index/_doc/1
        request.id(id);
        request.timeout(TimeValue.timeValueSeconds(1));
        //将数据放入请求 json
        request.source(JSONUtil.toJsonStr(object), XContentType.JSON);
        //客户端发送请求
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        return response;
    }

    /**
     * 数据添加 随机id
     *
     * @param object 要增加的数据
     * @param index  索引，类似数据库
     * @return
     */
    public IndexResponse addData(Object object, String index) throws IOException {
        return addData(object, index, IdUtil.randomUUID());
    }

    /**
     * 通过ID删除数据
     *
     * @param index 索引，类似数据库
     * @param id    数据ID
     * @return
     */
    public DeleteResponse deleteDataById(String index, String id) throws IOException {
        DeleteRequest request = new DeleteRequest(index, id);
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        return response;
    }

    /**
     * 通过ID 更新数据
     *
     * @param object 要更新数据
     * @param index  索引，类似数据库
     * @param id     数据ID
     * @return
     */
    public UpdateResponse updateDataById(Object object, String index, String id) throws IOException {
        UpdateRequest update = new UpdateRequest(index, id);
        update.timeout("1s");
        update.doc(JSONUtil.toJsonStr(object), XContentType.JSON);
        UpdateResponse response = restHighLevelClient.update(update, RequestOptions.DEFAULT);
        return response;
    }


    /**
     * 通过ID获取数据
     *
     * @param index  索引，类似数据库
     * @param id     数据ID
     * @param fields 需要显示的字段，逗号分隔（缺省为全部字段）
     * @return
     */
    public Map<String, Object> searchDataById(String index, String id, String fields) throws IOException {
        GetRequest request = new GetRequest(index, id);
        if (StrUtil.isNotBlank(fields)) {
            //只查询特定字段。如果需要查询所有字段则不设置该项。
            request.fetchSourceContext(new FetchSourceContext(true, fields.split(","), Strings.EMPTY_ARRAY));
        }
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        return response.getSource();
    }

    /**
     * 通过ID判断文档是否存在
     *
     * @param index 索引，类似数据库
     * @param id    数据ID
     * @return
     */
    public boolean existsById(String index, String id) throws IOException {
        GetRequest request = new GetRequest(index, id);
        //不获取返回的_source的上下文
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");
        return restHighLevelClient.exists(request, RequestOptions.DEFAULT);
    }

    /**
     * 批量插入
     *
     * @param index   索引，类似数据库
     * @param objects 数据
     * @return
     */
    public boolean bulkPost(String index, List<?> objects) {
        BulkRequest bulkRequest = new BulkRequest();
        BulkResponse response = null;
        // 最大数量不得超过20万
        for (Object object : objects) {
            IndexRequest request = new IndexRequest(index);
            request.source(JSONUtil.toJsonStr(object), XContentType.JSON);
            bulkRequest.add(request);
        }
        try {
            response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return !response.hasFailures();
    }

    /**
     * 根据经纬度查询范围查找location 经纬度字段，distance 距离中心范围KM，lat  lon 圆心经纬度
     *
     * @param index
     * @param longitude
     * @param latitude
     * @param distance
     * @return
     */
    public SearchResponse geoDistanceQuery(String index, Float longitude, Float latitude, String distance) throws IOException {

        if (longitude == null || latitude == null) {
            return null;
        }
        //拼接条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        QueryBuilder isdeleteBuilder = QueryBuilders.termQuery("isdelete", false);
        // 以某点为中心，搜索指定范围
        GeoDistanceQueryBuilder distanceQueryBuilder = new GeoDistanceQueryBuilder("location");
        distanceQueryBuilder.point(latitude, longitude);
        //查询单位：km
        distanceQueryBuilder.distance(distance, DistanceUnit.KILOMETERS);
        boolQueryBuilder.filter(distanceQueryBuilder);
//        boolQueryBuilder.must(isdeleteBuilder);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);

        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return searchResponse;
    }

    /**
     * 获取低水平客户端
     *
     * @return
     */
    public RestClient getLowLevelClient() {
        return restHighLevelClient.getLowLevelClient();
    }

    public EsPage queryPageList(PageSearchDTO pageSearchDTO) throws IOException {

        String index = pageSearchDTO.getIndex();
        Integer pageNo = pageSearchDTO.getPageNo();
        Integer pageSize = pageSearchDTO.getPageSize();
        String keyword = pageSearchDTO.getKeyword();
        String orderField = pageSearchDTO.getOrderField();
        String orderBy = pageSearchDTO.getOrderBy();

        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        QueryBuilder matchAllQuery = QueryBuilders.matchAllQuery();
        builder.query(matchAllQuery);
        Integer from = (pageNo - 1) * pageSize;
        builder.from(from);
        builder.size(pageSize);
        if (StrUtil.isNotBlank(keyword)) {
            MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("menu_name", keyword);
            builder.query(queryBuilder);
        }
        if (StrUtil.isNotBlank(orderField) && StrUtil.isNotBlank(orderBy)) {
            FieldSortBuilder order = new FieldSortBuilder(orderField);
            if (orderBy.equals("ASC")) {
                order.order(SortOrder.ASC);
            } else {
                order.order(SortOrder.DESC);
            }
            builder.sort(order);
        } else {
            builder.sort("_score", SortOrder.DESC);
        }
        request.source(builder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        log.info("DSL:" + builder.toString());
        Long total = response.getHits().getTotalHits().value;
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            log.info(sourceAsString);
        }
        List<Map<String, Object>> pageList = Arrays.stream(hits).map(SearchHit::getSourceAsMap).collect(Collectors.toList());
        EsPage<Map<String, Object>> page = new EsPage();
        page.setData(pageList);
        page.setCurrentPageNo(pageNo);
        page.setPageSize(Long.parseLong(pageSize.toString()));
        page.setTotalSize(total);
        return page;
    }


    /**
     * 查询并分页
     *
     * @param index          索引名称
     * @param query          查询条件
     * @param size           文档大小限制
     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField      排序字段
     * @param highlightField 高亮字段
     * @return
     */
    public List<Map<String, Object>> searchListData(String index,
            SearchSourceBuilder query,
            Integer size,
            Integer from,
            String fields,
            String sortField,
            String highlightField) throws IOException {
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder builder = query;
        if (StrUtil.isNotEmpty(fields)) {
            //只查询特定字段。如果需要查询所有字段则不设置该项。
            builder.fetchSource(new FetchSourceContext(true, fields.split(","), Strings.EMPTY_ARRAY));
        }
        from = from <= 0 ? 0 : from * size;
        //设置确定结果要从哪个索引开始搜索的from选项，默认为0
        builder.from(from);
        builder.size(size);
        if (StrUtil.isNotEmpty(sortField)) {
            //排序字段，注意如果proposal_no是text类型会默认带有keyword性质，需要拼接.keyword
            builder.sort(sortField + ".keyword", SortOrder.ASC);
        }
        //高亮
        HighlightBuilder highlight = new HighlightBuilder();
        highlight.field(highlightField);
        //关闭多个高亮
        highlight.requireFieldMatch(false);
        highlight.preTags("<span style='color:red'>");
        highlight.postTags("</span>");
        builder.highlighter(highlight);
        //不返回源数据。只有条数之类的数据。
//        builder.fetchSource(false);
        request.source(builder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        log.error("==" + response.getHits().getTotalHits());
        if (response.status().getStatus() == 200) {
            // 解析对象
            return transformHighlight(response);
        }
        return null;
    }

    public List<GroupByVO> queryGroupBy(String index, String groupByField, Integer size) throws IOException {
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String queryName = groupByField + "GroupBy";
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms(queryName).field(groupByField).size(size);
        searchSourceBuilder.aggregation(termsAggregationBuilder);
        log.info("DSL:" + searchSourceBuilder);
        request.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        Aggregations aggregations = response.getAggregations();
        Terms terms = aggregations.get(queryName);
        List<GroupByVO> result = terms.getBuckets().stream().map(item -> GroupByVO.builder().key(item.getKeyAsString())
                .val(item.getDocCount()).build()).collect(Collectors.toList());
        return result;
    }

    /**
     * 关键字搜索
     * @param index     索引
     * @param field     字段
     * @param keyword   关键字
     * @return
     * @throws IOException
     */
    public Object termSearch(String index, String field, String keyword) throws IOException {
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(field, keyword);
        searchSourceBuilder.query(termQueryBuilder);
        request.source(searchSourceBuilder);
        log.info("DSL:" + searchSourceBuilder.toString());
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        return Arrays.stream(response.getHits().getHits()).map(SearchHit::getSourceAsMap).collect(Collectors.toList());
    }

    /**
     * 多关键字搜索
     * @param termsSearchDTO    搜索条件
     * @return
     * @throws IOException
     */
    public Object termsSearch(TermsSearchDTO termsSearchDTO) throws IOException {

        String index = termsSearchDTO.getIndex();
        List<String> vals = termsSearchDTO.getVals();
        String field = termsSearchDTO.getField();

        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery(field, vals);
        searchSourceBuilder.query(termsQueryBuilder);
        HighlightBuilder highlightBuilder = ElasticsearchFactory.builderHighlight();
        searchSourceBuilder.highlighter(highlightBuilder);
        request.source(searchSourceBuilder);
        log.info("DSL:" + searchSourceBuilder.toString());
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        return Arrays.stream(response.getHits().getHits()).map(SearchHit::getSourceAsMap).collect(Collectors.toList());
    }

    /**
     * 范围搜索
     * @param index     索引
     * @param field     字段
     * @param gte
     * @param lte
     * @return
     * @throws IOException
     */
    public Object rangeSearch(String index, String field, String gte, String lte) throws IOException {
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(field).gte(gte).lte(lte);
        searchSourceBuilder.query(rangeQueryBuilder);
        request.source(searchSourceBuilder);
        log.info("DSL:" + searchSourceBuilder.toString());
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        return Arrays.stream(response.getHits().getHits()).map(SearchHit::getSourceAsMap).collect(Collectors.toList());
    }

    /**
     * 前缀搜索
     * @param index     索引
     * @param field     字段
     * @param prefix    前缀
     * @return
     * @throws IOException
     */
    public List<Map<String, Object>> prefixSearch(String index, String field, String prefix) throws IOException {
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery(field, prefix);
        searchSourceBuilder.query(prefixQueryBuilder);
        request.source(searchSourceBuilder);
        log.info("DSL:" + searchSourceBuilder.toString());
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        return Arrays.stream(response.getHits().getHits()).map(SearchHit::getSourceAsMap).collect(Collectors.toList());
    }

    /**
     *
     * 统配搜索
     * @param index         关键字
     * @param field         字段
     * @param condition     条件
     * @return
     * @throws IOException
     */
    public Object wildcardSearch(String index, String field, String condition) throws IOException {
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery(field, condition);
        searchSourceBuilder.query(wildcardQueryBuilder);
        request.source(searchSourceBuilder);
        log.info("DSL:" + searchSourceBuilder.toString());
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        return Arrays.stream(response.getHits().getHits()).map(SearchHit::getSourceAsMap).collect(Collectors.toList());
    }

    /**
     * 按照文档搜索
     * @param idsSearchDTO  搜索条件
     * @return
     * @throws IOException
     */
    public Object idsSearch(IdsSearchDTO idsSearchDTO) throws IOException {
        String index = idsSearchDTO.getIndex();
        List<String> ids = idsSearchDTO.getIds();
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        IdsQueryBuilder idsQueryBuilder = QueryBuilders.idsQuery();
        ids.forEach(id -> {
            idsQueryBuilder.addIds(id);
        });
        searchSourceBuilder.query(idsQueryBuilder);
        request.source(searchSourceBuilder);
        log.info("DSL:" + searchSourceBuilder.toString());
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        return Arrays.stream(response.getHits().getHits()).map(SearchHit::getSourceAsMap).collect(Collectors.toList());
    }

    /**
     * 模糊搜索
     * @param index         索引
     * @param field         字段
     * @param condition     条件
     * @return
     * @throws IOException
     */
    public Object fuzzyQuery(String index, String field, String condition) throws IOException {
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery(field, condition);
        searchSourceBuilder.query(fuzzyQueryBuilder);
        request.source(searchSourceBuilder);
        log.info("DSL:" + searchSourceBuilder.toString());
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        return Arrays.stream(response.getHits().getHits()).map(SearchHit::getSourceAsMap).collect(Collectors.toList());
    }

    /**
     * 高亮搜索
     * @param index         索引
     * @param field         字段
     * @param condition     条件
     * @return
     * @throws IOException
     */
    public Object highlightQuery(String index, String field, String condition) throws IOException {
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(field, condition);
        searchSourceBuilder.query(matchQueryBuilder);
        HighlightBuilder highlightBuilder = ElasticsearchFactory.builderHighlight();
        searchSourceBuilder.highlighter(highlightBuilder);
        request.source(searchSourceBuilder);
        log.info("DSL:" + searchSourceBuilder.toString());
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        return transformHighlight(response);

    }

    /**
     * 匹配搜索
     * @param index         索引
     * @param field         字段
     * @param keyword       关键字
     * @return
     * @throws IOException
     */
    public Object matchQuery(String index, String field, String keyword) throws IOException {
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(field, keyword);
        searchSourceBuilder.query(matchQueryBuilder);
        HighlightBuilder highlightBuilder = ElasticsearchFactory.builderHighlight();
        searchSourceBuilder.highlighter(highlightBuilder);
        request.source(searchSourceBuilder);
        log.info("DSL:" + searchSourceBuilder.toString());
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        return Arrays.stream(response.getHits().getHits()).map(SearchHit::getSourceAsMap).collect(Collectors.toList());
    }

    public Object autoCompletion(String keyword) throws IOException {
        SearchRequest request = new SearchRequest(IndexConst.API_NEWS_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        /**
         * 查询字符按一定要有 .pinyin .suggest .standard 子字段
         *
         */
        String field = "title";
        CompletionSuggestionBuilder pinyin = SuggestBuilders.completionSuggestion(field + CompletionFieldConst.PINYIN)
                .prefix(keyword)
                .skipDuplicates(true)
                .size(10);
        CompletionSuggestionBuilder suggest = SuggestBuilders.completionSuggestion(field + CompletionFieldConst.SUGGEST)
                .prefix(keyword)
                .skipDuplicates(true)
                .size(10);
        CompletionSuggestionBuilder standard = SuggestBuilders.completionSuggestion(field + CompletionFieldConst.STANDARD)
                .prefix(keyword)
                .skipDuplicates(true)
                .size(10);
        suggestBuilder.addSuggestion("pinyin", pinyin);
        suggestBuilder.addSuggestion("suggest", suggest);
        suggestBuilder.addSuggestion("standard", standard);
        searchSourceBuilder.suggest(suggestBuilder);
        request.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        log.info("DSL:" + searchSourceBuilder.toString());
        List<String> keywordList = response.getSuggest().filter(CompletionSuggestion.class).stream()
                .flatMap(s -> s.getOptions().stream())
                .map(Suggest.Suggestion.Entry.Option::getText)
                .map(Text::toString)
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());
        return keywordList;
    }

    public Object boolQueryMust(String category, String title) throws IOException {
        SearchRequest request = new SearchRequest(IndexConst.API_NEWS_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("category", category);
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("title", title);
        boolQueryBuilder.must(termQueryBuilder);
        boolQueryBuilder.must(matchQuery);
        searchSourceBuilder.query(boolQueryBuilder);
        HighlightBuilder highlightBuilder = ElasticsearchFactory.builderHighlight();
        searchSourceBuilder.highlighter(highlightBuilder);
        request.source(searchSourceBuilder);
        log.info("DSL:" + searchSourceBuilder.toString());
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        return Arrays.stream(response.getHits().getHits()).map(SearchHit::getSourceAsMap).collect(Collectors.toList());
    }


    public Object boolQueryShould(String authorName1, String authorName2) throws IOException {
        SearchRequest request = new SearchRequest(IndexConst.API_NEWS_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        MatchQueryBuilder matchQuery1 = QueryBuilders.matchQuery("author_name", authorName1);
        TermQueryBuilder matchQuery2 = QueryBuilders.termQuery("author_name", authorName2);
        boolQueryBuilder.should(matchQuery1);
        boolQueryBuilder.should(matchQuery2);
        searchSourceBuilder.query(boolQueryBuilder);
        HighlightBuilder highlightBuilder = ElasticsearchFactory.builderHighlight();
        searchSourceBuilder.highlighter(highlightBuilder);
        request.source(searchSourceBuilder);
        log.info("DSL:" + searchSourceBuilder.toString());
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        return Arrays.stream(response.getHits().getHits()).map(SearchHit::getSourceAsMap).collect(Collectors.toList());
    }

    public Object boolQueryMustNot(String category) throws IOException {
        SearchRequest request = new SearchRequest(IndexConst.API_NEWS_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        TermQueryBuilder categoryQuery = QueryBuilders.termQuery("category", category);
        boolQueryBuilder.mustNot(categoryQuery);
        searchSourceBuilder.query(boolQueryBuilder);
        HighlightBuilder highlightBuilder = ElasticsearchFactory.builderHighlight();
        searchSourceBuilder.highlighter(highlightBuilder);
        request.source(searchSourceBuilder);
        log.info("DSL:" + searchSourceBuilder.toString());
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        return Arrays.stream(response.getHits().getHits()).map(SearchHit::getSourceAsMap).collect(Collectors.toList());
    }

    public Object multiMatchQuery(MultiMatchQueryDTO multiMatchQueryDTO) throws IOException {
        String index = multiMatchQueryDTO.getIndex();
        List<String> fieldList = multiMatchQueryDTO.getFieldList();
        String keyword = multiMatchQueryDTO.getKeyword();
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keyword, ArrayUtil.toArray(fieldList, String.class));
        searchSourceBuilder.query(multiMatchQueryBuilder);
        HighlightBuilder highlightBuilder = ElasticsearchFactory.builderHighlight();
        searchSourceBuilder.highlighter(highlightBuilder);
        request.source(searchSourceBuilder);
        log.info("DSL:" + searchSourceBuilder.toString());
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        return Arrays.stream(response.getHits().getHits()).map(SearchHit::getSourceAsMap).collect(Collectors.toList());
    }

    /**
     * 统计单条文档中词频数量
     *
     * @param indexName 索引名称
     * @param fieldName 字段名称
     * @param docId     文档ID
     * @return 词频，分词
     * @throws IOException
     */
    public List<Map<String, Object>> getWordFrequency(String indexName, String fieldName, String docId) throws IOException {

        // 一个索引、一个id来指定某个文档以及为其检索信息的字段
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        TermVectorsRequest request = new TermVectorsRequest(indexName, docId);
        request.setFields(fieldName);
        // 同步执行
        List<TermVectorsResponse.TermVector> termVectorsList = restHighLevelClient.termvectors(request, RequestOptions.DEFAULT).getTermVectorsList();
        // 返回指定文档中，分词及分词频率
        termVectorsList.get(0).getTerms().forEach(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("term_freq", e.getTermFreq());
                    map.put("term_name", e.getTerm());
                    list.add(map);
                }
        );

        return list;
    }

    /**
     * 高亮结果集 特殊处理 map转对象 JSONObject.parseObject(JSONObject.toJSONString(map), Content.class)
     *
     * @param searchResponse
     */
    private List<Map<String, Object>> transformHighlight(SearchResponse searchResponse) {
        SearchHit[] hits = searchResponse.getHits().getHits();
        if (ArrayUtil.isNotEmpty(hits)) {
            return Arrays.stream(hits).map(hit -> {
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                highlightFields.forEach((field, highlight) -> {
                    Text[] fragments = highlight.getFragments();
                    if (ArrayUtil.isNotEmpty(fragments)) {
                        String fieldHighLightVal = Arrays.stream(fragments).map(Text::string).collect(Collectors.joining());
                        sourceAsMap.put(field, fieldHighLightVal);
                    }
                });
                return sourceAsMap;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

}
