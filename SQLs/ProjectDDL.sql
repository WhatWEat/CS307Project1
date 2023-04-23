-- select count(*)
-- from movies;
--
-- select count(*)
-- from countries;
--
-- select m.title, c.country_name, m.year_released, m.runtime
-- from movies m
--          join countries c
--               on m.country = c.country_code
-- limit 1 offset 10;

DROP TABLE IF EXISTS
    PostReply, AuthorReply,PostingCity, PostCity, AuthorWritePost,AuthorFavoritePost, AuthorLikePost, AuthorSharePost,SubReplyAuthor,AuthorFollowPost,
    SubReply,Author,Reply,Post, Category, PostCategory;
CREATE TABLE IF NOT EXISTS Author
(
    author_id         VARCHAR(20) primary key,
    registration_time TIMESTAMP          not null,
    phone_number      VARCHAR(20) unique not null,
    name              varchar(50) unique not null
    );
CREATE TABLE IF NOT EXISTS Reply
(
    reply_id BIGINT primary key,
    content  VARCHAR(1000) not null,
    stars    BIGINT
    );
-- author and reply relationship
CREATE TABLE IF NOT EXISTS AuthorReply
(
    author_id VARCHAR(20) not null,
    reply_id  BIGINT      not null,

    foreign key (author_id) references Author (author_id),
    foreign key (reply_id) references Reply (reply_id),

    primary key (reply_id, author_id)
    );
-- weak entity set for subreply
CREATE TABLE IF NOT EXISTS SubReply
(
    reply_id     BIGINT        not null,
    sub_reply_id BIGINT UNIQUE not null,
    content      VARCHAR(1000) not null,
    stars        BIGINT,

    foreign key (reply_id) references Reply (reply_id),

    primary key (reply_id, sub_reply_id)
    );
CREATE TABLE IF NOT EXISTS Post
(
    post_id      BIGINT primary key,
    title        VARCHAR(100)  not null,
    content      VARCHAR(1000) not null,
    posting_time TIMESTAMP     not null
    );
-- post and reply relationship
CREATE TABLE IF NOT EXISTS PostReply
(
    post_id  BIGINT not null,
    reply_id BIGINT not null,

    foreign key (post_id) references Post (post_id),
    foreign key (reply_id) references Reply (reply_id),

    primary key (reply_id, post_id)
    );

CREATE TABLE IF NOT EXISTS PostingCity
(
    city_id BIGINT primary key,
    city    varchar(50) not null,
    country varchar(50) not null,

    CONSTRAINT country_city UNIQUE (city, country)

    );

CREATE TABLE IF NOT EXISTS Category
(
    category_id BIGINT primary key,
    category    varchar(30) not null
    );

CREATE TABLE IF NOT EXISTS PostCity
(
    post_id BIGINT not null,
    city_id BIGINT not null,

    foreign key (post_id) references post (post_id),
    foreign key (city_id) references PostingCity (city_id),

    primary key (post_id, city_id)
    );

CREATE TABLE IF NOT EXISTS PostCategory
(
    category_id BIGINT not null,
    post_id     BIGINT not null,

    foreign key (category_id) references Category (category_id),
    foreign key (post_id) references Post (post_id),

    primary key (category_id, post_id)
    );
CREATE TABLE IF NOT EXISTS SubReplyAuthor
(
    author_id    varchar(20) not null,
    sub_reply_id BIGINT      not null,

    foreign key (author_id) references Author (author_id),
    foreign key (sub_reply_id) references SubReply (sub_reply_id),

    primary key (author_id, sub_reply_id)
    );
CREATE TABLE IF NOT EXISTS AuthorWritePost
(
    post_id   BIGINT      not null,
    author_id varchar(20) not null,

    foreign key (post_id) references post (post_id),
    foreign key (author_id) references Author (author_id),

    primary key (post_id, author_id)
    );

CREATE TABLE IF NOT EXISTS AuthorFollowPost
(
    post_id   BIGINT      not null,
    author_id varchar(20) not null,

    foreign key (post_id) references post (post_id),
    foreign key (author_id) references Author (author_id),

    primary key (post_id, author_id)
    );

CREATE TABLE IF NOT EXISTS AuthorFavoritePost
(
    post_id   BIGINT      not null,
    author_id varchar(20) not null,

    foreign key (post_id) references post (post_id),
    foreign key (author_id) references Author (author_id),

    primary key (post_id, author_id)
    );

CREATE TABLE IF NOT EXISTS AuthorLikePost
(
    post_id   BIGINT      not null,
    author_id varchar(20) not null,

    foreign key (post_id) references post (post_id),
    foreign key (author_id) references Author (author_id),

    primary key (post_id, author_id)
    );

CREATE TABLE IF NOT EXISTS AuthorSharePost
(
    post_id   BIGINT      not null,
    author_id varchar(20) not null,

    foreign key (post_id) references post (post_id),
    foreign key (author_id) references Author (author_id),

    primary key (post_id, author_id)
    );
