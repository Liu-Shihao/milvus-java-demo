package com.lsh.milvus;


import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.*;
import io.milvus.param.R;
import io.milvus.param.alias.CreateAliasParam;
import io.milvus.param.alias.DropAliasParam;
import io.milvus.param.collection.*;
import io.milvus.response.DescCollResponseWrapper;
import io.milvus.response.GetCollStatResponseWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class C2MilvusCollectionTest {
    @Autowired
    MilvusServiceClient milvusClient;



    /**
     * 创建Collection
     * 必要参数：field schema, collection schema, collection name
     * 创建一个 2分区 的cllection: book
     * 主键 book_id
     * word_count   INT64
     * book_intro   二维浮点向量字段
     */
    @Test
    public void createCollection(){
        FieldType fieldType1 = FieldType.newBuilder()
                .withName("book_id")
                .withDataType(DataType.Int64)
                .withPrimaryKey(true)
                .withAutoID(false)
                .build();
        FieldType fieldType2 = FieldType.newBuilder()
                .withName("word_count")
                .withDataType(DataType.Int64)
                .build();
        FieldType fieldType3 = FieldType.newBuilder()
                .withName("book_intro")
                .withDataType(DataType.FloatVector)
                .withDimension(2)
                .build();
        CreateCollectionParam createCollectionReq = CreateCollectionParam.newBuilder()
                .withCollectionName("book")
                .withDescription("Test book search")
                .withShardsNum(2)
                .addFieldType(fieldType1)
                .addFieldType(fieldType2)
                .addFieldType(fieldType3)
                .withEnableDynamicField(true)
                .build();

        milvusClient.createCollection(createCollectionReq);
        System.out.println("create collection done.");

    }

    /**
     *检查集合是否存在
     */
    @Test
    public void hasCollection(){
        R<Boolean> respHasCollection = milvusClient.hasCollection(
                HasCollectionParam.newBuilder()
                        .withCollectionName("book")
                        .build()
        );
        if (respHasCollection.getData() == Boolean.TRUE) {
            System.out.println("Collection exists.");
        }

    }

    /**
     * 查看集合详情
     */
    @Test
    public void describeCollection(){
        R<DescribeCollectionResponse> respDescribeCollection = milvusClient.describeCollection(
                // Return the name and schema of the collection.
                DescribeCollectionParam.newBuilder()
                        .withCollectionName("book")
                        .build()
        );
        DescCollResponseWrapper wrapperDescribeCollection = new DescCollResponseWrapper(respDescribeCollection.getData());
        System.out.println(wrapperDescribeCollection);

        R<GetCollectionStatisticsResponse> respCollectionStatistics = milvusClient.getCollectionStatistics(
                // Return the statistics information of the collection.
                GetCollectionStatisticsParam.newBuilder()
                        .withCollectionName("book")
                        .build()
        );
        GetCollStatResponseWrapper wrapperCollectionStatistics = new GetCollStatResponseWrapper(respCollectionStatistics.getData());
        System.out.println("Collection row count: " + wrapperCollectionStatistics.getRowCount());

    }

    /**
     * 列出所有集合
     */
    @Test
    public void showCollections(){
        R<ShowCollectionsResponse> respShowCollections = milvusClient.showCollections(
                ShowCollectionsParam.newBuilder().build()
        );
        System.out.println(respShowCollections);


    }

    /**
     * 删除一个集合
     */
    @Test
    public void dropCollectionTest(){
        milvusClient.dropCollection(
                DropCollectionParam.newBuilder()
                        .withCollectionName("book")
                        .build()
        );
        System.out.println("Drop Collection Done.");

    }

    /**
     * 创建集合别名
     */
    @Test
    public void createCllectionAlias(){
        milvusClient.createAlias(
                CreateAliasParam.newBuilder()
                        .withCollectionName("book")
                        .withAlias("publication")
                        .build()
        );
        System.out.println("Create Collection Alias Done.");
    }

    /**
     * 删除集合别名
     */
    @Test
    public void dropCllectionAlias(){

        milvusClient.dropAlias(
                DropAliasParam.newBuilder()
                        .withAlias("publication")
                        .build()
        );
        System.out.println("Drop Collection Alias Done.");

    }

    /**
     * 加载集合 Load a Collection
     * 如何在搜索或查询之前将集合加载到内存中。Milvus 内的所有搜索和查询操作都在内存中执行。您应该在加载集合之前创建索引。
     */
    @Test
    public void loadCollection2Memory(){
        String collectionName = "book";

        milvusClient.loadCollection(
                LoadCollectionParam.newBuilder()
                        .withCollectionName(collectionName)
                        .build()
        );

        // You can check the loading status

        GetLoadStateParam getLoadStateParam = GetLoadStateParam.newBuilder()
                .withCollectionName(collectionName)
                .build();
        R<GetLoadStateResponse> getLoadStateResponseR = milvusClient.getLoadState(getLoadStateParam);
        if (getLoadStateResponseR.getStatus() != R.Status.Success.getCode()) {
            System.out.println(getLoadStateResponseR.getMessage());
        }
        System.out.println(getLoadStateResponseR.getData().getState());

        // and loading progress as well

        GetLoadingProgressParam getLoadingProgressParam = GetLoadingProgressParam.newBuilder()
                .withCollectionName(collectionName)
                .build();
        R<GetLoadingProgressResponse> getLoadingProgressResponseR = milvusClient.getLoadingProgress(getLoadingProgressParam);
        if (getLoadingProgressResponseR.getStatus() != R.Status.Success.getCode()) {
            System.out.println(getLoadingProgressResponseR.getMessage());
        }
        System.out.println(getLoadingProgressResponseR.getData().getProgress());

    }

    /**
     * Release Collection  发布集合
     * 在搜索或查询后从内存中释放集合以减少内存使用
     */
    @Test
    public void releaseCollection(){
        milvusClient.releaseCollection(
                ReleaseCollectionParam.newBuilder()
                        .withCollectionName("book")
                        .build()
        );

    }


    @Test
    public void test(){

    }


}
