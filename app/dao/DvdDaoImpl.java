package dao;

import exceptions.MaximumCapacityException;
import io.ebean.Ebean;
import models.Dvd;
import models.ItemTransactionLog;
import models.Reservation;

import javax.inject.Singleton;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Set;


/**
 * Implementation of the Dvd Dao Interface
 * Executes, handles and perform CRUD operations on Dvds' data.
 * @Singleton annotation is used to indicate to Guice that this only a single object instance of this class should
 * be created, instantiated and provided for entire application runtime.
 */
@Singleton
public class DvdDaoImpl implements DvdDao{


    /**
     * Default constructor required by Guice
     */
    public DvdDaoImpl() {
    }


    /**
     * Getter for Dvd count from database
     * @return
     */
    @Override
    public int getDvdCount() {
        return Ebean.find(Dvd.class).findCount();
    }


    /**
     * Getter to retrieve details of all Dvd from database
     * @return
     */
    @Override
    public Set<Dvd> getAllDvd() {
        return Ebean.find(Dvd.class).findSet();
    }


    /**
     * Getter to retrieve details of a particular Dvd by quering database using Dvd ISBN.
     * @param isbn
     * @return
     * @throws EntityNotFoundException - when Dvd is not found for given ISBN.
     */
    @Override
    public Dvd getDvdByIsbn(String isbn) throws EntityNotFoundException {
        Dvd toReturn = Ebean.find(Dvd.class).where().idEq(isbn).findOne();
        if(toReturn == null)
            throw new EntityNotFoundException("Failed to find DVD for ISBN given.");
        else
            return toReturn;
    }


    /**
     * Method to insert details of new Dvd into database
     * @param dvd
     * @throws MaximumCapacityException - when library has reached maximum capacity of 50 Dvd
     * @throws EntityExistsException - when database already has a record of the given Dvd
     */
    @Override
    public void insertDvd(Dvd dvd) throws MaximumCapacityException, EntityExistsException {
        if(getDvdCount()<=Dvd.MAX_LIBRARY_CAPACITY){
            if(Ebean.find(Dvd.class).where().idEq(dvd.getISBN()).findCount()==0) {
                dvd.save();
                ItemTransactionLog.initLogForItem(dvd);
                Reservation.initReservationsForItem(dvd);
            }
            else {
                throw new EntityExistsException("Failed to create new DVD. DVD already exists for given ISBN.");
            }
        }
        else {
            throw new MaximumCapacityException("Library has reached maximum DVD capacity. " +
                    "Please discard old/damaged DVDs.");
        }
    }

    /**
     * Method to update the details of existing Dvd in the database.
     * @param dvd
     * @throws EntityNotFoundException - when a Dvd is not found for the given ISBN in the database.
     */
    @Override
    public void updateDvd(Dvd dvd) throws EntityNotFoundException {
        if(Ebean.find(Dvd.class).where().idEq(dvd.getISBN()).findCount()==0) {
            throw new EntityNotFoundException("Failed to update DVD. DVD not found for given ISBN.");
        }
        else {
            dvd.update();
        }
    }


    /**
     * Method to delete details of a Dvd from the database.
     * @param isbn
     * @return
     * @throws EntityNotFoundException - when a Dvd is not found for the given ISBN in the database.
     */
    @Override
    public Dvd deleteDvd(String isbn) throws EntityNotFoundException {
        Dvd dvd = getDvdByIsbn(isbn);
        dvd.delete();
        return dvd;
    }


}
