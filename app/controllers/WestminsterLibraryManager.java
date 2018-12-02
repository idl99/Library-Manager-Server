package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.BookDao;
import dao.DvdDao;
import io.ebean.Ebean;
import models.Book;
import models.Dvd;
import models.LibraryItem;
import models.Reader;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.MyDateUtil;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

public class WestminsterLibraryManager extends Controller implements LibraryManager {

    BookDao bookDao;
    DvdDao dvdDao;

    @Inject
    public WestminsterLibraryManager(BookDao bookDaoImpl, DvdDao dvdDaoImpl) {
        super();
        this.bookDao = bookDaoImpl;
        this.dvdDao = dvdDaoImpl;
    }

    @Override
    public Result borrowItem() {
        JsonNode requestBody = request().body().asJson();
        if (requestBody == null) {
            return badRequest("Empty POST request.");
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
            return ok();
        }
    }

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
                    dueFee = LibraryItem.calculateLateFee("Book", returnedOn, item.getBorrowedOn());
                    item.setCurrentReader(null);
                    item.setBorrowedOn(null);
                    bookDao.updateBook((Book)item);
                    break;
                case("Dvd"):
                    item = dvdDao.getDvdByIsbn(isbn);
                    dueFee = LibraryItem.calculateLateFee("Dvd", returnedOn, item.getBorrowedOn());
                    item.setCurrentReader(null);
                    item.setBorrowedOn(null);
                    dvdDao.updateDvd((Dvd)item);
                    break;
            }

            return ok(dueFee.toString());

        }
    }
//
//    @Override
//    public Result reserveItem(String isbn, Reader readerId) {
//        return null;
//    }
//
    @Override
    public Result report(String generatedOn) {

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
                itemNode.put("overdueBy", MyDateUtil.getDifference(new MyDateUtil(generatedOn),item.getBorrowedOn()) -
                        Book.MAX_BORROWAL_PERIOD);
                itemNode.put("fee", LibraryItem.calculateLateFee("Book",new MyDateUtil(generatedOn),
                        item.getBorrowedOn()));
            } else if(item instanceof Dvd){
                itemNode.put("overdueBy", MyDateUtil.getDifference(new MyDateUtil(generatedOn),item.getBorrowedOn())-
                        Dvd.MAX_BORROWAL_PERIOD);
                itemNode.put("fee", LibraryItem.calculateLateFee("Dvd",new MyDateUtil(generatedOn),
                        item.getBorrowedOn()));
            }

            arrayOfItems.add(itemNode);

        }

        return ok(result);

    }

}