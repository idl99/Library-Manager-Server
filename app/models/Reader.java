package models;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Model class used to design and represent Readers (Patrons) of the Library.
 * @Entity JPA annotation is used to denote that this class models an entity which needs to be persisted in a table.
 * @Table annotation is used to give explicit name for the table in which this entity need to be persisted.
 */
@Entity
@Table(name = "reader")
public class Reader extends Model {


    /**
     * Id of each Reader. Used as unique identifier.
     * @Id JPA annotation is used by Ebeans to identify this field as the unique identifier for Reader.
     * */
    @Id
    private String readerId; // unique id of reader

    /**
     * Name of each Reader.
     * @NotNull JPA annotation is used to indicate that this attribute cannot be null, as in
     * every Reader needs to have a name.
     */
    @NotNull
    private String name; // name of reader


    /**
     * Mobile contact number of each Reader.
     */
    private String mobile; // contact mobile number of reader


    /**
     * Email of each Reader.
     */
    private String email; // email of reader

    /**
     * Default constructor required by Ebeans
     */
    public Reader() {
        super();
    }


    /**
     * Constructor to create Reader objects
     * @param readerId
     * @param name
     * @param mobile
     * @param email
     */
    public Reader(String readerId, String name, String mobile, String email) {
        this();
        this.readerId = readerId;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
    }


    /**
     * Getter for reader Id
     * @return
     */
    public String getReaderId() {
        return readerId;
    }


    /**
     * Setter for reader Id
     * @param readerId
     */
    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }


    /**
     * Getter for reader name
     * @return
     */
    public String getName() {
        return name;
    }


    /**
     * Setter for reader name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Getter for mobile number
     * @return
     */
    public String getMobile() {
        return mobile;
    }


    /**
     * Setter for mobile number
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    /**
     * Getter for email
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * Method which checks if two Reader objects are equal.
     * Reader Id is used to identify if two Reader instances refer to the same Reader.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(super.equals(obj)){
            return true;
        }

        Reader reader = (Reader) obj;
        return readerId.equals(reader.getReaderId());
    }


}
