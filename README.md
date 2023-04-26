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



绘制软件：boardmix，https://boardmix.cn/

1.对于较难以描述的City、Entity.Category、SubReply等，我们分别为他们添加了键city_id、category_id、subReply_id以便于约束  
2.SubReply依附于Reply存在，我们将其定义为弱实体集

# Part-2 Database Design
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

| 属性              | 类型        | 含义              | 约束        |
| ----------------- | ----------- |-----------------| ----------- |
| reply_id         | BIGINT | 每一个reply唯一确定的id | primary key |
| content      |VARCHAR(1000) | replyd的内容       | not null      |
| stars | BIGINT   | reply的点赞数       |    |


#### Category
| 属性              | 类型          | 含义                 | 约束        |
| ----------------- |-------------|--------------------| ----------- |
| category_id         | BIGINT      | 每一个category唯一确定的id | primary key |
| category      | VARCHAR(30) | category的内容        | not null      |

#### Postingcity
| 属性              | 类型          | 含义             | 约束        |
| ----------------- |-------------|----------------| ----------- |
| city_id         | BIGINT      | 每一个city唯一确定的id | primary key |
| city      | VARCHAR(50) | city的名字        | not null      |
| country      | VARCHAR(50) | city所在的城市的名字   | not null      |
### 弱实体集

#### Subreply

**表Subreply**是一个依附于**表Reply**的弱实体集

| 属性         | 类型          | 含义                      | 约束                               |
| ------------ | ------------- | ------------------------- | ---------------------------------- |
| reply_id     | bigint        | 来自表Reply的主键reply_id | foreign key, composite primary key |
| sub_reply_id | bigint        | 标记subreply的id          | composite primary key              |
| content      | varcahr(1000) | subreply的内容            | not null                           |
| stars        | bigint        | subreply的点赞            |                                    |

### 关系表(9张)

#### AuthorReply

**表Author**一对多于**表Reply**

| 属性      | 类型        | 含义                        | 约束                                |
| --------- | ----------- | --------------------------- | ----------------------------------- |
| author_id | varchar(20) | 来自表Author的主键author_id | foreigin key, composite primary key |
| reply_id  | bigint      | 来自表Reply的主键reply_id   | foreigin key, composite primary key |

#### SubreplyAuthor
**表Author**一对多于**表SubReply**

| 属性      | 类型        | 含义                         | 约束                                |
| --------- | ----------- |----------------------------| ----------------------------------- |
| author_id | varchar(20) | 来自表Author的主键author_id      | foreigin key, composite primary key |
| sub_reply_id  | bigint      | 来自表SubReply的主键sub_reply_id | foreigin key, composite primary key |


#### AuthorWritePost
**表Author**一对多于**表Post**

| 属性        | 类型        | 含义                    | 约束                                |
|-----------| ----------- |-----------------------| ----------------------------------- |
| author_id | varchar(20) | 来自表Author的主键author_id | foreigin key, composite primary key |
| post_id   | bigint      | 来自表Post的主键post_id     | foreigin key, composite primary key |

#### AuthorSharePost
**表Author**一对多于**表Post**

| 属性        | 类型        | 含义                    | 约束                                |
|-----------| ----------- |-----------------------| ----------------------------------- |
| author_id | varchar(20) | 来自表Author的主键author_id | foreigin key, composite primary key |
| post_id   | bigint      | 来自表Post的主键post_id     | foreigin key, composite primary key |

#### AuthorLikePost
**表Author**一对多于**表Post**

| 属性        | 类型        | 含义                    | 约束                                |
|-----------| ----------- |-----------------------| ----------------------------------- |
| author_id | varchar(20) | 来自表Author的主键author_id | foreigin key, composite primary key |
| post_id   | bigint      | 来自表Post的主键post_id     | foreigin key, composite primary key |
#### AuthorFavoritePost
**表Author**一对多于**表Post**

| 属性        | 类型        | 含义                    | 约束                                |
|-----------| ----------- |-----------------------| ----------------------------------- |
| author_id | varchar(20) | 来自表Author的主键author_id | foreigin key, composite primary key |
| post_id   | bigint      | 来自表Post的主键post_id     | foreigin key, composite primary key |

#### PostCategory
**表Post**一对多于**表Category**

| 属性        | 类型        | 含义                        | 约束                                |
|-----------| ----------- |---------------------------| ----------------------------------- |
| post_id   | bigint      | 来自表Post的主键post_id         | foreigin key, composite primary key |
| category_id   | bigint      | 来自表Category的主键category_id | foreigin key, composite primary key |

#### PostCity
**表Post**一对一于**表City**

| 属性        | 类型        | 含义                | 约束                                |
|-----------| ----------- |-------------------| ----------------------------------- |
| post_id   | bigint      | 来自表Post的主键post_id | foreigin key, composite primary key |
| city_id   | bigint      | 来自表City的主键city_id | foreigin key, composite primary key |

#### PostReply
**表Post**一对多于**表Reply**

| 属性       | 类型        | 含义                  | 约束                                |
|----------| ----------- |---------------------| ----------------------------------- |
| post_id  | bigint      | 来自表Post的主键post_id   | foreigin key, composite primary key |
| reply_id | bigint      | 来自表reply的主键reply_id | foreigin key, composite primary key |



# Part-3 Data Import
### 3.1 Basic
#### Step1 读取文件与储存数据
1.导入数据前，将json文件放在同一目录下，在项目结构中添加json需要的jar包  
我们建立了Author、Post、Category、Reply、SubReply、City六个类用于储存数据  
2.使用FileReader读取文件，逐个读取json文件中的各项数据  
3.用不同的键读取不同的数据封装为不同的类，储存在相应的Arraylist中
###### Author
包含Author的用户名、ID、注册时间和电话号码
###### Category
包含Category的ID以及内容
###### City
包含City的ID、名字、所在的国家
###### Post
作为最重要的类之一，每个Post储存的内容可以分为以下三个部分：  
（1）Post的Post的ID、发表时间、发布城市、标题、内容、作者  
（2）Post的标签以及其带有的reply（分别存在Arraylist中）  
（3）like、follow、favorite、share这篇post的作者（分别存在Arraylist中）
###### Reply
每个Reply中储存的内容可以分为以下两个部分：  
（1）Reply的ID、内容、Stars、作者  
（2）Reply带有的SubReply（存在Arraylist中）
###### SubReply
包含每个SubReply的ID、内容、Stars、作者
#### Step2 导入数据
1.准备JDBC驱动程序  
2.创建一个Connection对象并连接数据库（需要指定URL、用户名、密码等）  
3.创建一个Statement对象，用于执行sql语句  
4.利用循环将此前存在Arraylist中的各种数导入到数据库相应的表中

### 3.2 Advanced
我们共使用了五种不同的导入数据的方法
