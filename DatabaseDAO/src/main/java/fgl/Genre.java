package fgl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;

@Entity
@Table( name = "Genres" )
public class Genre {
    @Id
    @GeneratedValue
    @Column( name = "ID" )
    private int id;

    @Column( name = "Name" )
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }
}
