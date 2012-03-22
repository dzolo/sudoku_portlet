/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : Game.java
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
import org.gatein.portal.examples.games.sudoku.entity.datatype.GameDifficulty;
import org.gatein.portal.examples.games.sudoku.entity.datatype.GameType;

/**
 * Game Entity Class
 *
 * @author Ondřej Fibich
 */
@Entity
@Table(name = "games")
@XmlRootElement
@NamedQueries({
    @NamedQuery(
        name = "Game.findAll",
        query = "SELECT g FROM Game g"
    ),
    @NamedQuery(
        name = "Game.findAllByTypeAndNotInUser",
        query = "SELECT g FROM Game g "
              + "WHERE g.type = :type AND g.id NOT IN ("
              + "      SELECT gs.gameId.id FROM GameSolution gs "
              + "      WHERE gs.userId LIKE :uid "
              + ") "
              + "GROUP BY g.id "
              + "ORDER BY g.id DESC"
    ),
    @NamedQuery(
        name = "Game.findLastGameOfService",
        query = "SELECT g FROM Game g "
              + "WHERE g.type = :type AND g.typeServiceId = :service "
              + "ORDER BY g.id DESC"
    ),
    @NamedQuery(
        name = "Game.findStatisticsLoggedPlayers",
        query = "SELECT AVG(gs.rating), AVG(gs.lasting), SUM(gs.lasting), COUNT(gs.id) "
              + "FROM GameSolution gs "
              + "WHERE gs.finished > 0 AND gs.userId IS NOT NULL"
    ),
    @NamedQuery(
        name = "Game.findStatisticsNotLoggedPlayers",
        query = "SELECT AVG(gs.rating), AVG(gs.lasting), SUM(gs.lasting), COUNT(gs.id) "
              + "FROM GameSolution gs "
              + "WHERE gs.finished > 0 AND gs.userId IS NULL"
    ),
    @NamedQuery(
        name = "Game.findProposals",
        query = "SELECT gs.gameId FROM GameSolution gs "
              + "WHERE gs.userId NOT LIKE :uid AND gs.finished > 0 "
              + "GROUP BY gs.gameId.id "
              + "ORDER BY COUNT(gs.gameId.id) DESC, AVG(gs.rating) DESC"
    ),
    @NamedQuery(
        name = "Game.findStatisticsOfGame",
        query = "SELECT AVG(gs.rating), AVG(gs.lasting), COUNT(gs.id) "
              + "FROM GameSolution gs "
              + "WHERE gs.gameId.id = :gid AND gs.finished > 0"
    ),
    @NamedQuery(
        name = "Game.findBestSolvers",
        query = "SELECT gs.userId, gs.userName, COUNT(gs.id), SUM(gs.lasting), AVG(gs.rating)"
              + "FROM GameSolution gs "
              + "WHERE gs.finished > 0 "
              + "GROUP BY gs.userId "
              + "HAVING gs.userId IS NOT NULL "
              + "ORDER BY COUNT(gs.id) DESC, SUM(gs.lasting) ASC"
    ),
    @NamedQuery(
        name = "Game.findBestSolvedSolutions",
        query = "SELECT gs FROM GameSolution gs "
              + "WHERE gs.finished > 0 AND gs.gameId.id = :gid "
              + "ORDER BY gs.lasting"
    ),
    @NamedQuery(
        name = "Game.countOfSolutionsOfGame",
        query = "SELECT COUNT(gs) from GameSolution gs "
              + "WHERE gs.gameId.id = :gid"
    )
})
public class Game implements Serializable
{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "init_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date initDate;
    
    @Basic(optional = false)
    @Lob
    @Column(name = "init_values")
    private String initValues;
    
    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    @Column(name = "type")
    private GameType type;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type_dificulty")
    private GameDifficulty typeDifficulty;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gameId")
    private Collection<GameSolution> gameSolutionsCollection;
    
    @JoinColumn(name = "type_service_id", referencedColumnName = "id")
    @ManyToOne
    private Service typeServiceId;

    public Game()
    {
    }

    public Game(Integer id)
    {
        this.id = id;
    }

    public Game(Integer id, Date initDate, String initValues, GameType type)
    {
        this.id = id;
        this.initDate = initDate;
        this.initValues = initValues;
        this.type = type;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Date getInitDate()
    {
        return initDate;
    }

    public void setInitDate(Date initDate)
    {
        this.initDate = initDate;
    }

    public String getInitValues()
    {
        return initValues;
    }

    public void setInitValues(String initValues)
    {
        this.initValues = initValues;
    }

    public GameType getType()
    {
        return type;
    }

    public void setType(GameType type)
    {
        this.type = type;
    }

    public GameDifficulty getTypeDifficulty()
    {
        return typeDifficulty;
    }

    public void setTypeDifficulty(GameDifficulty typeDificulty)
    {
        this.typeDifficulty = typeDificulty;
    }

    @XmlTransient
    public Collection<GameSolution> getGameSolutionsCollection()
    {
        return gameSolutionsCollection;
    }

    public void setGameSolutionsCollection(Collection<GameSolution> gameSolutionsCollection)
    {
        this.gameSolutionsCollection = gameSolutionsCollection;
    }

    public Service getTypeServiceId()
    {
        return typeServiceId;
    }

    public void setTypeServiceId(Service typeServiceId)
    {
        this.typeServiceId = typeServiceId;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof Game))
        {
            return false;
        }
        
        Game other = (Game) object;
        
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
        return "org.gatein.portal.examples.games.entities.Games[ id=" + id + " ]";
    }
}
