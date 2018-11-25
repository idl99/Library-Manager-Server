package dao;

import com.google.inject.ImplementedBy;
import models.Reader;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Set;

@ImplementedBy(ReaderDaoImpl.class)
public interface ReaderDao {

    int getReaderCount();

    Set<Reader> getAllReaders();

    Reader getReaderById(String id) throws EntityNotFoundException;

    void insertReader(Reader reader) throws EntityExistsException;

    void updateReader(Reader reader) throws EntityNotFoundException;

    Reader deleteReader(String id) throws EntityNotFoundException;

}
