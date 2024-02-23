package com.lsh.milvus;

import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.ListDatabasesResponse;
import io.milvus.param.R;
import io.milvus.param.collection.CreateDatabaseParam;
import io.milvus.param.collection.DropDatabaseParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class C1MilvusDatabaseTest {

    @Autowired
    MilvusServiceClient milvusClient;


    @Test
    public void createDb(){
        CreateDatabaseParam createDatabaseParam = CreateDatabaseParam.newBuilder()
                .withDatabaseName("book")
                .build();

        milvusClient.createDatabase(createDatabaseParam);
        System.out.println("create Database done.");
        dbList();
    }

    @Test
    public void dbList(){
        R<ListDatabasesResponse> listDatabasesResponseR = milvusClient.listDatabases();
        for (String dbName : listDatabasesResponseR.getData().getDbNamesList()) {
            System.out.println(dbName);

        }
    }

    @Test
    public void deleteDb(){
        dbList();
        DropDatabaseParam dropDatabaseParam = DropDatabaseParam.newBuilder()
                .withDatabaseName("book")
                .build();
        milvusClient.dropDatabase(dropDatabaseParam);
        System.out.println("==============");
        dbList();

    }
}
