package utils;


/**
 * Custom utility class to handle Date related logic of this application
 */
public class MyDateUtil {


    /**
     * Represents day of the month
     */
    private int day;


    /**
     * Represents month of the year
     */
    private int month;


    /**
     * Represents year
     */
    private int year;


    /**
     * Default constructor required by Ebeans to resolve deserialization of JsonObject stored in database
     */
    public MyDateUtil(){
    }


    /**
     * Constructor to create a MyDateUtil object by passing day, month, and year as arguments.
     * @param day
     * @param month
     * @param year
     */
    public MyDateUtil(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }


    /**
     * Constructor to create a MyDateUtil object by passing in a date string as argument.
     * @param date
     */
    public MyDateUtil(String date){
        this.day = Integer.valueOf(date.split("/")[0]);
        this.month = Integer.valueOf(date.split("/")[1]);
        this.year = Integer.valueOf(date.split("/")[2]);
    }


    /**
     * Getter for day
     */
    public int getDay() {
        return day;
    }


    /**
     * Setter for day
     * @param day
     */
    public void setDay(int day) {
        this.day = day;
    }


    /**
     * Getter for month
     * @return
     */
    public int getMonth() {
        return month;
    }


    /**
     * Setter for month
     * @param month
     */
    public void setMonth(int month) {
        this.month = month;
    }


    /**
     * Getter for year
     * @return
     */
    public int getYear() {
        return year;
    }


    /**
     * Setter for year
     * @param year
     */
    public void setYear(int year) {
        this.year = year;
    }


    /**
     * Utility static method to get difference as number of days between two MyDateUtil Dates
     * @param date1
     * @param date2
     * @return - difference in number of days between the two days
     */
    public static int getDifference(MyDateUtil date1, MyDateUtil date2){
        return daysElapsed(date1) - daysElapsed(date2);
    };


    /**
     * Utility static method to get the number of days elapsed since 1900 epoch. Output of this method
     * is used by other methods like getDifference() to compute the difference between two dates in terms of the number
     * of days elapsed.
     * @param date
     * @return
     */
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


    /**
     * Private utility static method to check if a given year is a leap year or not
     * @param year
     * @return - boolean indicating true if year passed as argument is a leap year, or false if otherwise.
     */
    private static boolean isLeapYear(int year){
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


    /**
     * Method that converts MyDateUtil object into a String and returns it to the user.
     * @return - returns the date as a date string.
     */
    @Override
    public String toString() {
        return this.getDay()+"/"+this.getMonth()+"/"+this.getYear();
    }


}
