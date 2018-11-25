package dao;

import exceptions.MaximumCapacityException;
import io.ebean.Ebean;
import models.Book;

import javax.inject.Singleton;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Set;

@Singleton
public class BookDaoImpl implements BookDao {

    // Default empty constructor
    public BookDaoImpl() {
    }

    @Override
    public int getBookCount() {
        return Ebean.find(Book.class).findCount();
    }

    @Override
    public Set<Book> getAllBooks() {
        return Ebean.find(Book.class).findSet();
    }

    @Override
    public Book getBookByIsbn(String isbn) throws EntityNotFoundException{
        Book toReturn = Ebean.find(Book.class).where().idEq(isbn).findOne();
        if(toReturn == null)
            throw new EntityNotFoundException("Failed to find Book for ISBN given.");
        else
            return toReturn;
    }

    @Override
    public void insertBook(Book book) throws MaximumCapacityException, EntityExistsException {
        if(getBookCount()<=100){
            if(Ebean.find(Book.class).where().idEq(book.getISBN()).findCount()==0) {
                book.save();
            }
            else {
                throw new EntityExistsException("Failed to create new Book. Book already exists for given ISBN.");
            }
        }
        else {
            throw new MaximumCapacityException("Library has reached maximum Book capacity. " +
                    "Please discard old/damaged books.");
        }
    }

    @Override
    public void updateBook(Book book) throws EntityNotFoundException {
        if(Ebean.find(Book.class).where().idEq(book.getISBN()).findCount()==0) {
            throw new EntityNotFoundException("Failed to update Book. Book not found for given ISBN.");
        }
        else {
            book.update();
        }
    }

    @Override
    public Book deleteBook(String isbn) throws EntityNotFoundException {
        Book book = getBookByIsbn(isbn);
        book.delete();
        return book;
    }

}
