/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : GameSolutions.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * GameSolutions Entity Class
 *
 * @author Ondřej Fibich
 */
@Entity
@Table(name = "game_solutions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GameSolutions.findAll",query = "SELECT g FROM GameSolutions g"),
    @NamedQuery(name = "GameSolutions.findById", query = "SELECT g FROM GameSolutions g WHERE g.id = :id"),
    @NamedQuery(name = "GameSolutions.findByUserId", query = "SELECT g FROM GameSolutions g WHERE g.userId = :userId"),
    @NamedQuery(name = "GameSolutions.findByUserName", query = "SELECT g FROM GameSolutions g WHERE g.userName = :userName"),
    @NamedQuery(name = "GameSolutions.findByTimeStart", query = "SELECT g FROM GameSolutions g WHERE g.timeStart = :timeStart"),
    @NamedQuery(name = "GameSolutions.findByLasting", query = "SELECT g FROM GameSolutions g WHERE g.lasting = :lasting"),
    @NamedQuery(name = "GameSolutions.findByFinished", query = "SELECT g FROM GameSolutions g WHERE g.finished = :finished")
})
public class GameSolutions implements Serializable
{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "user_id")
    private String userId;
    
    @Basic(optional = false)
    @Column(name = "user_name")
    private String userName;
    
    @Basic(optional = false)
    @Lob
    @Column(name = "values")
    private String values;
    
    @Basic(optional = false)
    @Column(name = "time_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStart;
    
    @Basic(optional = false)
    @Column(name = "lasting")
    private int lasting;
    
    @Basic(optional = false)
    @Column(name = "finished")
    private boolean finished;
    
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Games gameId;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gameSolutionId")
    private Collection<SavedGames> savedGamesCollection;

    public GameSolutions()
    {
    }

    public GameSolutions(Integer id)
    {
        this.id = id;
    }

    public GameSolutions(Integer id, String userId, String userName,
            String values, Date timeStart, int lasting, boolean finished)
    {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.values = values;
        this.timeStart = timeStart;
        this.lasting = lasting;
        this.finished = finished;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getValues()
    {
        return values;
    }

    public void setValues(String values)
    {
        this.values = values;
    }

    public Date getTimeStart()
    {
        return timeStart;
    }

    public void setTimeStart(Date timeStart)
    {
        this.timeStart = timeStart;
    }

    public int getLasting()
    {
        return lasting;
    }

    public void setLasting(int lasting)
    {
        this.lasting = lasting;
    }

    public boolean getFinished()
    {
        return finished;
    }

    public void setFinished(boolean finished)
    {
        this.finished = finished;
    }

    public Games getGameId()
    {
        return gameId;
    }

    public void setGameId(Games gameId)
    {
        this.gameId = gameId;
    }

    @XmlTransient
    public Collection<SavedGames> getSavedGamesCollection()
    {
        return savedGamesCollection;
    }

    public void setSavedGamesCollection(Collection<SavedGames> savedGamesCollection)
    {
        this.savedGamesCollection = savedGamesCollection;
    }

    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof GameSolutions)) {
            return false;
        }
        
        GameSolutions other = (GameSolutions) object;
        
        if ((this.id == null && other.id != null) ||
            (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString()
    {
        return "org.gatein.portal.examples.games.entities.GameSolutions[ id=" + id + " ]";
    }
}
