package models;

import io.ebean.Ebean;
import io.ebean.Model;
import io.ebean.annotation.DbArray;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;


/**
 * Model class to design and represent Reservations for Library Item.
 * @Entity JPA annotation is used to denote that this class models an entity which needs to be persisted and stored in a table.
 * @Table annotation is used to give explicit name for the table in which Reservations need to be persisted.
 */
@Entity
@Table(name = "reservations")
public class Reservation extends Model {


    /**
     * Unique identifier for all Reservation entries
     */
    @Id
    private String itemIsbn;


    /**
     * The list of Readers who've placed Reservation for given Library Item
     */
    @DbArray
    private List<String> listOfReaders;


    /**
     * Constructor used to create Reservation Objects
     * @param itemIsbn - ISBN of item for which Reservations are being recorded
     * @param listOfReaders - list of Readers who've placed Reservation for given Library Item
     */
    public Reservation(String itemIsbn, List<String> listOfReaders) {
        this.itemIsbn = itemIsbn;
        this.listOfReaders = listOfReaders;
    }


    /**
     * Getter for Item ISBN
     * @return
     */
    public String getItemIsbn() {
        return itemIsbn;
    }


    /**
     * Setter for Item ISBN
     * @param itemIsbn
     */
    public void setItemIsbn(String itemIsbn) {
        this.itemIsbn = itemIsbn;
    }


    /**
     * Getter for List of Readers
     * @return
     */
    public List<String> getListOfReaders() {
        return listOfReaders;
    }


    /**
     * Setter for List of Readers
     * @param listOfReaders
     */
    public void setListOfReaders(List<String> listOfReaders) {
        this.listOfReaders = listOfReaders;
    }


    /**
     * Method to get Reservation details for Library Item
     * @param isbn - ISBN of item for which Reservation details are to be retrieved
     * @return
     */
    public static Reservation getReservationById(String isbn){
        return Ebean.find(Reservation.class).where().idEq(isbn).findOne();
    }


    /**
     * Method to initialize Reservation details for Library Item
     * @param item - Item for which Reservation details are to be initialized
     */
    public static void initReservationsForItem(LibraryItem item){
        Reservation reservation = new Reservation(item.getISBN(),new ArrayList<>());
        reservation.save();
    }


}
