package com.lsh.milvus;

import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.ListDatabasesResponse;
import io.milvus.param.R;
import io.milvus.param.collection.CreateDatabaseParam;
import io.milvus.param.collection.DropDatabaseParam;
import io.milvus.param.partition.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@SpringBootTest
public class C3MilvusPartitionTest {

    @Autowired
    MilvusServiceClient milvusClient;

    /**
     * 创建分区
     * Milvus 允许您将大量矢量数据划分为少量分区。然后，可以将搜索和其他操作限制在一个分区内以提高性能。
     * 一个集合由一个或多个分区组成。在创建新集合时，Milvus 会创建一个默认分区_default。
     */
    @Test
    public void createPartition(){
        milvusClient.createPartition(
                CreatePartitionParam.newBuilder()
                        .withCollectionName("book")//CollectionName	要在其中创建分区的集合的名称。
                        .withPartitionName("novel") //PartitionName	要创建的分区的名称。
                        .build()
        );
        System.out.println("Create Partition Done.");

    }

    /**
     * 验证分区是否存在
     */
    @Test
    public void testHasPartition(){
        R<Boolean> respHasPartition = milvusClient.hasPartition(
                HasPartitionParam.newBuilder()
                        .withCollectionName("book")
                        .withPartitionName("novel")
                        .build()
        );
        if (respHasPartition.getData() == Boolean.TRUE) {
            System.out.println("Partition exists.");
        }

    }

    /**
     * 删除分区
     */
    @Test
    public void testDropPartition(){
        milvusClient.dropPartition(
                DropPartitionParam.newBuilder()
                        .withCollectionName("book")
                        .withPartitionName("novel")
                        .build()
        );

    }

    /**
     * 加载分区 load Partition
     * 将分区加载到内存中。将分区而不是整个集合加载到内存中可以显着减少内存使用量。Milvus 内的所有搜索和查询操作都在内存中执行。
     */
    @Test
    public void testLoadPartitions(){
        milvusClient.loadPartitions(
                LoadPartitionsParam.newBuilder()
                        .withCollectionName("book")
                        .withPartitionNames(Arrays.asList("novel"))
                .build()
    );

    }

    /**
     * 释放分区 release partition
     */
    @Test
    public void testReleasePartitions(){
        List<String> partitionNames = new ArrayList<>();
        partitionNames.add("novel");
        milvusClient.releasePartitions(
                ReleasePartitionsParam.newBuilder()
                        .withCollectionName("book")
                        .withPartitionNames(partitionNames)
                        .build()
        );

    }


    @Test
    public void test(){

    }


}
