import java.util.ArrayList;

public class Utility {
    public static long termNumber = 0;
    public static long totalTime = 0;
    public static void addCount(long dis, long count, String name){
        termNumber += count;
        totalTime += dis;
        double timeUnit = (count * 1000.0) / dis;
        System.out.println(count + name +" records successfully loaded");
        System.out.println("Loading speed : " + timeUnit + " records/s");
    }
    public static long getTotalTime(){
        return totalTime;
    }
    public static long getAverageTime(){
        long result = termNumber * 1000 / totalTime;
        termNumber = 0;
        totalTime = 0;
        return result;
    }
}
