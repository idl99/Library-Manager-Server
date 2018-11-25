package models;

import io.ebean.Model;
import io.ebean.annotation.DbJson;
import io.ebean.annotation.NotNull;
import utils.DateTime;

import javax.persistence.*;

@MappedSuperclass
public abstract class LibraryItem extends Model {

    @Id
    protected String ISBN; // isbn of item

    @NotNull
    protected String title; // title of item

    protected String section; // section to which item belongs

    @DbJson
    protected DateTime pubDate; // date on which the item has been published

    @ManyToOne
    protected Reader currentReader; // reader who has currently borrowed the book

    @DbJson
    protected DateTime borrowedOn; // date on which the book has been borrowed

    // Default empty constructor
    public LibraryItem(){
    }

    public LibraryItem(String ISBN, String title, String section,
                       DateTime pubDate, Reader currentReader, DateTime borrowedOn) {
        this.ISBN = ISBN;
        this.title = title;
        this.section = section;
        this.pubDate = pubDate;
        this.currentReader = currentReader;
        this.borrowedOn = borrowedOn;
    }

    public final String getISBN() {
        return ISBN;
    }

    public final void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public final String getTitle() {
        return title;
    }

    public final void setTitle(String title) {
        this.title = title;
    }

    public final String getSection() {
        return section;
    }

    public final void setSection(String section) {
        this.section = section;
    }

    public final DateTime getPubDate() {
        return pubDate;
    }

    public final void setPubDate(DateTime pubDate) {
        this.pubDate = pubDate;
    }

    public final Reader getCurrentReader() {
        return currentReader;
    }

    public final void setCurrentReader(Reader currentReader) {
        this.currentReader = currentReader;
    }

    public final DateTime getBorrowedOn() {
        return borrowedOn;
    }

    public final void setBorrowedOn(DateTime borrowedOn) {
        this.borrowedOn = borrowedOn;
    }

    @Override
    public final boolean equals(Object obj) {
        // check if same object-wise (memory address)
        if(super.equals(obj)){
            return true;
        }

        // check if same library-item-wise (same ISBN)
        LibraryItem item= (LibraryItem) obj;
        return ISBN.equals(item.getISBN());
    }

}
