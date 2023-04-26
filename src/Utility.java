import Entity.*;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class Utility {
    public static long termNumber = 0;
    public static long totalTime = 0;
    public static Random random = new Random();

    public static int mul = 2;

    public static void addCount(long dis, long count, String name) {
        termNumber += count;
        totalTime += dis;
        double timeUnit = (count * 1000.0) / dis;
//        System.out.println(count + name + " records successfully loaded");
//        System.out.println("Loading speed : " + timeUnit + " records/s");
    }

    public static long getTotalTime() {
        return totalTime;
    }

    public static long getAverageTime() {
        long result = termNumber * 1000 / totalTime;
        termNumber = 0;
        totalTime = 0;
        return result;
    }

    //需要输入url，user，password的值
    public static void clearDataBase(String url, String user, String password) {
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName("org.postgresql.Driver");

            conn = DriverManager.getConnection(url, user, password);

            stmt = conn.createStatement();

            // 清空表中所有数据
            ArrayList<String> SQL = getSql("src\\sql.txt");

            for (String sql : SQL) {
                stmt.executeUpdate(sql);
            }

//            System.out.println("Clear database completed");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException ignored) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static ArrayList<String> getSql(String fileName) {
        ArrayList<String> strings = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                strings.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }

    public static void randomData(ArrayList<Author> authors, int lengthAuthor,
                                  ArrayList<Category> categories, int lengthCategory,
                                  ArrayList<City> cities, int lengthCity,
                                  ArrayList<Post> posts, int lengthPost,
                                  ArrayList<Reply> replies, int lengthReply,
                                  ArrayList<SubReply> subReplies, int lengthSub) {
        //category
        HashSet<String> strings = new HashSet<>();
        while (strings.size() < lengthCategory) {
            String temp = randomString(30);
            strings.add(temp);
        }
        for (String element : strings) {
            categories.add(new Category(element));
        }
        strings.clear();
        //author
        for (int i = 0; i < lengthAuthor; i++) {
            String temp = randomString(40);
            strings.add(temp);
        }
        for (String element : strings) {
            authors.add(new Author(element));
        }

        //city
        Iterator<String> iterator = strings.iterator();
        while (strings.size() < lengthCity * 2) {
            String temp = randomString(40);
            strings.add(temp);
        }
        for (int i = 0; i < lengthCity; i++) {
            String element1 = iterator.next();
            String element2 = iterator.next();
            cities.add(new City(element1, element2));
        }
        strings.clear();
        //post
        for (int i = 0; i < lengthPost; i++) {
            int randomInt = random.nextInt(lengthAuthor);
            Author author = authors.get(randomInt);
            Timestamp timestamp = author.registerTime;
            // 将时间戳增加一个小时作为发表时间
            LocalDateTime localDateTime = timestamp.toLocalDateTime();
            localDateTime = localDateTime.plusHours(1);
            timestamp = Timestamp.valueOf(localDateTime);

            randomInt = random.nextInt(lengthCity);
            City city = cities.get(randomInt);

            String title = randomString(80);
            String content = randomString(800);

            Post post = new Post((long) i, title, content, timestamp.toString());
            post.author = author;
            post.city = city;
            posts.add(post);
            randomInt = random.nextInt(3, 10);
            for (int i1 = 0; i1 < randomInt; i1++) {
                Category category = categories.get(random.nextInt(lengthCategory));
                if (!post.categories.contains(category)) {
                    post.categories.add(category);
                } else i1--;
            }
            randomInt = random.nextInt(50);
            for (int j = 0; j < randomInt; j++) {
                Author author1 = authors.get(random.nextInt(lengthAuthor));
                if (!post.like.contains(author1) && !author1.equals(author)) {
                    post.like.add(author1);
                } else j--;
            }
            randomInt = random.nextInt(50);
            for (int j = 0; j < randomInt; j++) {
                Author author1 = authors.get(random.nextInt(lengthAuthor));
                if (!post.share.contains(author1) && !author1.equals(author)) {
                    post.share.add(author1);
                } else j--;
            }
            randomInt = random.nextInt(50);
            for (int j = 0; j < randomInt; j++) {
                Author author1 = authors.get(random.nextInt(lengthAuthor));
                if (!post.favorite.contains(author1) && !author1.equals(author)) {
                    post.favorite.add(author1);
                } else j--;
            }
            randomInt = random.nextInt(50);
            for (int j = 0; j < randomInt; j++) {
                Author author1 = authors.get(random.nextInt(lengthAuthor));
                if (!post.follow.contains(author1) && !author1.equals(author)) {
                    post.follow.add(author1);
                } else j--;
            }
        }
        //reply
        for (int i = 0; i < lengthReply; i++) {
            int randomInt = random.nextInt(lengthAuthor);
            Author author = authors.get(randomInt);
            long star = random.nextLong(10000);
            String content = randomString(800);
            Reply reply = new Reply(content, star, author);
            reply.reply_Id = i;
            replies.add(reply);
            randomInt = random.nextInt(lengthPost);
            posts.get(randomInt).replies.add(reply);
        }

        //subReply
        for (int i = 0; i < lengthSub; i++) {
            int randomInt = random.nextInt(lengthAuthor);
            Author author = authors.get(randomInt);
            long star = random.nextLong(10000);
            String content = randomString(1000);
            SubReply subReply = new SubReply(content, star, author);
            subReply.subReply_ID = i;
            subReplies.add(subReply);
            randomInt = random.nextInt(lengthReply);
            replies.get(randomInt).subReplies.add(subReply);
        }
    }

    public static void storeData() {
        ArrayList<Author> authors = new ArrayList<>();
        ArrayList<Category> categories = new ArrayList<>();
        ArrayList<City> cities = new ArrayList<>();
        ArrayList<Post> posts = new ArrayList<>();
        ArrayList<Reply> replies = new ArrayList<>();
        ArrayList<SubReply> subReplies = new ArrayList<>();
        int lengthAuthor = 100000;
        int lengthCategory = 2000;
        int lengthCity = 1000;
        int lengthPost = 10000;
        int lengthReply = 20000;
        int lengthSub = 50000;


        lengthCategory*=mul;
        lengthAuthor*=mul;
        lengthSub*=mul;
        lengthPost*=mul;
        lengthReply*=mul;
        lengthCity*=mul;

        randomData(authors, lengthAuthor, categories, lengthCategory, cities, lengthCity
                , posts, lengthPost, replies, lengthReply, subReplies, lengthSub);
        try {
            FileOutputStream fos = new FileOutputStream("datas\\dataOfAuthor.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(authors);
            oos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            FileOutputStream fos = new FileOutputStream("datas\\dataOfCategory.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(categories);
            oos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            FileOutputStream fos = new FileOutputStream("datas\\dataOfCity.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(cities);
            oos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            FileOutputStream fos = new FileOutputStream("datas\\dataOfPost.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(posts);
            oos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            FileOutputStream fos = new FileOutputStream("datas\\dataOfReply.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(replies);
            oos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            FileOutputStream fos = new FileOutputStream("datas\\dataOfSubReply.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(subReplies);
            oos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String randomString(int length) {
        length = random.nextInt(length / 2, length);
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char i1 = (char) ('a' + random.nextInt(26));
            out.append(i1);
        }
        return out.toString();
    }


}
