package com.lsh.milvus;

import io.milvus.client.MilvusServiceClient;
import io.milvus.common.clientenum.ConsistencyLevelEnum;
import io.milvus.grpc.QueryResults;
import io.milvus.grpc.SearchResults;
import io.milvus.param.MetricType;
import io.milvus.param.R;
import io.milvus.param.collection.LoadCollectionParam;
import io.milvus.param.collection.ReleaseCollectionParam;
import io.milvus.param.dml.InsertParam;
import io.milvus.param.dml.QueryParam;
import io.milvus.param.dml.SearchParam;
import io.milvus.response.QueryResultsWrapper;
import io.milvus.response.SearchResultsWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;



@SpringBootTest
public class C6MilvusSearchQueryTest {

    final Integer SEARCH_K = 2; // TopK 要返回的最相似结果的数量。
    final String SEARCH_PARAM = "{\"nprobe\":10, \"offset\":0}";    // Params

    @Autowired
    MilvusServiceClient milvusClient;

    /**
     * Milvus 中的向量相似度搜索会计算查询向量与具有指定相似度度量的集合中的向量之间的距离，
     * 并返回最相似的结果。您可以通过指定过滤标量字段或主键字段的布尔表达式来执行混合搜索。
     */
    @Test
    public void search(){

        //Load Collection
        milvusClient.loadCollection(
                LoadCollectionParam.newBuilder()
                        .withCollectionName("book")
                        .build()
        );

        List<String> search_output_fields = Arrays.asList("book_id");
        List<List<Float>> search_vectors = Arrays.asList(Arrays.asList(0.1f, 0.2f));

        SearchParam searchParam = SearchParam.newBuilder()
                .withCollectionName("book")
                .withConsistencyLevel(ConsistencyLevelEnum.STRONG)
                .withMetricType(MetricType.L2)
                .withOutFields(search_output_fields)
                .withTopK(SEARCH_K)
                .withVectors(search_vectors)
                .withVectorFieldName("book_intro")
                .withParams(SEARCH_PARAM)
                .build();
        R<SearchResults> respSearch = milvusClient.search(searchParam);

        //检查最相似向量的主键值及其距离。
        SearchResultsWrapper wrapperSearch = new SearchResultsWrapper(respSearch.getData().getResults());
        System.out.println(wrapperSearch.getIDScore(0));
        System.out.println(wrapperSearch.getFieldData("book_id", 0));


        // 当搜索完成时，释放 Milvus 中加载的集合以减少内存消耗。
        milvusClient.releaseCollection(
                ReleaseCollectionParam.newBuilder()
                        .withCollectionName("book")
                        .build());

    }

    /**
     * 与向量相似性搜索不同，查询通过基于布尔表达式的标量过滤来检索向量。
     * Milvus 支持标量字段中的多种数据类型以及各种布尔表达式。
     * 布尔表达式对标量字段或主键字段进行过滤，并检索与过滤器匹配的所有结果。
     */
    @Test
    public void query(){

        milvusClient.loadCollection(
                LoadCollectionParam.newBuilder()
                        .withCollectionName("book")
                        .build()
        );

        /**
         *  进行查询
         * 以下示例过滤具有特定值的向量book_id，并返回结果的book_id字段 和。book_intro
         *
         * Milvus 支持专门为查询设置一致性级别。本主题中的示例将一致性级别设置为Strong。
         * 您还可以将一致性级别设置为Bounded、Session或Eventually。
         */
        List<String> query_output_fields = Arrays.asList("book_id", "word_count");
        QueryParam queryParam = QueryParam.newBuilder()
                .withCollectionName("book")
                .withConsistencyLevel(ConsistencyLevelEnum.STRONG)
                .withExpr("book_id in [2,4,6,8]")
                .withOutFields(query_output_fields)
                .withOffset(0L)
                .withLimit(10L)
                .build();
        R<QueryResults> respQuery = milvusClient.query(queryParam);

        /**
         * 检查返回的结果
         */
        QueryResultsWrapper wrapperQuery = new QueryResultsWrapper(respQuery.getData());
        System.out.println(wrapperQuery.getFieldWrapper("book_id").getFieldData());
        System.out.println(wrapperQuery.getFieldWrapper("word_count").getFieldData());
        /**
         * 进行查询时，可以追加count(*)到output_fields，这样 Milvus 就可以返回集合中实体的数量。
         * 如果要对满足特定条件的实体进行计数，请使用expr定义布尔表达式。
         * 当count(*)在 中使用时output_fields，该limit参数被禁用。
         */

    }


}
