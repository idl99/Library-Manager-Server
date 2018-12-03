package dao;

import com.google.inject.ImplementedBy;
import exceptions.MaximumCapacityException;
import models.Book;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Set;


/**
 * Interface that declares the contract for behavior that all Book Data Access Object Implementations should
 * define and implement.
 *
 * @ImplementedBy tells Guice which classes (BookDaoImpl.class) implements this interface.
 *
 */
@ImplementedBy(BookDaoImpl.class)
public interface BookDao {

    int getBookCount();

    Set<Book> getAllBooks();

    Book getBookByIsbn(String isbn) throws EntityNotFoundException;

    void insertBook(Book book) throws MaximumCapacityException, EntityExistsException;

    void updateBook(Book book) throws EntityNotFoundException;

    Book deleteBook(String isbn) throws EntityNotFoundException;

}
