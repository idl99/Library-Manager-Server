package controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.BookDao;
import exceptions.MaximumCapacityException;
import models.Book;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Set;

public class BookController extends Controller {

    BookDao bookDao;

    @Inject
    public BookController(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public Result list() {
        Set<Book> setOfBook = bookDao.getAllBooks();
        if(setOfBook.size() == 0)
            return ok("No books found.");
        else
            return ok(Json.toJson(setOfBook));
    }

    public Result get(String isbn) {
        try {
            Book toReturn = bookDao.getBookByIsbn(isbn);
            return ok(Json.toJson(toReturn));
        } catch (EntityNotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    public Result save() {
        // extract POST request body
        JsonNode requestBody = request().body().asJson();
        Book book;
        if (requestBody == null) {
            return badRequest("Empty POST request");
        } else {
            book = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY).convertValue(requestBody, Book.class);
            try {
                bookDao.insertBook(book);
                // Create new JSON object node
                ObjectNode result = Json.newObject();
                // embed inserted Book object and message to be sent to client as Http POST request response
                result.put("message", "Successfully added new book.");
                result.set("inserted", Json.toJson(book));
                return created(result); // Return HTTP response
            } catch (MaximumCapacityException | EntityExistsException e) {
                return forbidden(e.getMessage());
            }
        }
    }

    public Result update() {
        // Get PUT request body
        JsonNode requestBody = request().body().asJson();
        Book book;
        if (requestBody == null) {
            return badRequest("Empty POST request.");
        } else {
            book = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY).convertValue(requestBody, Book.class);
            try {
                bookDao.updateBook(book);
                return ok("Successfully updated.");
            } catch (EntityNotFoundException e) {
                return notFound(e.getMessage());
            }
        }
    }

    public Result delete(String isbn) {
        // Create new JSON object node
        ObjectNode result = Json.newObject();
        try {
            // Delete book and store
            Book deletedBook = bookDao.deleteBook(isbn);
            // embed deleted Book object and message to be sent to client as Http DELETE request response
            result.put("message", "Successfully deleted");
            result.set("deleted", Json.toJson(deletedBook));
            return ok(result);
        } catch (EntityNotFoundException e) {
            return notFound(e.getMessage());
        }
    }

}