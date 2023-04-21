## Part1 ER图

*ER图绘制：张未硕；修改：汤玉磊*

我们设计的ER图如下：



绘制软件：boardmix，https://boardmix.cn/

1.对于较难以描述的City、Category、SubReply等，我们分别为他们添加了键city_id、category_id、subReply_id以便于约束
2.SubReply依附于Reply存在，我们将其定义为弱实体集

## Part2 建表
*建表：汤玉磊*

datagrip生成的ER图如下：


###### Author
这个表中记录所有Author的信息  
Author_ID：代表Author的唯一ID，是Author的主键  
Registration_Time：代表Author的注册时间，不可为空  
Phone_Number：代表Author的电话号码

###### Post
这个表中记录所有Post的信息
Post_ID：代表Post的唯一ID，是Post的主键
Title：代表Post的标题，不可为空
Content：代表Post的内容，不可为空
Posting_Time：代表Post的发表时间，不可为空

###### Reply
这个表中记录所有Post的Reply的信息
Reply_ID：代表Reply的唯一ID，是Reply的主键
Content：代表Reply的内容，不可为空
Stars：代表Reply的星星，不可为空


