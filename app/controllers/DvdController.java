package controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.DvdDao;
import exceptions.MaximumCapacityException;
import models.Dvd;
import models.ItemTransactionLog;
import models.Reservation;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Set;


/**
 * Controller class which services Dvd CRUD functions requested by the Client
 * Extends Controller class of play.mvc
 */
public class DvdController extends Controller {


    /**
     * Reference to Dvd Data Access Object to pass instructions and execute Dvd-related CRUD operations
     */
    DvdDao dvdDao;


    /**
     * Constructor method. Uses Guice dependency injection to resolve DvdDao dependency
     * @param dvdDao
     */
    @Inject
    public DvdController(DvdDao dvdDao) {
        this.dvdDao = dvdDao;
    }


    /**
     * Method which services and responds to Client request for listing all existing Dvds.
     * @return - returns an OK HTTP response with a JSON object containing set of Dvd, returns a Not Found HTTP response
     * if no Dvd exists.
     */
    public Result list() {
        Set<Dvd> setOfDvd = dvdDao.getAllDvd();
        if(setOfDvd.size() == 0)
            return notFound(Json.toJson("No Dvd's found. Please add Dvd's to database first."));
        else
            return ok(Json.toJson(setOfDvd));
    }


    /**
     * Method which services and responds to Client request for retrieving details of an existing Dvd given its ISBN
     * @param isbn
     * @return - an OK HTTP response with Dvd details if found, else a Not Found response with an error message.
     */
    public Result get(String isbn) {
        try {
            Dvd toReturn = dvdDao.getDvdByIsbn(isbn);
            return ok(Json.toJson(toReturn));
        } catch (EntityNotFoundException e) {
            return notFound(Json.toJson("Dvd not found for given ISBN."));
        }
    }


    /**
     * Method which services and responds to Client request for saving details of a new Dvd into the database.
     * @return - an OK HTTP response if Dvd was successfully inserted into the database, or a Forbidden response
     * if the library has reached maximum capacity for Dvd or already has a record for the given Dvd or a Bad Request
     * response if the request sent by Client is empty.
     */
    public Result save() {
        JsonNode requestBody = request().body().asJson();
        Dvd dvd;
        if (requestBody == null) {
            return badRequest(Json.toJson("Empty POST request"));
        } else {
            dvd = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY).
                    convertValue(requestBody, Dvd.class);
            try {
                dvdDao.insertDvd(dvd);
                return created(Json.toJson(String.format("Inserted item into database. Remaining library capacity " +
                                "for Dvd is %s.",
                        String.valueOf(Dvd.MAX_LIBRARY_CAPACITY - dvdDao.getDvdCount()))));
            } catch (MaximumCapacityException | EntityExistsException e) {
                if(e instanceof  MaximumCapacityException)
                    return forbidden(Json.toJson("Maximum capacity for Dvd reached. Please delete record of any " +
                            "damaged or " + "non existent Dvd."));
                else {
                    return forbidden(Json.toJson("Record already exists for the given Dvd. Please recheck " +
                            "and enter details"));
                }
            }
        }
    }


    /**
     * Method which services and responds to Client request to update details of a Dvd in the database.
     * @return - an OK HTTP response if the Dvd was succesfully updated, otherwise return a Not Found response if a Dvd
     * was not found in the database for the given ISBN, or else a Bad Request if Dvd not found for given isbn.
     */
    public Result update() {
        JsonNode requestBody = request().body().asJson();
        Dvd dvd;
        if (requestBody == null) {
            return badRequest(Json.toJson("Empty POST request"));
        } else {
            dvd = new ObjectMapper().convertValue(requestBody, Dvd.class);
            try {
                dvdDao.updateDvd(dvd);
                return ok(Json.toJson("Successfully updated."));
            } catch (EntityNotFoundException e) {
                return notFound(Json.toJson("Dvd not found in the database for given ISBN."));
            }
        }
    }


    /**
     * Method which services and responds to Client request to delete details of a Dvd in the database.
     * @return - an OK HTTP response if the Dvd was succesfully deleted, otherwise return a Not Found response if a Dvd
     * was not found in the database for the given ISBN, or else a Bad Request if Dvd not found for given isbn.
     */
    public Result delete(String isbn) {
        try {
            Dvd deletedBook = dvdDao.deleteDvd(isbn);
            Reservation reservation;
            if((reservation = Reservation.getReservationById(isbn)) != null){
                reservation.delete();
            }
            ItemTransactionLog.db().delete(ItemTransactionLog.getLogByItem(isbn));
            return ok(Json.toJson(String.format("Successfully deleted Dvd. Remaining library capacity for Dvd is %s",
                    String.valueOf(Dvd.MAX_LIBRARY_CAPACITY - dvdDao.getDvdCount()))));
        } catch (EntityNotFoundException e) {
            return notFound(Json.toJson("Dvd not found in the database for given ISBN."));
        }
    }

}
