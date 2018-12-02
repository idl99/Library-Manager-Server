package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.ebean.annotation.DbArray;
import utils.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "book")
public class Book extends LibraryItem{

    public final static int MAX_BORROWAL_PERIOD = 7;

    @JsonProperty("authors")
    @DbArray
    private List<String> authors; // list of author/s who've written the book

    private String publisher; // publisher who has published the book

    private int noOfPages; // number of pages in the book

    public Book(String ISBN, String title, String section, Date pubDate, Reader currentReader, Date borrowedOn,
                List<String> authors, String publisher, int noOfPages) {
        super(ISBN, title, section, pubDate, currentReader, borrowedOn);
        this.authors = authors;
        this.publisher = publisher;
        this.noOfPages = noOfPages;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getNoOfPages() {
        return noOfPages;
    }

    public void setNoOfPages(int noOfPages) {
        this.noOfPages = noOfPages;
    }

}
