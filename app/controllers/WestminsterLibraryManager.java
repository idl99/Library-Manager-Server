package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.BookDao;
import dao.DvdDao;
import io.ebean.Ebean;
import models.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.MyDateUtil;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Controller class which services common Library business functions requested by the Client
 * Extends Controller class of play.mvc
 * Implements custom interface Library Manager
 */
public class WestminsterLibraryManager extends Controller implements LibraryManager {

    /**
     * Reference to Book Data Access Object to pass instructions and execute Book-related CRUD operations
     */
    BookDao bookDao;

    /**
     * Reference to Dvd Data Access Object to pass instructions and execute Dvd-related CRUD operations
     */
    DvdDao dvdDao;


    /**
     * Constructor method. Uses Guice dependency injection to resolve BookDao and DvdDao dependency
     * @param bookDaoImpl - Book Dao Implementation instance
     * @param dvdDaoImpl - Dvd Dao Implementation instance
     */
    @Inject
    public WestminsterLibraryManager(BookDao bookDaoImpl, DvdDao dvdDaoImpl) {
        super();
        this.bookDao = bookDaoImpl;
        this.dvdDao = dvdDaoImpl;
    }


    /**
     * Method which services business logic related to Item being borrowed.
     * @return - returns an OK HTTP response to Client if logic for item being borrowed is successfully executed.
     */
    @Override
    public Result borrowItem() {

        JsonNode requestBody = request().body().asJson();

        if (requestBody == null) {

            return badRequest(Json.toJson("Empty POST request."));

        } else {

            String type = requestBody.get("type").textValue();
            String isbn = requestBody.get("isbn").textValue();
            String readerId = requestBody.get("readerId").textValue();
            String borrowedOn = requestBody.get("borrowedOn").textValue();

            LibraryItem item;
            Reader reader = new Reader();

            switch (type){
                case("Book"):
                    item = bookDao.getBookByIsbn(isbn);
                    reader.setReaderId(readerId);
                    item.setCurrentReader(reader);
                    item.setBorrowedOn(new MyDateUtil(borrowedOn));
                    bookDao.updateBook((Book)item);
                    break;
                case("Dvd"):
                    item = dvdDao.getDvdByIsbn(isbn);
                    reader.setReaderId(readerId);
                    item.setCurrentReader(reader);
                    item.setBorrowedOn(new MyDateUtil(borrowedOn));
                    dvdDao.updateDvd((Dvd)item);
                    break;
            }

            ItemTransactionLog log = ItemTransactionLog.getLogByItem(isbn);
            log.incrementNoOfTimesBorrowed(); // Increase borrow count in ItemTransactionLog
            log.update();

            // Removing reader from list of reservations, if reader has made reservation for item before
            Reservation reservation = Reservation.getReservationById(isbn);
            if(reservation!=null){
                if(reservation.getListOfReaders().contains(readerId)){
                    reservation.getListOfReaders().remove(readerId);
                    reservation.update();
                }
            }

            return ok(Json.toJson("Item successfully borrowed. Please return the item within the overdue period to avoid " +
                    "any late item return fees."));

        }
    }


    /**
     * Method which services business logic related to Item return.
     * @return - returns an OK HTTP response with any late item return fee if needed to be paid off by Reader.
     */
    @Override
    public Result returnItem() {

        JsonNode requestBody = request().body().asJson();

        if (requestBody == null) {

            return badRequest("Empty POST request.");

        } else {

            String type = requestBody.get("type").textValue();
            String isbn = requestBody.get("isbn").textValue();
            MyDateUtil returnedOn = new MyDateUtil(requestBody.get("returnedOn").textValue());

            LibraryItem item;
            BigDecimal dueFee = null;

            switch (type){
                case("Book"):
                    item = bookDao.getBookByIsbn(isbn);
                    ItemTransactionLog.updateOnItemReturn(item,returnedOn); // Update ItemTransactionLog that item
                                                                            // has been returned by user
                    dueFee = item.calculateLateFee(returnedOn, Book.MAX_BORROWAL_PERIOD);
                    item.setCurrentReader(null); // Reset current reader attribute value
                    item.setBorrowedOn(null); // Reset borrowed on attribute value
                    bookDao.updateBook((Book)item);

                    break;
                case("Dvd"):
                    item = dvdDao.getDvdByIsbn(isbn);
                    ItemTransactionLog.updateOnItemReturn(item,returnedOn); // Update ItemTransactionLog that item
                                                                            // has been returned by user
                    dueFee = item.calculateLateFee(returnedOn, Dvd.MAX_BORROWAL_PERIOD);
                    item.setCurrentReader(null); // Reset current reader attribute value
                    item.setBorrowedOn(null); // Reset borrowed on attribute value
                    dvdDao.updateDvd((Dvd)item);

                    break;
            }

            return ok(Json.toJson(String.format("Successfully returned book. Due fee is $%s",dueFee.toString())));

        }
    }


    /**
     * Method which services business logic related to Item reservation.
     * @return - an OK HTTP result with the estimated waiting time based on the reservation queue
     */
    @Override
    public Result reserveItem() {
        JsonNode requestBody = request().body().asJson();
        String type = requestBody.get("type").textValue();
        String isbn = requestBody.get("isbn").textValue();
        String readerId = requestBody.get("readerId").textValue();

        Reservation reservation = Reservation.getReservationById(isbn);
        reservation.getListOfReaders().add(readerId);
        reservation.update();

        int avgBorrowalPeriod = ItemTransactionLog.getLogByItem(isbn).getAverageBorrowalPeriod();
        int noOfReadersInQueue = reservation.getListOfReaders().size();

        LibraryItem item;
        ObjectNode result = Json.newObject();
        if (type.equals("Book")) {
            Book book = bookDao.getBookByIsbn(isbn);
        } else if(type.equals("Dvd")) {
            Dvd dvd = dvdDao.getDvdByIsbn(isbn);
        }

        return ok(Json.toJson(String.format("Successfully reserved item. Estimated waiting time is %s days.",
                (noOfReadersInQueue * avgBorrowalPeriod))));

    }

    /**
     * Method that services the business logic for reporting on items borrowed, and their status
     * (number of days which they are overdue by), and late return fee estimates.
     * @param generateFor - the date for which the report needs to be generated for
     * @return - a custom JSON object, with the overude items, their isbn, title, date on which they were borrowed,
     * the number of days by which they are overdue, and the late return fee
     */
    @Override
    public Result report(String generateFor) {

        Set<Book> setOfBook = Ebean.find(Book.class).where().not().eq("borrowed_on",null).findSet();
        Set<Dvd> setOfDvd = Ebean.find(Dvd.class).where().not().eq("borrowed_on",null).findSet();

        Set<LibraryItem> libraryItems = new LinkedHashSet<>();
        libraryItems.addAll(setOfBook);
        libraryItems.addAll(setOfDvd);

        ObjectNode result = Json.newObject();
        ArrayNode arrayOfItems = result.putArray("items");

        for(LibraryItem item: libraryItems){

            ObjectNode itemNode = Json.newObject();

            itemNode.put("isbn",item.getISBN());
            itemNode.put("title",item.getTitle());

            if(item.getBorrowedOn() == null) {
                itemNode.put("borrowedOn","");
            } else{
                itemNode.put("borrowedOn",item.getBorrowedOn().toString());
            }

            if(item instanceof Book){
                itemNode.put("overdueBy", MyDateUtil.getDifference(new MyDateUtil(generateFor),
                        item.getBorrowedOn()) - Book.MAX_BORROWAL_PERIOD);
                itemNode.put("fee", item.calculateLateFee(new MyDateUtil(generateFor),
                        Book.MAX_BORROWAL_PERIOD));

            } else if(item instanceof Dvd){
                itemNode.put("overdueBy", MyDateUtil.getDifference(new MyDateUtil(generateFor),
                        item.getBorrowedOn())- Dvd.MAX_BORROWAL_PERIOD);
                itemNode.put("fee", item.calculateLateFee(new MyDateUtil(generateFor),
                        Dvd.MAX_BORROWAL_PERIOD));
            }

            arrayOfItems.add(itemNode);

        }

        return ok(result);

    }

}