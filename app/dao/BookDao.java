package dao;

import com.google.inject.ImplementedBy;
import exceptions.MaximumCapacityException;
import models.Book;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Set;

@ImplementedBy(BookDaoImpl.class)
public interface BookDao {

    int getBookCount();

    Set<Book> getAllBooks();

    Book getBookByIsbn(String isbn) throws EntityNotFoundException;

    void insertBook(Book book) throws MaximumCapacityException, EntityExistsException;

    void updateBook(Book book) throws EntityNotFoundException;

    Book deleteBook(String isbn) throws EntityNotFoundException;

}
