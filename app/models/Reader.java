package models;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reader")
public class Reader extends Model {

    @Id
    private String readerId; // unique id of reader

    @NotNull
    private String name; // name of reader

    private String mobile; // contact mobile number of reader

    private String email; // email of reader

    /**
     * Default constructor
     */
    public Reader() {
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object obj) {
        // check if same Object-wise (memory address)
        if(super.equals(obj)){
            return true;
        }

        // check if same Reader-wise (same readerId)
        Reader reader = (Reader) obj;
        return readerId.equals(reader.getReaderId());
    }

}
