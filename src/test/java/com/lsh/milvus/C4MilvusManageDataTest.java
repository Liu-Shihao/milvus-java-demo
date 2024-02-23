package com.lsh.milvus;

import io.milvus.client.MilvusServiceClient;
import io.milvus.param.dml.InsertParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * 通过客户端向 Milvus 中插入数据。
 * 要插入的数据。要插入的数据的数据类型必须与集合的模式匹配，否则 Milvus 将引发异常。
 */
@SpringBootTest
public class C4MilvusManageDataTest {

    @Autowired
    MilvusServiceClient milvusClient;



    /**
     *向 Milvus 插入数据
     * 将数据插入到集合中。通过指定partition_name，您可以选择决定将数据插入到哪个分区。
     * 将实体插入到之前已建立索引的集合中后，无需重新索引该集合，因为 Milvus 会自动为新插入的数据创建索引。
     */
    @Test
    public void insertMilvus(){
        List<InsertParam.Field> fields = randomData();
        InsertParam insertParam = InsertParam.newBuilder()
                .withCollectionName("book")
                .withPartitionName("novel")
                .withFields(fields)
                .build();
        milvusClient.insert(insertParam);
        System.out.println("Insert Data Done");
    }

    public  List<InsertParam.Field> randomData(){
        Random ran = new Random();
        List<Long> bookIdArray = new ArrayList<>();
        List<Long> wordCountArray = new ArrayList<>();
        List<List<Float>> bookIntroArray = new ArrayList<>();
        for (long i = 0L; i < 2000; ++i) {
            bookIdArray.add(i);
            wordCountArray.add(i + 10000);
            List<Float> vector = new ArrayList<>();
            for (int k = 0; k < 2; ++k) {
                vector.add(ran.nextFloat());
            }
            bookIntroArray.add(vector);
        }

        List<InsertParam.Field> fields = new ArrayList<>();
        fields.add(new InsertParam.Field("book_id", bookIdArray));
        fields.add(new InsertParam.Field("word_count", wordCountArray));
        fields.add(new InsertParam.Field("book_intro", bookIntroArray));
        return fields;
    }

    /**
     * 刷新 Milvus 中的数据
     * 当数据插入到 Milvus 中时，它是分段存储的。段必须达到一定的大小才能被密封和索引。
     * 使用强力搜索未密封的段。如果需要插入后立即查找数据，可以 flush() 在插入数据后调用该方法。
     * 此方法密封所有剩余的段并将其发送以进行索引。仅在插入会话结束时调用此方法非常重要。
     * 过于频繁地调用它会导致数据碎片化，需要稍后进行清理。
     */

    @Test
    public void test(){

    }


}
