package models;

import io.ebean.annotation.DbArray;
import utils.MyDateUtil;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "dvd")
public class Dvd extends LibraryItem {

    public final static int MAX_BORROWAL_PERIOD = 3;

    @DbArray
    private List<String> audio; // list of available audio languages embedded in DVD
    @DbArray
    private List<String> subtitles; // list of subtitle languages embedded in DVD
    private String producer; // producer of DVD
    @DbArray
    private List<String> actors; // actors who act in the DVD title

    public Dvd(String ISBN, String title, String section, MyDateUtil pubDate, Reader currentReader, MyDateUtil borrowedOn,
               List<String> audio, List<String> subtitles, String producer, List<String> actors) {
        super(ISBN, title, section, pubDate, currentReader, borrowedOn);
        this.audio = audio;
        this.subtitles = subtitles;
        this.producer = producer;
        this.actors = actors;
    }

    public List<String> getAudio() {
        return audio;
    }

    public void setAudio(List<String> audio) {
        this.audio = audio;
    }

    public List<String> getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(List<String> subtitles) {
        this.subtitles = subtitles;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

}
