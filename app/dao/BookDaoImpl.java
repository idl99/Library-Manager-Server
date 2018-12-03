package dao;

import exceptions.MaximumCapacityException;
import io.ebean.Ebean;
import models.Book;
import models.ItemTransactionLog;
import models.Reservation;

import javax.inject.Singleton;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Set;


/**
 * Implementation of the Book Dvd Dao Interface
 * Executes, handles and perform CRUD operations on Books' data.
 * @Singleton annotation is used to indicate to Guice that this class should only instantiate a single instance
 * for entire application runtime.
 */
@Singleton
public class BookDaoImpl implements BookDao {


    /**
     * Default constructor required by Guice
     */
    public BookDaoImpl() {
    }


    /**
     * Getter for Book count from database
     * @return
     */
    @Override
    public int getBookCount() {
        return Ebean.find(Book.class).findCount();
    }


    /**
     * Getter to retrieve details of all Book from database
     * @return
     */
    @Override
    public Set<Book> getAllBooks() {
        return Ebean.find(Book.class).findSet();
    }


    /**
     * Getter to retrieve details of a particular Book by quering database using Book ISBN.
     * @param isbn
     * @return
     * @throws EntityNotFoundException - when a Book is not found for given ISBN.
     */
    @Override
    public Book getBookByIsbn(String isbn) throws EntityNotFoundException{
        Book toReturn = Ebean.find(Book.class).where().idEq(isbn).findOne();
        if(toReturn == null)
            throw new EntityNotFoundException("Failed to find Book for ISBN given.");
        else
            return toReturn;
    }


    /**
     * Method to insert details of new Book into database
     * @param book
     * @throws MaximumCapacityException - when library has reached maximum capacity of 100 Books
     * @throws EntityExistsException - when database already has a record of the given Book
     */
    @Override
    public void insertBook(Book book) throws MaximumCapacityException, EntityExistsException {
        if(getBookCount()<=Book.MAX_LIBRARY_CAPACITY){
            if(Ebean.find(Book.class).where().idEq(book.getISBN()).findCount()==0) {
                book.save();
                ItemTransactionLog.initLogForItem(book);
                Reservation.initReservationsForItem(book);
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


    /**
     * Method to update the details of existing Book in the database.
     * @param book
     * @throws EntityNotFoundException - when a Book is not found for the given ISBN in the database.
     */
    @Override
    public void updateBook(Book book) throws EntityNotFoundException {
        if(Ebean.find(Book.class).where().idEq(book.getISBN()).findCount()==0) {
            throw new EntityNotFoundException("Failed to update Book. Book not found for given ISBN.");
        }
        else {
            book.update();
        }
    }


    /**
     * Method to delete details of a Book from the database.
     * @param isbn
     * @return
     * @throws EntityNotFoundException - when a Book is not found for the given ISBN in the database.
     */
    @Override
    public Book deleteBook(String isbn) throws EntityNotFoundException {
        Book book = getBookByIsbn(isbn);
        book.delete();
        return book;
    }


}
