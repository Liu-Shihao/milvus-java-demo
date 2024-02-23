package com.lsh.milvus;

import io.milvus.client.MilvusServiceClient;
import io.milvus.common.clientenum.ConsistencyLevelEnum;
import io.milvus.grpc.QueryResults;
import io.milvus.grpc.SearchResults;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import io.milvus.param.R;
import io.milvus.param.collection.LoadCollectionParam;
import io.milvus.param.collection.ReleaseCollectionParam;
import io.milvus.param.dml.QueryParam;
import io.milvus.param.dml.SearchParam;
import io.milvus.param.index.CreateIndexParam;
import io.milvus.param.index.DropIndexParam;
import io.milvus.response.QueryResultsWrapper;
import io.milvus.response.SearchResultsWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;


/**
 *
 */
@SpringBootTest
public class C5MilvusIndexTest {

    //以下示例构建一个 1024 簇 IVF_FLAT 索引，并使用欧几里德距离 (L2) 作为相似性度量。
    final IndexType INDEX_TYPE = IndexType.IVF_FLAT;   // IndexType
    final String INDEX_PARAM = "{\"nlist\":1024}";     // ExtraParam

    @Autowired
    MilvusServiceClient milvusClient;

    /**
     * 矢量索引是用于加速矢量相似性搜索的元数据的组织单元。您需要先创建一个索引，然后才能针对您的 Milvus 执行 ANN 搜索。
     * Build an Index on Vectors 建立向量索引
     */
    @Test
    public void createIndex(){

        milvusClient.createIndex(
                CreateIndexParam.newBuilder()
                        .withCollectionName("book")
                        .withFieldName("book_intro")
                        .withIndexType(INDEX_TYPE)
                        .withMetricType(MetricType.L2)
                        .withExtraParam(INDEX_PARAM)
                        .withSyncMode(Boolean.FALSE)
                        .build()
        );
        System.out.println("Build an Index on Vectors Done.");
    }

    /**
     * 在标量上建立索引 Build an Index on Scalars
     * 在标量字段上构建索引
     * 与同时具有大小和方向的矢量不同，标量仅具有大小。Milvus 将单个数字和字符串视为标量。
     * 要在标量字段上构建索引，您不需要设置任何索引参数。标量字段索引名称的默认值为_default_idx_<fieldId>后跟索引字段的名称。您可以将其设置为另一个合适的值。
     *
     * 创建索引后，您可以在向量相似性搜索中在此字符串字段上包含布尔表达式
     */
    @Test
    public void test(){

    }

    @Test
    public void dropIndex(){
        milvusClient.dropIndex(
                DropIndexParam.newBuilder()
                        .withCollectionName("book")
                        .withIndexName("book_intro")
                        .build()
        );
        System.out.println("Drop Index Done.");
    }

}
