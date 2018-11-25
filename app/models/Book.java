package models;

import io.ebean.annotation.DbArray;
import utils.DateTime;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "book")
public class Book extends LibraryItem implements Comparable<Book>{

    @DbArray
    private List<String> authors; // list of author/s who've written the book

    private String publisher; // publisher who has published the book

    private int noOfPages; // number of pages in the book

    public Book(String ISBN, String title, String section, DateTime pubDate, Reader currentReader, DateTime borrowedOn,
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

    @Override
    public int compareTo(Book o) {
        return this.getISBN().compareTo(o.getISBN());
    }

}
