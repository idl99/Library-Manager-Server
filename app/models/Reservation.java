package models;

import io.ebean.Ebean;
import io.ebean.Model;
import io.ebean.annotation.DbArray;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservations")
public class Reservation extends Model {

    @Id
    private String itemIsbn;

    @DbArray
    private List<String> listOfReaders;

    public Reservation(String itemIsbn, List<String> listOfReaders) {
        this.itemIsbn = itemIsbn;
        this.listOfReaders = listOfReaders;
    }

    public String getItemIsbn() {
        return itemIsbn;
    }

    public void setItemIsbn(String itemIsbn) {
        this.itemIsbn = itemIsbn;
    }

    public List<String> getListOfReaders() {
        return listOfReaders;
    }

    public void setListOfReaders(List<String> listOfReaders) {
        this.listOfReaders = listOfReaders;
    }

    public static Reservation getReservationById(String isbn){
        return Ebean.find(Reservation.class).where().idEq(isbn).findOne();
    }

    public static void initReservationsForItem(LibraryItem item){
        Reservation reservation = new Reservation(item.getISBN(),new ArrayList<>());
        reservation.save();
    }


}
