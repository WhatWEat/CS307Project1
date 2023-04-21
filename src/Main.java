import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        int numOfPost = 0;
        String[] titles;
        Map<Integer, Set<String>> postCats = new HashMap<>();
        Set<String> categories = new HashSet<>();
        String[] contents;
        String[] postTimes;
        city[] postCities;
        author[] authorOfPosts;
        Map<String,author> authors = new HashMap<>();//用author的昵称和author的其他信息匹配
        Object[] favorite ;
        Object[] follow ;
        Object[] share ;
        Object[] like ;

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("E:\\CS307\\project1\\project1\\src\\posts.json"));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonString.append(line); //逐行读取JSON字符串
            }
            bufferedReader.close(); //关闭文件流
            String A;
            JSONArray jsonArray = new JSONArray(jsonString.toString());

            numOfPost = jsonArray.length();
            titles = new String[numOfPost];
            contents = new String[numOfPost];
            postTimes = new String[numOfPost];
            postCities = new city[numOfPost];
            authorOfPosts = new author[numOfPost];
            favorite = new Object[numOfPost];
            follow = new Object[numOfPost];
            share = new Object[numOfPost];
            like = new Object[numOfPost];

            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                titles[i] = jsonObject.getString("Title");
                contents[i] = jsonObject.getString("Content");
                postTimes[i] = jsonObject.getString("Posting Time");
                String data = jsonObject.getString("Posting City");
                String[] values = data.split(", ");
                city in = new city(values[0],values[1] );
                postCities[i] = in;
                author temp = new author();
                temp.id = jsonObject.getString("Author's ID");
                temp.phoneNumber = jsonObject.getString("Author's Phone");
                temp.registerTime = jsonObject.getString("Author Registration Time");
                temp.name = jsonObject.getString("Author");
                authors.put(jsonObject.getString("Author"), temp);
                authorOfPosts[i] = authors.get(jsonObject.getString("Author"));
                JSONArray categoryArray = jsonObject.getJSONArray("Category");
                Set<String> cats = new HashSet<>();
                for (int j = 0; j < categoryArray.length();j++) {
                    String category = categoryArray.getString(j);
                    cats.add(category);
                    categories.add(category);
                }
                postCats.put(i,cats);

                JSONArray followedByArray = jsonObject.getJSONArray("Authors Followed By");
                addStringList(follow,followedByArray,i);
                JSONArray shareArray = jsonObject.getJSONArray("Authors Who Shared the Entity.Post");
                addStringList(share,shareArray,i);
                JSONArray likeArray = jsonObject.getJSONArray("Authors Who Liked the Entity.Post");
                addStringList(like,likeArray,i);
                JSONArray favoriteArray = jsonObject.getJSONArray("Authors Who Favorited the Entity.Post");
                addStringList(favorite,favoriteArray,i);
            }
            System.out.println("Finished!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static class author{
        String phoneNumber;
        String id;
        String registerTime;
        String name;
    }
    static class city{
        String name;
        String country;

        public city(String name, String country) {
            this.name = name;
            this.country = country;
        }
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