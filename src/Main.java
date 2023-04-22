import Entity.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {
    static ArrayList<Author> authors = new ArrayList<>();
    static ArrayList<Category> categories = new ArrayList<>();
    static ArrayList<Post> posts = new ArrayList<>();
    static ArrayList<City> cities = new ArrayList<>();

    static ArrayList<Reply> replies = new ArrayList<>();

    static ArrayList<SubReply> subReplies = new ArrayList<>();

    public static void main(String[] args) {
        loadPost();
        loadReply();
        /* TODO:
            1. try to finish the loadReply() static method in Main.java like loadPost() method
            2. start to design the framework of the insert part
        */
//        System.out.println("author_id,registration_time,phone_number,author_name");
//        for (Author author : authors) {
//            System.out.println(author.id + ";" + author.registerTime + ";" + author.phoneNumber + ";" + author.name);
//        }
    }

    static Author createAuthor(String name) {
        Author author = new Author();
        author.name = name;
        return author;
    }

    static Author findIndex(ArrayList<Author> authors, String name) {
        for (Author temp : authors) {
            if (temp.name.equals(name)) {
                return temp;
            }
        }
        Author author = new Author(name);
        authors.add(author);
        return author;
    }

    static Post findPost(ArrayList<Post> posts, Long post_id) {
        for (Post temp : posts) {
            if (temp.post_id.equals(post_id)) {
                return temp;
            }
        }
        return null;
    }

    static void loadPost() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("posts.json"));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonString.append(line); //逐行读取JSON字符串
            }
            bufferedReader.close(); //关闭文件流
            JSONArray jsonArray = new JSONArray(jsonString.toString());


            for (int i = 0; i < jsonArray.length(); i++) {
                // basic information
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("Title");
                Long id = jsonObject.getLong("Post ID");
                String content = jsonObject.getString("Content");
                String postTime = jsonObject.getString("Posting Time");
                Post post = new Post(id, title, content, postTime);
                posts.add(post);

                // city
                String data = jsonObject.getString("Posting City");
                int lastPeriodIndex = data.lastIndexOf(", "); // 找到最后一个 “。 ” 的位置
                String part1 = data.substring(0, lastPeriodIndex); // 截取分割后的第一部分
                String part2 = data.substring(lastPeriodIndex + 2).trim(); // 截取分割后的第二部分
//                String[] values = data.split(", ");
                City city = new City(part1, part2);
                if (cities.contains(city)) {
                    City.id--;
                } else {
                    cities.add(city);
                }
                post.city = city;

                //author
                Author author = new Author();
                author.id = jsonObject.getString("Author's ID");
                author.phoneNumber = jsonObject.getString("Author's Phone");
                author.registerTime = jsonObject.getString("Author Registration Time");
                author.name = jsonObject.getString("Author");
                if (!authors.contains(author)) {
                    authors.add(author);
                }
                post.author = author;

                //category
                JSONArray categoryArray = jsonObject.getJSONArray("Category");

                for (int j = 0; j < categoryArray.length(); j++) {
                    Category category = new Category(categoryArray.getString(j));
                    if (categories.contains(category)) {
                        Category.id--;
                        int index = categories.indexOf(category);
                        post.categories.add(categories.get(index));
                    } else {
                        categories.add(category);
                        post.categories.add(category);
                    }
                }
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // Followed By
                JSONArray followedByArray = jsonObject.getJSONArray("Authors Followed By");
                for (int j = 0; j < followedByArray.length(); j++) {
                    String name = followedByArray.getString(j);
                    Author author = findIndex(authors, name);
                    posts.get(i).follow.add(author);
                }
                // Favorited
                JSONArray favoriteArray = jsonObject.getJSONArray("Authors Who Favorited the Post");
                for (int j = 0; j < favoriteArray.length(); j++) {
                    String name = favoriteArray.getString(j);
                    Author author = findIndex(authors, name);
                    posts.get(i).like.add(author);
                }
            }

            //like
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray authorLike = jsonObject.getJSONArray("Authors Who Liked the Post");
                for (int j = 0; j < authorLike.length(); j++) {
                    posts.get(i).like.add(findIndex(authors, authorLike.getString(j)));
                }
            }
            //shared
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray authorShare = jsonObject.getJSONArray("Authors Who Shared the Post");
                for (int j = 0; j < authorShare.length(); j++) {
                    posts.get(i).share.add(findIndex(authors, authorShare.getString(j)));
                }
            }
            System.out.println("Post.json is Finished!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void loadReply() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("replies.json"));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonString.append(line); //逐行读取JSON字符串
            }
            bufferedReader.close(); //关闭文件流
            JSONArray jsonArray = new JSONArray(jsonString.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                long post_id = jsonObject.getLong("Post ID");
                String content = jsonObject.getString("Reply Content");
                long stars = jsonObject.getLong("Reply Stars");
                String replyAuthor = jsonObject.getString("Reply Author");
                String subReplyContent = jsonObject.getString("Secondary Reply Content");
                long subReplyStars = jsonObject.getLong("Secondary Reply Stars");
                String subReplyAuthor = jsonObject.getString("Secondary Reply Author");
                //reply  reply&author
                Author authorOfReply = findIndex(authors, replyAuthor);
                Reply reply = new Reply(content, stars, authorOfReply);
                replies.add(reply);
                //reply&post
                Post post = findPost(posts, post_id);
                if (post != null) {
                    post.replies.add(reply);
                }
                //subReply  subReply&Author  subReply&Reply
                Author authorOfSubReply = findIndex(authors, subReplyAuthor);
                SubReply subReply = new SubReply(subReplyContent, subReplyStars, authorOfSubReply);
                subReplies.add(subReply);
                reply.subReplies.add(subReply);

            }
            System.out.println("Reply.json is Finished!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}