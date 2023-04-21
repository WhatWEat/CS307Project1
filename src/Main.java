import Entity.Author;
import Entity.Category;
import Entity.City;
import Entity.Post;
import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadLocalRandom;
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
    public static void main(String[] args) {

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("posts.json"));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonString.append(line); //逐行读取JSON字符串
            }
            bufferedReader.close(); //关闭文件流
            JSONArray jsonArray = new JSONArray(jsonString.toString());


            for (int i=0; i<jsonArray.length(); i++) {
                // basic information
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("Title");
                Long id = jsonObject.getLong("Post ID");
                String content = jsonObject.getString("Content");
                String postTime = jsonObject.getString("Posting Time");
                Post post = new Post(id,title,content,postTime);
                posts.add(post);

                // city
                String data = jsonObject.getString("Posting City");
                int lastPeriodIndex = data.lastIndexOf(", "); // 找到最后一个 “。 ” 的位置
                String part1 = data.substring(0, lastPeriodIndex); // 截取分割后的第一部分
                String part2 = data.substring(lastPeriodIndex + 2).trim(); // 截取分割后的第二部分
//                String[] values = data.split(", ");
                City city = new City(part1,part2);
                if(cities.contains(city)){
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
                if (!authors.contains(author)){
                    authors.add(author);
                }
                post.author = author;

                //category
                JSONArray categoryArray = jsonObject.getJSONArray("Category");

                for (int j = 0; j < categoryArray.length();j++) {
                    Category category = new Category(categoryArray.getString(j));
                    if (categories.contains(category)){
                        Category.id --;
                        int index = categories.indexOf(category);
                        post.categories.add(categories.get(index));
                    }else {
                        categories.add(category);
                        post.categories.add(category);
                    }
                }
            }

            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // Followed By
                JSONArray followedByArray = jsonObject.getJSONArray("Authors Followed By");
                for (int j = 0; j < followedByArray.length();j++) {
                    Author author = new Author();
                    author.id = followedByArray.getString(j);
                    if (!authors.contains(author)){
                        authors.add(author);
                    }
                    int index = authors.indexOf(author);
                    posts.get(i).follow.add(authors.get(index));
                }
            }

            System.out.println("Finished!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static Author createAuthor(String name){
        Author author = new Author();
        author.name = name;
        return author;
    }

    static void addStringList(Object[] in,JSONArray array,int index){
        int length = array.length();
        String[] out = new String[length];
        for (int i = 0 ; i < length ;i++){
            out[i] = array.getString(i);
        }
        in[index] = out;
    }
}