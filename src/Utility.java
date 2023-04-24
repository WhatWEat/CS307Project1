import java.util.ArrayList;

public class Utility {
    public static long tableNumber = 0;
    public static double averageTime = 0;
    public static long totalTime = 0;
    public static void addCount(long dis, long count, String name){
        tableNumber++;
        totalTime += dis;
        double timeUnit = (count * 1000.0) / dis;
        averageTime += timeUnit;
        System.out.println(count + name +" records successfully loaded");
        System.out.println("Loading speed : " + timeUnit + " records/s");
    }
    public static long getTotalTime(){
        long tmp = totalTime;
        totalTime = 0;
        return tmp;
    }
    public static double getAverageTime(){
        double result = averageTime / tableNumber;
        averageTime = 0;
        tableNumber = 0;
        return result;
    }
}
