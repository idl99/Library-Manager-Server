package dao;

import exceptions.MaximumCapacityException;
import io.ebean.Ebean;
import models.Dvd;

import javax.inject.Singleton;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Set;

@Singleton
public class DvdDaoImpl implements DvdDao{

    // Default empty constructor
    public DvdDaoImpl() {
    }

    @Override
    public int getDvdCount() {
        return Ebean.find(Dvd.class).findCount();
    }

    @Override
    public Set<Dvd> getAllDvd() {
        return Ebean.find(Dvd.class).findSet();
    }

    @Override
    public Dvd getDvdByIsbn(String isbn) throws EntityNotFoundException {
        Dvd toReturn = Ebean.find(Dvd.class).where().idEq(isbn).findOne();
        if(toReturn == null)
            throw new EntityNotFoundException("Failed to find DVD for ISBN given.");
        else
            return toReturn;
    }

    @Override
    public void insertDvd(Dvd dvd) throws MaximumCapacityException, EntityExistsException {
        if(getDvdCount()<=100){
            if(Ebean.find(Dvd.class).where().idEq(dvd.getISBN()).findCount()==0) {
                dvd.save();
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

    @Override
    public void updateDvd(Dvd dvd) throws EntityNotFoundException {
        if(Ebean.find(Dvd.class).where().idEq(dvd.getISBN()).findCount()==0) {
            throw new EntityNotFoundException("Failed to update DVD. DVD not found for given ISBN.");
        }
        else {
            dvd.update();
        }
    }

    @Override
    public Dvd deleteDvd(String isbn) throws EntityNotFoundException {
        Dvd dvd = getDvdByIsbn(isbn);
        dvd.delete();
        return dvd;
    }

}
