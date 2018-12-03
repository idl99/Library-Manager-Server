package dao;

import com.google.inject.ImplementedBy;
import models.Reader;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Set;


/**
 * Interface that declares the contract for behavior that all Reader Data Access Object Implementations should
 * define and implement.
 *
 * @ImplementedBy tells Guice which classes (ReaderDaoImpl.class) implements this interface.
 *
 */
@ImplementedBy(ReaderDaoImpl.class)
public interface ReaderDao {

    int getReaderCount();

    Set<Reader> getAllReaders();

    Reader getReaderById(String id) throws EntityNotFoundException;

    void insertReader(Reader reader) throws EntityExistsException;

    void updateReader(Reader reader) throws EntityNotFoundException;

    Reader deleteReader(String id) throws EntityNotFoundException;

}
