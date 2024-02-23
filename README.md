## Milvus Vector Database 
官方文档：https://milvus.io/docs/overview.md


## Java API Docs
https://milvus.io/api-reference/java/v2.3.x/About.md



## Milvus 官方示例
https://milvus.io/milvus-demos/

## Integrate With Hugging Face 与Huggingface集成

https://milvus.io/docs/integrate_with_hugging-face.md


## Reference

https://zhuanlan.zhihu.com/p/476025527


## Note

Milvus是向量数据库，存储、索引 embedding vector的数据库，但是不是转换embedding，需要借助模型将数据转换为embedding。

Milvus可以存储非结构化数据。

作为专门为处理输入向量查询而设计的数据库，它能够对万亿规模的向量进行索引。与现有的关系数据库主要处理遵循预定义模式的结构化数据不同，Milvus 是自下而上设计的，旨在处理从非结构化数据转换而来的嵌入向量。

为了让计算机理解和处理非结构化数据，使用嵌入技术将这些数据转换为向量。Milvus 存储并索引这些向量。Milvus 能够通过计算两个向量的相似距离来分析它们之间的相关性。如果两个嵌入向量非常相似，则意味着原始数据源也相似。





