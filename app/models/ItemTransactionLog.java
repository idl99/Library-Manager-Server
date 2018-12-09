package models;

import io.ebean.Ebean;
import io.ebean.Model;
import utils.MyDateUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity which keeps log of Item Transactions, for Item being borrowed and Item Return.
 * @Entity JPA annotation is used to denote that this class models an entity which needs to be persisted in a table.
 * @Table annotation is used to give explicit name for the table in which this entity need to be persisted.
 */
@Entity
@Table(name="ItemTransactionLog")
public class ItemTransactionLog extends Model{

    /**
     * ISBN of item logged. Used as unique identifier for each Item for which transaction history is logged.
     */
    @Id
    private String itemIsbn;


    /**
     * Number of items given item has been borrowed
     */
    private int noOfTimesBorrowed;


    /**
     * Average borrowal period for a given
     */
    private int averageBorrowalPeriod;


    /**
     * Default constructor for Ebeans
     */
    public ItemTransactionLog() {
        super();
    }


    /**
     * Constructor to create Item Transaction Log objects
     * @param itemIsbn
     * @param noOfTimesBorrowed
     * @param averageBorrowalPeriod
     */
    public ItemTransactionLog(String itemIsbn, int noOfTimesBorrowed, int averageBorrowalPeriod) {
        this();
        this.itemIsbn = itemIsbn;
        this.noOfTimesBorrowed = noOfTimesBorrowed;
        this.averageBorrowalPeriod = averageBorrowalPeriod;
    }


    /**
     * Getter for tem ISBN
     * @return
     */
    public String getItemIsbn() {
        return itemIsbn;
    }


    /**
     * Setter for item ISBN
     * @param itemIsbn
     */
    public void setItemIsbn(String itemIsbn) {
        this.itemIsbn = itemIsbn;
    }


    /**
     * Getter for number of times item has been borrowed
     * @return
     */
    public int getNoOfTimesBorrowed() {
        return noOfTimesBorrowed;
    }


    /**
     * Setter for number of times item has been borrowed
     * @param noOfTimesBorrowed
     */
    public void setNoOfTimesBorrowed(int noOfTimesBorrowed) {
        this.noOfTimesBorrowed = noOfTimesBorrowed;
    }


    /**
     * Method to increment the number of times the item has been borrowed.
     * Invoked when item is borrowed to update noOfTimesBorrowed.
     */
    public void incrementNoOfTimesBorrowed(){
        this.noOfTimesBorrowed++;
    }


    /**
     * Getter for average borrowal period
     * @return
     */
    public int getAverageBorrowalPeriod() {
        return averageBorrowalPeriod;
    }


    /**
     * Setter for average borrowal period
     * @param averageBorrowalPeriod
     */
    public void setAverageBorrowalPeriod(int averageBorrowalPeriod) {
        this.averageBorrowalPeriod = averageBorrowalPeriod;
    }


    /**
     * Method to get Transaction Log for a particular item
     * @param isbn
     * @return
     */
    public static ItemTransactionLog getLogByItem(String isbn){
        return Ebean.find(ItemTransactionLog.class).where().idEq(isbn).findOne();
    }


    /**
     * Method to initialize Transaction Log for a particular item.
     * Invoked when a new Library Item is inserted into the system.
     * @param item
     */
    public static void initLogForItem(LibraryItem item){
        ItemTransactionLog itemTransactionLog = new ItemTransactionLog(item.getISBN(), 0,0);
        itemTransactionLog.save();
    }


    /**
     * Method to update the Item Transaction Log when item is returned.
     * Updates the average borrowal period.
     * @param item
     * @param returnedOn
     */
    public static void updateOnItemReturn(LibraryItem item, MyDateUtil returnedOn){
        //Calculate day difference and update item transaction log
        ItemTransactionLog log = getLogByItem(item.getISBN());
        int currentBorrowalDayDiff = MyDateUtil.getDifference(returnedOn, item.getBorrowedOn());
        log.setAverageBorrowalPeriod(
                (currentBorrowalDayDiff+log.getAverageBorrowalPeriod()*(log.getNoOfTimesBorrowed()-1))
                        /log.getNoOfTimesBorrowed()
        );
        log.update();
    }


}