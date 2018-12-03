package models;

import io.ebean.annotation.DbArray;
import utils.MyDateUtil;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * Model class to design and represent Books
 * Extends Library Item since Book is-a Library Item
 * @Entity JPA annotation is used to denote that this class models an entity which needs to be persisted and stored in a table.
 * @Table annotation is used to give explicit name for the table in which Books need to be persisted.
 */
@Entity
@Table(name = "book")
public class Book extends LibraryItem{


    /**
     * Maximum Library capacity for Books
     */
    public final static int MAX_LIBRARY_CAPACITY = 100;


    /**
     * Common maximum borrowal period for all Books
     */
    public final static int MAX_BORROWAL_PERIOD = 7;


    /**
     * The list of authors who have authored the book
     * @DbArray Ebeans Annotation is used to indicate to Ebeans that values in the List need to be persisted inside an
     * SQL array.
     */
    @DbArray
    private List<String> authors; // list of author/s who've written the book


    /**
     * The publisher who has published the book
     * @DbArray Ebeans Annotation is used to indicate to Ebeans that values in the List need to be persisted inside an
     * SQL array.
     */
    private String publisher; // publisher who has published the book


    /**
     * The number of pages in the book
     */
    private int noOfPages; // number of pages in the book

    /**
     * Constructor for Book objects
     * @param ISBN
     * @param title
     * @param section
     * @param pubDate
     * @param currentReader
     * @param borrowedOn
     * @param authors
     * @param publisher
     * @param noOfPages
     */
    public Book(String ISBN, String title, String section, MyDateUtil pubDate, Reader currentReader, MyDateUtil borrowedOn,
                List<String> authors, String publisher, int noOfPages) {
        super(ISBN, title, section, pubDate, currentReader, borrowedOn);
        this.authors = authors;
        this.publisher = publisher;
        this.noOfPages = noOfPages;
    }


    /**
     * Getter for Authors
     * @return
     */
    public List<String> getAuthors() {
        return authors;
    }


    /**
     * Setter for Authors
     * @param authors
     */
    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }


    /**
     * Getter for Publisher
     * @return
     */
    public String getPublisher() {
        return publisher;
    }


    /**
     * Setter for Publisher
     * @param publisher
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }


    /**
     * Getter for number of pages
     * @return
     */
    public int getNoOfPages() {
        return noOfPages;
    }


    /**
     * Setter for number of pages
     * @param noOfPages
     */
    public void setNoOfPages(int noOfPages) {
        this.noOfPages = noOfPages;
    }


}
