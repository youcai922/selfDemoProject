package com.yc.esDemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yc.pojo.MusicModel;
import lombok.val;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yucan
 * @date 2022/12/6 15:07
 */
@SpringBootTest
class DocumentsQueryTests {

    @Resource
    private RestHighLevelClient client;

    @Test
    void contextLoads() {
    }

    /**
     * 查询所有
     *
     * @throws Exception
     */
    @Test
    public void searchAll() throws Exception {
        //创建查询构造器queryBuilder来指定查询matchAllQuery
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        List<Object> result = searchByQueryBuilder(client, queryBuilder);
        for (Object musicModel : result) {
            System.out.println(musicModel.toString());
        }
    }

    /**
     * 词条查询/精确查询term
     */
    @Test
    public void searchTerm() throws Exception {
        //创建查询构造器queryBuilder来指定查询matchAllQuery
        QueryBuilder queryBuilder = QueryBuilders.termQuery("singer.keyword","Charlie Puth");
        List<Object> result = searchByQueryBuilder(client, queryBuilder);
        for (Object musicModel : result) {
            System.out.println(musicModel.toString());
        }
    }

    /**
     * 分词查询match
     */
    @Test
    public void matchQuery() {
        try {
            //分词查询，分词器分为Charlie和Puth通过后面的Operator进行逻辑连接，AND为  **Charlie***Puth**,OR为**Charlie** 和**Puth**和**Charlie**Puth**
            QueryBuilder queryBuilder = QueryBuilders.matchQuery("singer", "Charlie Puth").operator(Operator.OR);
            System.out.println(searchByQueryBuilder(client, queryBuilder));
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 模糊查询wildcard
     */
    @Test
    public void wildcard() {
        try {
            //分词查询，分词器分为Charlie和Puth通过后面的Operator进行逻辑连接，AND为  **Charlie***Puth**,OR为**Charlie** 和**Puth**和**Charlie**Puth**
            WildcardQueryBuilder queryBuilder = QueryBuilders.wildcardQuery("singer.keyword", "England*");
            System.out.println(searchByQueryBuilder(client, queryBuilder));
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多个匹配查询
     */
    @Test
    public void multiMatchQuery() {
        try {
            //查询 country singer字段包含ce 的数据
            QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery("England", "country", "singer").operator(Operator.AND);
            System.out.println(searchByQueryBuilder(client, queryBuilder));
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 布尔查询
     */
    @Test
    public void boolQuery() {
        try {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            QueryBuilder queryBuilder_name = QueryBuilders.termQuery("name", "leon");
//            QueryBuilder queryBuilder_type = QueryBuilders.termQuery("type", "romance");
            //查询name 为 "leon" 或者 type 为 "romance"的数据
//            boolQueryBuilder.should(queryBuilder_name);
//            boolQueryBuilder.should(queryBuilder_type);
            QueryBuilder queryBuilder_type = QueryBuilders.termQuery("type", "action");
            //查询name为"leon"并且type为"action"的数据
//            boolQueryBuilder.must(queryBuilder_name);
//            boolQueryBuilder.must(queryBuilder_type);
            //查询date>1997-01-01的数据
//            RangeQueryBuilder rangeQueryBuilder =QueryBuilders.rangeQuery("date").gt("1997-01-01");
//            boolQueryBuilder.must(rangeQueryBuilder);
            //id范围查询
            int[] ids = {1, 2, 3};
            TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("_id", ids);
            boolQueryBuilder.must(termsQueryBuilder);
            System.out.println(searchByQueryBuilder(client, boolQueryBuilder));
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * String 搜索
     */
    @Test
    public void stringQuery() {
        try {
            //查询 含有 France的数据
            QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("France");
            System.out.println(searchByQueryBuilder(client, queryBuilder));
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 排序
     *
     * @throws IOException
     */
    @Test
    public void sort() throws IOException {
        SearchRequest searchRequest = new SearchRequest("movie");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(0).size(10);
        //设置一个可选的超时时间，用于可控制搜索允许的时间
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //将数据按date排序
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort("date").order(SortOrder.DESC);
        searchSourceBuilder.sort(sortBuilder);
        //指定返回字段
//        String[] includeFields = {"name", "director"};
//        String[] excludeFields = {"date"};
//        searchSourceBuilder.fetchSource(includeFields, excludeFields);
        //实现字段高亮显示
//        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        HighlightBuilder.Field highlightName = new HighlightBuilder.Field("name");
//        highlightBuilder.field(highlightName);
//        searchSourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        //获取热点数据
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] hits = searchHits.getHits();
        //将结果装进对象list中
        List<MusicModel> list = new ArrayList<>();
        for (SearchHit searchHit : hits) {
            String sourceAsString = searchHit.getSourceAsString();
            list.add(JSON.parseObject(sourceAsString, new TypeReference<MusicModel>() {
            }));
        }
        System.out.println(list);
        client.close();
    }

    /**
     * 聚合查询
     *
     * @throws IOException
     */
    @Test
    public void agg() throws IOException {
//        SearchRequest searchRequest = new SearchRequest("movie");
        SearchRequest searchRequest = new SearchRequest("people");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //按type分组
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("by_sex").field("sex.keyword");
        searchSourceBuilder.aggregation(aggregationBuilder);

        //求平均值和总和(按name分组)
//        //对应的json串
//        String json_es = "GET people/_search\n" +
//                            "{\n" +
//                            "  \"aggs\": {\n" +
//                            "    \"by_sex\":{\n" +
//                            "      \"terms\": {\n" +
//                            "        \"field\": \"sex.keyword\"\n" +
//                            "      },\n" +
//                            "      \"aggs\": {\n" +
//                            "        \"avg_age\": {\n" +
//                            "          \"avg\": {\n" +
//                            "            \"field\": \"age\"\n" +
//                            "          }\n" +
//                            "        },\n" +
//                            "        \"sum_age\":{\n" +
//                            "          \"sum\": {\n" +
//                            "            \"field\": \"age\"\n" +
//                            "          }\n" +
//                            "        }\n" +
//                            "      }\n" +
//                            "    }\n" +
//                            "  }\n" +
//                            "}";
//        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("by_sex").field("sex.keyword");
//        AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("avg_age").field("age");
//        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sum_age").field("age");
//        searchSourceBuilder.aggregation(aggregationBuilder.subAggregation(avgAggregationBuilder).subAggregation(sumAggregationBuilder));


        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Aggregations aggregations = searchResponse.getAggregations();
        //获取按type分组聚合结果
        Terms byTypeAggregation = aggregations.get("by_sex");
        for (Terms.Bucket bucket : byTypeAggregation.getBuckets()) {
            System.out.println("key:" + bucket.getKey());
            System.out.println("docCount:" + bucket.getDocCount());
        }


        //获取求平均值和总和聚合结果
//        Aggregations aggregations = searchResponse.getAggregations();
//        Terms byAgeAggregation = aggregations.get("by_sex");
//        for (Terms.Bucket bucket : byAgeAggregation.getBuckets()){
//            System.out.println("key:"+bucket.getKey());
//            System.out.println("docCount:"+bucket.getDocCount());
//            Avg avg = bucket.getAggregations().get("avg_age");
//            System.out.println("avg_age:"+avg.getValue());
//            Sum sum = bucket.getAggregations().get("sum_age");
//            System.out.println("sum:"+sum.getValue());
//        }

        client.close();
    }


    private static List searchByQueryBuilder(RestHighLevelClient client, QueryBuilder queryBuilder) throws IOException {
        //创建搜索请求对象指定搜索构造器，指定搜索的索引名称为music
        SearchRequest searchRequest = new SearchRequest("music");
        //创建搜索文档内容对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        //设置查询的起始索引位置和数量，以下表示从第1条开始，共返回1000条文档数据
        searchSourceBuilder.from(0).size(1000);
        searchRequest.source(searchSourceBuilder);
        //根据搜索请求返回结果
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        //创建搜索命中数对象对搜索返回对象中拿到命中数
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] hits = searchHits.getHits();
        //将结果装进对象list中
        List<MusicModel> list = new ArrayList<>();
        for (SearchHit searchHit : hits) {
            String sourceAsString = searchHit.getSourceAsString();
            list.add(JSON.parseObject(sourceAsString, new TypeReference<MusicModel>() {
            }));
        }
        return list;
    }

    @Test
    public void test(){

        System.out.println(Double.valueOf("1.0")/1);
    }
}
