package dao;

import io.ebean.Ebean;
import models.Reader;

import javax.inject.Singleton;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Set;


/**
 * Implementation of the Reader Dao Interface
 * Executes, handles and perform CRUD operations on Readers' data.
 * @Singleton annotation is used to indicate to Guice that this only a single object instance of this class should
 * be created, instantiated and provided for entire application runtime.
 */
@Singleton
public class ReaderDaoImpl implements ReaderDao {


    /**
     * Default constructor required by Ebeans
     */
    public ReaderDaoImpl() {
    }


    /**
     * Getter for Reader count from database
     * @return
     */
    @Override
    public int getReaderCount() {
        return Ebean.find(Reader.class).findCount();
    }


    /**
     * Getter to retrieve details of all Readers from database
     * @return
     */
    @Override
    public Set<Reader> getAllReaders() {
        return Ebean.find(Reader.class).findSet();
    }


    /**
     * Getter to retrieve details of a particular Reader by querying database using reader Id.
     * @param id
     * @return
     * @throws EntityNotFoundException - when Reader is not found for given reader Id.
     */
    @Override
    public Reader getReaderById(String id) throws EntityNotFoundException {
        Reader toReturn = Ebean.find(Reader.class).where().idEq(id).findOne();
        if(toReturn == null)
            throw new EntityNotFoundException("Failed to find Reader for Reader Id given.");
        else
            return toReturn;
    }


    /**
     * Method to insert new Reader details into database when new Reader registers.
     * @param reader
     * @throws EntityExistsException - when trying to register a Reader who is already registered.
     */
    @Override
    public void insertReader(Reader reader) throws EntityExistsException {
        if(Ebean.find(Reader.class).where().eq("name",reader.getName()).findCount()==0) {
            reader.save();
        }
        else {
            throw new EntityExistsException("Reader is already registered.");
        }
    }


    /**
     * Method to update details of existing Reader in the database.
     * @param reader
     * @throws EntityNotFoundException - when Reader is not found for given reader Id
     */
    @Override
    public void updateReader(Reader reader) throws EntityNotFoundException {
        if(Ebean.find(Reader.class).where().idEq(reader.getReaderId()).findCount()==0) {
            throw new EntityNotFoundException("Failed to update Reader details. Reader Id not found.");
        }
        else {
            reader.update();
        }
    }


    /**
     * Method to delete a Reader's details from the database.
     * @param id
     * @return
     * @throws EntityNotFoundException - when Reader is not found for given reader Id.
     */
    @Override
    public Reader deleteReader(String id) throws EntityNotFoundException {
        Reader reader = getReaderById(id);
        reader.delete();
        return reader;
    }


}
