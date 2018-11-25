package dao;

import io.ebean.Ebean;
import models.Reader;

import javax.inject.Singleton;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Set;

@Singleton
public class ReaderDaoImpl implements ReaderDao {

    // Default empty constructor
    public ReaderDaoImpl() {
    }

    @Override
    public int getReaderCount() {
        return Ebean.find(Reader.class).findCount();
    }

    @Override
    public Set<Reader> getAllReaders() {
        return Ebean.find(Reader.class).findSet();
    }

    @Override
    public Reader getReaderById(String id) throws EntityNotFoundException {
        Reader toReturn = Ebean.find(Reader.class).where().idEq(id).findOne();
        if(toReturn == null)
            throw new EntityNotFoundException("Failed to find Reader for Reader Id given.");
        else
            return toReturn;
    }

    @Override
    public void insertReader(Reader reader) throws EntityExistsException {
        if(Ebean.find(Reader.class).where().eq("name",reader.getName()).findCount()==0) {
            reader.save();
        }
        else {
            throw new EntityExistsException("Reader is already registered.");
        }
    }

    @Override
    public void updateReader(Reader reader) throws EntityNotFoundException {
        if(Ebean.find(Reader.class).where().idEq(reader.getReaderId()).findCount()==0) {
            throw new EntityNotFoundException("Failed to update Reader details. Reader Id not found.");
        }
        else {
            reader.update();
        }
    }

    @Override
    public Reader deleteReader(String id) throws EntityNotFoundException {
        Reader reader = getReaderById(id);
        reader.delete();
        return reader;
    }

}
