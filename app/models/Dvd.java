package models;

import io.ebean.annotation.DbArray;
import utils.MyDateUtil;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * Model class to design and represent Dvd's
 * Extends Library Item since Dvd is-a Library Item
 * @Entity JPA annotation is used to denote that this class models an entity which needs to be persisted in a table.
 * @Table annotation is used to give explicit name for the table in which Dvds need to be persisted.
 */
@Entity
@Table(name = "dvd")
public class Dvd extends LibraryItem {


    /**
     * Maximum library capacity for Dvd
     */
    public final static int MAX_LIBRARY_CAPACITY = 50;


    /**
     * Common maximum borrowal period for all Dvd
     */
    public final static int MAX_BORROWAL_PERIOD = 3;


    /**
     * The list of language in which audio is available in the Dvd
     * @DbArray Ebeans Annotation is used to indicate to Ebeans that values in the List need to be persisted inside an
     * SQL array.
     */
    @DbArray
    private List<String> audio; // list of available audio languages embedded in DVD


    /**
     * The list of languages in which subtitles are available in the Dvd
     * @DbArray Ebeans Annotation is used to indicate to Ebeans that values in the List need to be persisted inside an
     * SQL array.
     */
    @DbArray
    private List<String> subtitles; // list of subtitle languages embedded in DVD


    /**
     * The list of producer who has produced the Dvd
     */
    private String producer; // producer of DVD


    /**
     * The list of actors who act in the Dvd
     * @DbArray Ebeans Annotation is used to indicate to Ebeans that values in the List need to be persisted inside an
     * SQL array.
     */
    @DbArray
    private List<String> actors; // actors who act in the DVD title


    /**
     * Constructor used to create Dvd objects
     * @param ISBN
     * @param title
     * @param section
     * @param pubDate
     * @param currentReader
     * @param borrowedOn
     * @param audio
     * @param subtitles
     * @param producer
     * @param actors
     */
    public Dvd(String ISBN, String title, String section, MyDateUtil pubDate, Reader currentReader, MyDateUtil borrowedOn,
               List<String> audio, List<String> subtitles, String producer, List<String> actors) {
        super(ISBN, title, section, pubDate, currentReader, borrowedOn);
        this.audio = audio;
        this.subtitles = subtitles;
        this.producer = producer;
        this.actors = actors;
    }


    /**
     * Getter for audio languages
     * @return
     */
    public List<String> getAudio() {
        return audio;
    }


    /**
     * Setter for audio languages
     * @param audio
     */
    public void setAudio(List<String> audio) {
        this.audio = audio;
    }


    /**
     * Getter for subtitles' languages
     * @return
     */
    public List<String> getSubtitles() {
        return subtitles;
    }


    /**
     * Setter for subtitles' languages
     * @param subtitles
     */
    public void setSubtitles(List<String> subtitles) {
        this.subtitles = subtitles;
    }


    /**
     * Getter for producer
     * @return
     */
    public String getProducer() {
        return producer;
    }


    /**
     * Setter for producer
     * @param producer
     */
    public void setProducer(String producer) {
        this.producer = producer;
    }


    /**
     * Getter for actors
     * @return
     */
    public List<String> getActors() {
        return actors;
    }

    /**
     * Setter for actors
     * @param actors
     */
    public void setActors(List<String> actors) {
        this.actors = actors;
    }


}
