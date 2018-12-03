package controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.BookDao;
import exceptions.MaximumCapacityException;
import models.Book;
import models.Reservation;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Set;


/**
 * Controller class which services Book CRUD functions requested by the Client
 * Extends Controller class of play.mvc
 */
public class BookController extends Controller {


    /**
     * Reference to Book Data Access Object to pass instructions and execute Dvd-related CRUD operations
     */
    BookDao bookDao;


    /**
     * Constructor method. Uses Guice dependency injection to resolve BookDao dependency
     * @param bookDao
     */
    @Inject
    public BookController(BookDao bookDao) {
        this.bookDao = bookDao;
    }


    /**
     * Method which services and responds to Client request for listing all existing Books.
     * @return - returns an OK HTTP response with a JSON object containing set of Book, returns a Not Found HTTP response
     * if no Book exists.
     */
    public Result list() {
        Set<Book> setOfBook = bookDao.getAllBooks();
        if(setOfBook.size() == 0)
            return notFound(Json.toJson("No Books found. Please add Books to database first."));
        else
            return ok(Json.toJson(setOfBook));
    }


    /**
     * Method which services and responds to Client request for retrieving details of an existing Book given its ISBN
     * @param isbn
     * @return - an OK HTTP response with Book details if found, else a Not Found response with an error message.
     */
    public Result get(String isbn) {
        try {
            Book toReturn = bookDao.getBookByIsbn(isbn);
            return ok(Json.toJson(toReturn));
        } catch (EntityNotFoundException e) {
            return notFound(Json.toJson("Book not found for given ISBN."));
        }
    }


    /**
     * Method which services and responds to Client request for saving details of a new Book into the database.
     * @return - an OK HTTP response if Book was successfully inserted into the database, or a Forbidden response
     * if the library has reached maximum capacity for Book or already has a record for the given Book or a Bad Request
     * response if the request sent by Client is empty.
     */
    public Result save() {
        JsonNode requestBody = request().body().asJson();
        Book book;
        if (requestBody == null) {
            return badRequest(Json.toJson("Empty POST request"));
        } else {
            book = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY).
                    convertValue(requestBody, Book.class);
            try {
                bookDao.insertBook(book);
                return created(Json.toJson(String.format("Inserted item into database. Remaining library capacity " +
                                "for Book is %s.",
                        String.valueOf(Book.MAX_LIBRARY_CAPACITY - bookDao.getBookCount()))));
            } catch (MaximumCapacityException | EntityExistsException e) {
                if(e instanceof  MaximumCapacityException)
                    return forbidden(Json.toJson("Maximum capacity for Book reached. Please delete record of any " +
                            "damaged or " + "non existent Book."));
                else {
                    return forbidden(Json.toJson("Record already exists for the given Book. Please recheck " +
                            "and enter details"));
                }
            }
        }
    }


    /**
     * Method which services and responds to Client request to update details of a Book in the database.
     * @return - an OK HTTP response if the Book was succesfully updated, otherwise return a Not Found response if a Book
     * was not found in the database for the given ISBN, or else a Bad Request if Book not found for given isbn.
     */
    public Result update() {
        JsonNode requestBody = request().body().asJson();
        Book book;
        if (requestBody == null) {
            return badRequest(Json.toJson("Empty POST request."));
        } else {
            book = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY).convertValue(requestBody, Book.class);
            try {
                bookDao.updateBook(book);
                return ok(Json.toJson("Successfully updated."));
            } catch (EntityNotFoundException e) {
                return notFound(Json.toJson("Book not found in the database for given ISBN."));
            }
        }
    }


    /**
     * Method which services and responds to Client request to delete details of a Book in the database.
     * @param isbn
     * @return - an OK HTTP response if the Dvd was succesfully deleted, otherwise return a Not Found response if a Book
     * was not found in the database for the given ISBN, or else a Bad Request if Book not found for given isbn.
     */
    public Result delete(String isbn) {
        ObjectNode result = Json.newObject();
        try {
            Book deletedBook = bookDao.deleteBook(isbn);
            Reservation.getReservationById(isbn).delete();
            return ok(Json.toJson(String.format("Successfully deleted Book. Remaining library capacity for Book is %s",
                    String.valueOf(Book.MAX_LIBRARY_CAPACITY - bookDao.getBookCount()))));
        } catch (EntityNotFoundException e) {
            return notFound(Json.toJson("Book not found in the database for given ISBN."));
        }
    }

}