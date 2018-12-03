package dao;

import com.google.inject.ImplementedBy;
import exceptions.MaximumCapacityException;
import models.Dvd;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Set;


/**
 * Interface that declares the contract for behavior that all Dvd Data Access Object Implementations should
 * define and implement.
 *
 * @ImplementedBy tells Guice which classes (DvdDaoImpl.class) implements this interface.
 *
 */
@ImplementedBy(DvdDaoImpl.class)
public interface DvdDao {

    int getDvdCount();

    Set<Dvd> getAllDvd();

    Dvd getDvdByIsbn(String isbn) throws EntityNotFoundException;

    void insertDvd(Dvd dvd) throws MaximumCapacityException, EntityExistsException;

    void updateDvd(Dvd dvd) throws EntityNotFoundException;

    Dvd deleteDvd(String isbn) throws EntityNotFoundException;


}
