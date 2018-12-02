package models;

import io.ebean.Model;
import io.ebean.annotation.DbJson;
import io.ebean.annotation.NotNull;
import utils.MyDateUtil;

import javax.persistence.*;
import java.math.BigDecimal;

@MappedSuperclass
public abstract class LibraryItem extends Model implements Comparable<LibraryItem>{

    public static final double PRIMARY_PENALTY = 4.8;
    public static final double SECONDARY_PENALTY = 12;

    @Id
    protected String ISBN; // isbn of item

    @NotNull
    protected String title; // title of item

    protected String section; // section to which item belongs

    @DbJson
    protected MyDateUtil pubDate; // date on which the item has been published

    @ManyToOne
    protected Reader currentReader; // reader who has currently borrowed the book

    @DbJson
    protected MyDateUtil borrowedOn; // date on which the book has been borrowed

    // Default empty constructor
    public LibraryItem(){
    }

    public LibraryItem(String ISBN, String title, String section,
                       MyDateUtil pubDate, Reader currentReader, MyDateUtil borrowedOn) {
        this.ISBN = ISBN;
        this.title = title;
        this.section = section;
        this.pubDate = pubDate;
        this.currentReader = currentReader;
        this.borrowedOn = borrowedOn;
    }

    public final String getISBN() {
        return ISBN;
    }

    public final void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public final String getTitle() {
        return title;
    }

    public final void setTitle(String title) {
        this.title = title;
    }

    public final String getSection() {
        return section;
    }

    public final void setSection(String section) {
        this.section = section;
    }

    public final MyDateUtil getPubDate() {
        return pubDate;
    }

    public final void setPubDate(MyDateUtil pubDate) {
        this.pubDate = pubDate;
    }

    public final Reader getCurrentReader() {
        return currentReader;
    }

    public final void setCurrentReader(Reader currentReader) {
        this.currentReader = currentReader;
    }

    public final MyDateUtil getBorrowedOn() {
        return borrowedOn;
    }

    public final void setBorrowedOn(MyDateUtil borrowedOn) {
        this.borrowedOn = borrowedOn;
    }

    public static BigDecimal calculateLateFee(String itemType, MyDateUtil returned, MyDateUtil borrowed){
        int difference = MyDateUtil.getDifference(returned,borrowed);
        int borrowalPeriod = 0;
        switch(itemType){
            case "Book":
                borrowalPeriod = Book.MAX_BORROWAL_PERIOD;
                break;
            case "Dvd":
                borrowalPeriod = Dvd.MAX_BORROWAL_PERIOD;
                break;
        }
        if(difference > borrowalPeriod){
            int overdueBy = difference - borrowalPeriod;
            BigDecimal fee = BigDecimal.valueOf(0);
            if(overdueBy<=3){
                fee = fee.add(BigDecimal.valueOf(PRIMARY_PENALTY).multiply(BigDecimal.valueOf(overdueBy)));
            } else {
                fee = fee.add(BigDecimal.valueOf(PRIMARY_PENALTY).multiply(BigDecimal.valueOf(3)));
                fee =fee.add(BigDecimal.valueOf(SECONDARY_PENALTY).multiply(BigDecimal.valueOf(overdueBy-3)));
            }
            return fee;
        }else{
            return BigDecimal.valueOf(0.0);
        }
    };

    @Override
    public final boolean equals(Object obj) {
        // check if same object-wise (memory address)
        if(super.equals(obj)){
            return true;
        }

        // check if same library-item-wise (same ISBN)
        LibraryItem item= (LibraryItem) obj;
        return ISBN.equals(item.getISBN());
    }

    @Override
    public int compareTo(LibraryItem o) {
        return this.getISBN().compareTo(o.getISBN());
    }

}
