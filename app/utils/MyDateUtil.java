package utils;

public class MyDateUtil {

    private int day;
    private int month;
    private int year;

    // Default empty constructor required by Ebeans to resolve deserialization of JsonObject stored in database
    public MyDateUtil(){
    }

    public MyDateUtil(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public MyDateUtil(String date){
        this.day = Integer.valueOf(date.split("/")[0]);
        this.month = Integer.valueOf(date.split("/")[1]);
        this.year = Integer.valueOf(date.split("/")[2]);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public static int getDifference(MyDateUtil date1, MyDateUtil date2){
        return daysElapsed(date1) - daysElapsed(date2);
    };

    public static int daysElapsed(MyDateUtil date){

        final int[] CUMULATIVE_DAYS = new int[]{0,31,59,90,120,151,181,212,243,273,304,334};
        final int[] LEAP_CUMULATIVE_DAYS = new int[]{0,31,60,91,121,152,182,213,244,274,305,335};

        int yearDiffInDays = 0;
        for(int i=1900; i<=date.getYear(); i++){
            if(isLeapYear(i)){
                yearDiffInDays+=366;
            } else {
                yearDiffInDays+=365;
            }
        }

        int monthDiffInDays;
        if(isLeapYear(date.getYear())){
            // Leap year
            monthDiffInDays = LEAP_CUMULATIVE_DAYS[(date.getMonth() - 1)];
        } else {
            // Not leap year
            monthDiffInDays = CUMULATIVE_DAYS[(date.getMonth() - 1)];
        }

        int daysDiff = date.getDay() - 1;

        return yearDiffInDays + monthDiffInDays + daysDiff;

    }

    private static boolean isLeapYear(int year){
//        if (year is not divisible by 4) then (it is a common year)
//        else if (year is not divisible by 100) then (it is a leap year)
//        else if (year is not divisible by 400) then (it is a common year)
//        else (it is a leap year)
        if(year%4 !=0){
            return false;
        } else if(year%100 !=0){
            return true;
        } else if(year%400 !=0){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return this.getDay()+"/"+this.getMonth()+"/"+this.getYear();
    }
}
