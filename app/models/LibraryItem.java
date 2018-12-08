package models;

import io.ebean.Model;
import io.ebean.annotation.DbJson;
import io.ebean.annotation.NotNull;
import utils.MyDateUtil;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Model class used to design abstraction of a tangible Library Item consumed by Readers.
 *
 * Extends Model class in io.ebeans to extend persistence methods provided by Model class.
 * Implements Comparable to compare two Library Items, for comparison and ordering purpose.
 *
 * @MappedSuperClass JPA annotation is used to denote that this is a class from which other its subtypes will
 * inherit attributes and behavior.
 */
@MappedSuperclass
public abstract class LibraryItem extends Model implements Comparable<LibraryItem>{

    /**Common late item return fee charged on Readers for the first 3 days for which the item is overdue
     * Rate stands at $4.8
     */
    public static final double PRIMARY_PENALTY = 4.8;


    /**Common late item return free charged on Readers after the first 3 days for which the item is overdue.
     * Rate stands at $4.8
     */
    public static final double SECONDARY_PENALTY = 12;


    /**ISBN of each Library Item. Used as unique identifier.
     * @Id JPA annotation is used by Ebeans to identify this field as the unique identifier for all Library Item.
     */
    @Id
    protected String ISBN;


    /**Title of each Library Item.
     * @NotNull JPA annotation is used by Ebeans to require a value for this field.
     */
    @NotNull
    protected String title;


    /**Section to which each Library Item belongs to.
     */
    protected String section;


    /**Date on which the Library Item has been published.
     * @DbJson Ebean annotation is used to indicate to Ebeans that this field of my custom defined type, MyDateUtil
     * should be converted to a JSON object and persisted in the database, with its attribute values.
     */
    @DbJson
    protected MyDateUtil pubDate;


    /**The reader who has currently borrowed the Library Item.
     * @ManyToOne JPA annotation is to indicate to Ebeans the Many-to-One relationship between Library Item and Reader.
     */
    @ManyToOne
    protected Reader currentReader;


    /**The date on which the Library Item has been borrowed.
     * @DbJson Ebean annotation is used to indicate to Ebean that this field of my custom defined type, MyDateUtil
     * should be converted to a JSON object and persisted in the database, with its attribute values
     */
    @DbJson
    protected MyDateUtil borrowedOn;


    /**
     * Default constructor required by Ebeans
     */
    public LibraryItem(){
        super();
    }

    /**
     * Constructor for Library Item objects
     * Resolved by Ebeans when loading data onto memory.
     * @param ISBN
     * @param title
     * @param section
     * @param pubDate
     * @param currentReader
     * @param borrowedOn
     */
    public LibraryItem(String ISBN, String title, String section, MyDateUtil pubDate,
                       Reader currentReader, MyDateUtil borrowedOn) {
        this();
        this.ISBN = ISBN;
        this.title = title;
        this.section = section;
        this.pubDate = pubDate;
        this.currentReader = currentReader;
        this.borrowedOn = borrowedOn;
    }


    /**
     * Getter for ISBN
     * @return
     */
    public final String getISBN() {
        return ISBN;
    }


    /**
     * Setter for ISBN
     * @param ISBN
     */
    public final void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }


    /**
     * Getter for title
     * @return
     */
    public final String getTitle() {
        return title;
    }


    /**
     * Setter for title
     * @param title
     */
    public final void setTitle(String title) {
        this.title = title;
    }


    /**
     * Getter for section
     * @return
     */
    public final String getSection() {
        return section;
    }


    /**
     * Setter for section
     * @param section
     */
    public final void setSection(String section) {
        this.section = section;
    }


    /**
     * Getter for publication date
     * @return
     */
    public final MyDateUtil getPubDate() {
        return pubDate;
    }


    /**
     * Setter for publication date
     * @param pubDate
     */
    public final void setPubDate(MyDateUtil pubDate) {
        this.pubDate = pubDate;
    }


    /**
     * Getter for current Reader
     * @return
     */
    public final Reader getCurrentReader() {
        return currentReader;
    }


    /**
     * Setter for current Reader
     * @param currentReader
     */
    public final void setCurrentReader(Reader currentReader) {
        this.currentReader = currentReader;
    }


    /**
     * Getter for date on which item was borrowed
     * @return
     */
    public final MyDateUtil getBorrowedOn() {
        return borrowedOn;
    }


    /**
     * Setter for date on which item was borrowed
     * @param borrowedOn
     */
    public final void setBorrowedOn(MyDateUtil borrowedOn) {
        this.borrowedOn = borrowedOn;
    }


    /**Method to calculate the late fee on item return. Returns $0.00 if item is returned before due date
     * @param returned - the date on which the item is being returned by the reader.
     * @param maxBorrowalPeriod - the maximum period for which the item can be borrowed for
     * @return - the calculated late fee
     */
    public BigDecimal calculateLateFee(MyDateUtil returned, int maxBorrowalPeriod){

        int difference = MyDateUtil.getDifference(returned,getBorrowedOn()); // Get difference between date on which item
                                                // has been borrowed and date on which item is being returned in days.

        if(difference > maxBorrowalPeriod){
            // Reader has kept item for more than the allowed duration
            int overdueBy = difference - maxBorrowalPeriod;
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

    }


    /**Method to check if two Library Items are the same. Overriding equals method in Object class.
     * ISBN is used to check if two Library items are the same.
     * @param obj - Item with which this item needs to be compared with for equality.
     * @return boolean, indicating true if both object instances represent same Library item, false if otherwise.
     */
    @Override
    public final boolean equals(Object obj) {
        if(super.equals(obj)){
            return true;
        }
        LibraryItem item= (LibraryItem) obj;
        return ISBN.equals(item.getISBN());
    }


    /**Method to compare two Library Items and define their natural sort order.
     * Naturally sort by Title.
     * @param o - the Item with which this Item is to be compared
     * @return - positive value if this item is naturally higher in the sort order, negative value if other item
     * is higher in the natural sort order, or 0 if both items are equal in the natural sorder.
     */
    @Override
    public int compareTo(LibraryItem o) {
        return this.getTitle().compareTo(o.getTitle());
    }


}
