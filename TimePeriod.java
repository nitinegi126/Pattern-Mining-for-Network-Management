package nitin.codebind;


import java.util.Comparator;

public class TimePeriod  implements Comparable {
    private int startTime;
    private int endTime;

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "TimePeriod{" + startTime + ", " + endTime + '}';
    }

    @Override
    public int compareTo(Object o) {
        TimePeriod t1 = (TimePeriod) o;
        if(this.getStartTime()-t1.getStartTime() !=0)
            return  this.getStartTime() - t1.getStartTime();
        else
            return this.getEndTime() - t1.getEndTime();
    }

}