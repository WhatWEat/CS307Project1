import Entity.Author;
import Entity.Category;
import Entity.City;
import Entity.Post;
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

        int numOfPost = 0;

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("E:\\CS307\\project1\\project1\\src\\posts.json"));
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
                String[] values = data.split(", ");
                City city = new City(values[0],values[1] );
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

            System.out.println("Finished!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    static class author{
//        String phoneNumber;
//        String id;
//        String registerTime;
//        String name;
//    }
//    static class city{
//        String name;
//        String country;
//
//        public city(String name, String country) {
//            this.name = name;
//            this.country = country;
//        }
//    }

    static void addStringList(Object[] in,JSONArray array,int index){
        int length = array.length();
        String[] out = new String[length];
        for (int i = 0 ; i < length ;i++){
            out[i] = array.getString(i);
        }
        in[index] = out;
    }
}