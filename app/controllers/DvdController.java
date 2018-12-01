package controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.DvdDao;
import exceptions.MaximumCapacityException;
import models.Dvd;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Set;

public class DvdController extends Controller {

    DvdDao dvdDao;

    @Inject
    public DvdController(DvdDao bookDao) {
        this.dvdDao = bookDao;
    }

    public Result list() {
        Set<Dvd> setOfDvd = dvdDao.getAllDvd();
        if(setOfDvd.size() == 0)
            return ok("No DVD found.");
        else
            return ok(Json.toJson(setOfDvd));
    }

    public Result get(String isbn) {
        try {
            Dvd toReturn = dvdDao.getDvdByIsbn(isbn);
            return ok(Json.toJson(toReturn));
        } catch (EntityNotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    public Result save() {
        // extract POST request body
        JsonNode requestBody = request().body().asJson();
        Dvd dvd;
        if (requestBody == null) {
            return badRequest("Empty POST request");
        } else {
            dvd = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY).
                    convertValue(requestBody, Dvd.class);
            try {
                dvdDao.insertDvd(dvd);
                // Create new JSON object node
                ObjectNode result = Json.newObject();
                // embed inserted DVD object and message to be sent to client as Http POST request response
                result.put("message", "Successfully added new DVD.");
                result.set("inserted", Json.toJson(dvd));
                return created(result); // Return HTTP response
            } catch (MaximumCapacityException | EntityExistsException e) {
                return forbidden(e.getMessage());
            }
        }
    }

    public Result update() {
        // Get PUT request body
        JsonNode requestBody = request().body().asJson();
        Dvd dvd;
        if (requestBody == null) {
            return badRequest("Empty POST request.");
        } else {
            dvd = new ObjectMapper().convertValue(requestBody, Dvd.class);
            try {
                dvdDao.updateDvd(dvd);
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
            Dvd deletedBook = dvdDao.deleteDvd(isbn);
            // embed deleted DVD object and message to be sent to client as Http DELETE request response
            result.put("message", "Successfully deleted");
            result.set("deleted", Json.toJson(deletedBook));
            return ok(result);
        } catch (EntityNotFoundException e) {
            return notFound(e.getMessage());
        }
    }

}
