package fgl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.TemporalType;

import java.util.Date;

@Entity
@Table( name = "Opinions" )
public class Opinion {
    @Id
    @GeneratedValue
    @Column( name = "ID" )
    private int id;

    @ManyToOne
    @JoinColumn( name = "UserID", nullable = false )
    private User user;

    @ManyToOne
    @JoinColumn( name = "GameID", nullable = false )
    private Game game;

    @Temporal( TemporalType.DATE )
    private Date date;
}
