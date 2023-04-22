# Team
### Information 

Lab03-4-34
汤玉磊 12111908
张未硕 12111905
Group Number: 307

### Contribution

| 成员   | 贡献比 | ER图 | 建表              | 数据导入                 |
| ------ | ------ | ---- | ----------------- | ------------------------ |
| 汤玉磊 | 50     | 修改 | 写DDL语句         | 从JSON读取数据，优化版本 |
| 张未硕 | 50     | 绘制 | 对照ER图检查Debug | 从JSON读取数据，测试基准 |

## Part-1 E-R Diagram

*ER图绘制：张未硕；修改：汤玉磊*

我们设计的ER图如下：

![](https://github.com/WhatWEat/CS307Project1/blob/main/Img/ERimage.jpg)

绘制软件：boardmix，https://boardmix.cn/

1.对于较难以描述的City、Entity.Category、SubReply等，我们分别为他们添加了键city_id、category_id、subReply_id以便于约束
2.SubReply依附于Reply存在，我们将其定义为弱实体集

## Part-2 Database Design
### Diagram By Datagrip



### 强实体集(5张)

#### Author

| 属性              | 类型        | 含义                     | 约束        |
| ----------------- | ----------- | ------------------------ | ----------- |
| author_id         | varchar(20) | 每一个author唯一确定的id | primary key |
| phone_number      | varchar(20) | 手机号                   | unique      |
| registration_time | timestamp   | 注册时间                 | not null    |

#### Post

| 属性         | 类型          | 含义                   | 约束        |
| ------------ | ------------- | ---------------------- | ----------- |
| post_id      | bigint        | 每一个post唯一确定的id | primary key |
| content      | varchar(1000) | post的内容             | not null    |
| title        | varchar(100)  | post的标题             | not null    |
| posting_time | timestamp     | 发帖时间               | not null    |

#### Reply

#### Category

#### Postingcity

### 弱实体集

#### Subreply

**表Subreply**是一个依附于**表Reply**的弱实体集

| 属性         | 类型          | 含义                      | 约束                               |
| ------------ | ------------- | ------------------------- | ---------------------------------- |
| reply_id     | bigint        | 来自表Reply的主键reply_id | foreign key, composite primary key |
| sub_reply_id | bigint        | 标记subreply的id          | composite primary key              |
| content      | varcahr(1000) | subreply的内容            | not null                           |
| stars        | bigint        | subreply的点赞            |                                    |

### 关系表(8张)

#### AuthorReply

**表Author**一对多于**表Reply**

| 属性      | 类型        | 含义                        | 约束                                |
| --------- | ----------- | --------------------------- | ----------------------------------- |
| author_id | varchar(20) | 来自表Author的主键author_id | foreigin key, composite primary key |
| reply_id  | bigint      | 来自表Reply的主键reply_id   | foreigin key, composite primary key |



#### SubreplyAuthor

#### AuthorWritePost

#### AuthorSharePost

#### AuthorLikePost

#### PostCategory

#### PostCity

#### PostReply



# Part-3 Data Import
