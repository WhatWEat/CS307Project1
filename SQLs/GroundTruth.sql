-- How many posts are there in total?
-- 203
select count(*)
from post;

-- How many likes in post with Post ID 163?
-- 25
select count(*)
from authorlikepost
where post_id = 163;

-- How many likes in post with Post ID 28?
-- 2
select count(*)
from authorlikepost
where post_id = 28;

-- How many likes in post with Post ID 6?
-- 24
select count(*)
from authorlikepost
where post_id = 6;

-- How many favorites in post with Post ID 163?
-- 41
select count(*)
from authorfavoritepost
where post_id = 163;

-- How many favorites in post with Post ID 28?
-- 13
select count(*)
from authorfavoritepost
where post_id = 28;

-- How many favorites in post with Post ID 6?
-- 3
select count(*)
from authorfavoritepost
where post_id = 6;

-- How many shares in post with Post ID 163?
-- 19
select count(*)
from authorsharepost
where post_id = 163;

-- How many shares in post with Post ID 28?
-- 10
select count(*)
from authorsharepost
where post_id = 28;

-- How many shares in post with Post ID 6?
-- 25
select count(*)
from authorsharepost
where post_id = 6;

-- List the information of novel_expert.
-- ('77299519840727839X', '18889727122', '2010-06-04 11:07:08')
select author_id, phone_number, registration_time
from author
where name = 'novel_expert';

-- List the information of creative_hat.
-- ('102053196407095853', '18288072303', '2021-11-02 15:10:15')
select author_id, phone_number, registration_time
from author
where name = 'creative_hat';

-- List the information of embarrassed_guitar.
-- ('154877196111232324', '13322606476', '2010-08-24 15:03:48')
select author_id, phone_number, registration_time
from author
where name = 'embarrassed_guitar';

-- What is the ID of the post with the highest number of likes?
-- [85, 120], with value: 100
SELECT DISTINCT post_id, count
FROM (SELECT post_id, COUNT(*) OVER (PARTITION BY post_id) AS count
      FROM authorlikepost) AS t
WHERE count = (SELECT MAX(count)
               FROM (SELECT COUNT(*) OVER (PARTITION BY post_id) AS count
                     FROM authorlikepost) AS tt);

-- What is the ID of the post with the highest number of favorites??
-- [19, 23, 78, 114], with value: 50
SELECT DISTINCT post_id, count
FROM (SELECT post_id, COUNT(*) OVER (PARTITION BY post_id) AS count
      FROM authorfavoritepost) AS t
WHERE count = (SELECT MAX(count)
               FROM (SELECT COUNT(*) OVER (PARTITION BY post_id) AS count
                     FROM authorfavoritepost) AS tt);

-- What is the ID of the post with the highest number of shares?
-- [46, 50, 52, 65, 72, 191], with value: 30
SELECT DISTINCT post_id, count
FROM (SELECT post_id, COUNT(*) OVER (PARTITION BY post_id) AS count
      FROM authorsharepost) AS t
WHERE count = (SELECT MAX(count)
               FROM (SELECT COUNT(*) OVER (PARTITION BY post_id) AS count
                     FROM authorsharepost) AS tt);

-- Who is the author who follows the most people?
-- ['young_contribution'], with value: 17


-- Who is the author with the highest number of followers?
-- ['realistic_negative'], with value: 4


-- What is the Post ID of the earliest post?
-- 129
select post_id
from post
order by posting_time
limit 1;

-- What is the Post ID of the latest post?
-- 59
select post_id
from post
order by posting_time desc
limit 1;

-- How many posts are there in 2020?
-- 18
SELECT count(*)
FROM post
WHERE posting_time >= '2020-01-01' AND posting_time < '2021-01-01';

-- How many posts are there in 2021?
-- 29
SELECT count(*)
FROM post
WHERE posting_time >= '2021-01-01' AND posting_time < '2022-01-01';

-- How many posts are there in 2022?
-- 27
SELECT count(*)
FROM post
WHERE posting_time >= '2022-01-01' AND posting_time < '2023-01-01';

select count(*)from author;
