package TableInsert;

import Entity.Author;
import Entity.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class BasicInfor {

    public static Connection con = null;
    public PreparedStatement sql = null;
    public static final int BATCH_SIZE = 1000;
    public CountDownLatch startSignal;
    public CountDownLatch doneSignal;
    public static String loadAuthor =
        "INSERT INTO public.Author (author_id,registration_time,phone_number,name) "
            + "VALUES (?,?,?,?);";
    public static String loadPost =
        "INSERT INTO public.Post (post_id,title,content,posting_time) " +
            "VALUES (?,?,?,?);";
    public static String loadCategory = "INSERT INTO public.Category (category_id,category) " +
        "VALUES (?,?);";
    public static String loadCity = "INSERT INTO public.PostingCity (city_id,city,country) " +
        "VALUES (?,?,?);";
    public static String loadReply = "INSERT INTO public.Reply (reply_id,content,stars) " +
        "VALUES (?,?,?);";
    public static String loadAuthorWritePost =
        "INSERT INTO public.AuthorWritePost (post_id,author_id) " +
            "VALUES (?,?);";
    public static String loadAuthorLikePost =
        "INSERT INTO public.AuthorLikePost (post_id,author_id) " +
            "VALUES (?,?);";

    public static String loadAuthorSharePost =
        "INSERT INTO public.AuthorSharePost (post_id,author_id) " +
            "VALUES (?,?);";
    public static String loadAuthorFavoritePost =
        "INSERT INTO public.AuthorFavoritePost (post_id,author_id) " +
            "VALUES (?,?);";
    public static String loadAuthorFollowPost =
        "INSERT INTO public.AuthorFollowPost (post_id,author_id) " +
            "VALUES (?,?);";
    public static String loadPostCategory =
        "INSERT INTO public.PostCategory (category_id,post_id) " +
            "VALUES (?,?);";
    public static String loadPostCity =
        "INSERT INTO public.PostCity (post_id,city_id) " + "VALUES (?,?);";

    public static String loadPostReply =
        "INSERT INTO public.PostReply (post_id,reply_id) " + "VALUES (?,?);";

    public static String loadAuthorReply =
        "INSERT INTO public.AuthorReply (author_id,reply_id) " + "VALUES (?,?);";

    public static String loadSubReply =
        "INSERT INTO public.SubReply (reply_id,sub_reply_id,content,stars) " +
            "VALUES (?,?,?,?);";

    public static String loadSubReplyAuthor =
        "INSERT INTO public.SubReplyAuthor (author_id,sub_reply_id) " +
            "VALUES (?,?);";

    public BasicInfor(Connection con, String sql) {
        if (BasicInfor.con == null) {
            BasicInfor.con = con;
        }
        try {
            this.sql = con.prepareStatement(sql);
        } catch (SQLException e) {
            System.err.println("create SQL sentence failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public BasicInfor(String sql, CountDownLatch startSignal, CountDownLatch doneSignal) {
        try {
            this.sql = con.prepareStatement(sql);
            this.doneSignal = doneSignal;
            this.startSignal = startSignal;
        } catch (SQLException e) {
            System.err.println("create SQL sentence failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public void finalCommit(long count) {
        try {
            if (count % BATCH_SIZE != 0) {
                sql.executeBatch();
            }
            con.commit();
            sql.close();
        } catch (SQLException s) {
            System.err.println(s.getMessage());
            throw new RuntimeException(s);
        }
    }

    static void closeDB() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.err.println("close DB failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static <T> ArrayList<ArrayList<T>> splitRecords(ArrayList<T> records, int size) {
        ArrayList<ArrayList<T>> blocks = new ArrayList<>();
        if (size <= 1000) {
            blocks.add(records);
        } else {
            for (int i = 0; i < records.size(); i += size) {
                blocks.add(new ArrayList<>(records.subList(i, Math.min(i + size, records.size()))));
            }
        }
        return blocks;
    }

    public static ArrayList<Author> splitPostInner(ArrayList<Post> records, String tableName) {
        ArrayList<Author> authors = new ArrayList<>();
        switch (tableName) {
            case "AuthorLikePost":
                for (Post i : records) {
                    authors.addAll(i.like);
                }
                break;
            case "AuthorSharePost":
                for (Post i : records) {
                    authors.addAll(i.share);
                }
                break;
            case "AuthorFavoritePost":
                for (Post i : records) {
                    authors.addAll(i.favorite);
                }
                break;
            case "AuthorFollowPost":
                for (Post i : records) {
                    authors.addAll(i.follow);
                }
                break;
        }
        return authors;
    }
}
