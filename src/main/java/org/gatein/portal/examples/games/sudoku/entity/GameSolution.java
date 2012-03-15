/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : GameSolution.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * GameSolution Entity Class
 *
 * @author Ondřej Fibich
 */
@Entity
@Table(name = "game_solutions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(
        name = "GameSolution.findAll",
        query = "SELECT g FROM GameSolution g"
    ),
    @NamedQuery(
        name = "GameSolution.findUnfinishedOfUser",
        query = "SELECT g FROM GameSolution g "
              + "WHERE g.userId = :uid AND g.finished = 0"
    ),
    @NamedQuery(
        name = "GameSolution.count",
        query = "SELECT COUNT(g) FROM GameSolution g"
    ),
    @NamedQuery(
        name = "GameSolution.countFinished",
        query = "SELECT COUNT(g) FROM GameSolution g "
              + "WHERE g.finished > 0"
    ),
    @NamedQuery(
        name = "GameSolution.countSolver",
        query = "SELECT COUNT(g2) FROM GameSolution g2 "
              + "WHERE g2.id IN ("
              + "   SELECT g.id FROM GameSolution g "
              + "   WHERE g.finished > 0 "
              + "   GROUP BY g.userId)"
    )
})
public class GameSolution implements Serializable
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
    
    @Column(name = "user_name")
    private String userName;
    
    @Basic(optional = false)
    @Lob
    @Column(name = "values_")
    private String values;
    
    @Basic(optional = false)
    @Column(name = "time_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStart;
    
    @Basic(optional = false)
    @Column(name = "lasting")
    private int lasting;
    
    @Column(name = "finished")
    private boolean finished;
    
    @Column(name = "rating")
    private Integer rating;
    
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Game gameId;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gameSolutionId")
    private Collection<SavedGame> savedGamesCollection;

    public GameSolution()
    {
    }

    public GameSolution(Integer id)
    {
        this.id = id;
    }

    public GameSolution(Integer id, String userId, String userName,
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

    public Game getGameId()
    {
        return gameId;
    }

    public void setGameId(Game gameId)
    {
        this.gameId = gameId;
    }

    public Integer getRating()
    {
        return rating;
    }

    public void setRating(Integer rating)
    {
        this.rating = rating;
    }

    @XmlTransient
    public Collection<SavedGame> getSavedGamesCollection()
    {
        return savedGamesCollection;
    }

    public void setSavedGamesCollection(Collection<SavedGame> savedGamesCollection)
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
        if (!(object instanceof GameSolution))
        {
            return false;
        }
        
        GameSolution other = (GameSolution) object;
        
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
