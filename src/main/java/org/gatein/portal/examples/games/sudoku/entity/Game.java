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
 * The Game entity contains initialization values, such as a time stamp of
 * a game creation and initialization field values that contain fixed values of
 * a Sudoku puzzle.
 * Game types specify how a game was created. There are two types: generated and
 * service. A game with the generated type was algorithmically generated within
 * this application with a difficulty, specified by a difficulty attribute.
 * On the other hand a game with the service type was obtained from a periodically
 * remote service. If the game is of the service type, it will contain a reference
 * to a service entity.
 *
 * @see GameType
 * @see GameDifficulty
 * @see Service
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
        query = "SELECT AVG(gs.rating), AVG(gs.lasting),"
              + "       SUM(gs.lasting), COUNT(gs.id) "
              + "FROM GameSolution gs "
              + "WHERE gs.finished > 0 AND gs.userId IS NOT NULL"
    ),
    @NamedQuery(
        name = "Game.findProposals",
        query = "SELECT g FROM Game g "
              + "WHERE g.id IN ("
              + "   SELECT gs.gameId.id FROM GameSolution gs"
              + "   WHERE gs.finished > 0 AND gs.gameId.id NOT IN ("
              + "       SELECT gs1.gameId.id FROM GameSolution gs1"
              + "       WHERE gs1.userId LIKE :uid AND gs1.finished > 0"
              + "   )"
              + "   GROUP BY gs.gameId.id, gs.rating"
              + "   ORDER BY COUNT(gs.gameId.id) DESC, AVG(gs.rating) DESC"
              + ")"
    ),
    @NamedQuery(
        name = "Game.findStatisticsOfGame",
        query = "SELECT AVG(gs.rating), AVG(gs.lasting), COUNT(gs.id) "
              + "FROM GameSolution gs "
              + "WHERE gs.gameId.id = :gid AND gs.finished > 0"
    ),
    @NamedQuery(
        name = "Game.findBestSolvers",
        query = "SELECT gs.userId, gs.userName, COUNT(gs.id),"
              + "       SUM(gs.lasting), AVG(gs.rating)"
              + "FROM GameSolution gs "
              + "WHERE gs.finished > 0 "
              + "GROUP BY gs.userId, gs.userName "
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
    @Column(name = "init_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date initTime;
    
    @Basic(optional = false)
    @Lob
    @Column(name = "init_values", nullable = false)
    private String initValues;
    
    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    @Column(name = "type", nullable = false)
    private GameType type;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type_dificulty")
    private GameDifficulty typeDifficulty;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gameId")
    private Collection<GameSolution> gameSolutionsCollection;
    
    @JoinColumn(name = "type_service_id", referencedColumnName = "id")
    @ManyToOne
    private Service typeServiceId;

    /**
     * An empty constructor of the entity
     */
    public Game()
    {
    }

    /**
     * A contructor of thes entity
     * 
     * @param id        An identificator of the entity
     * @param initTime  A time of the creation of the game
     * @param initValues Init values of the game (fixed values)
     * @param type      A type of the game
     */
    public Game(Integer id, Date initTime, String initValues, GameType type)
    {
        this.id = id;
        this.initTime = initTime;
        this.initValues = initValues;
        this.type = type;
    }

    /**
     * Gets the identificator 
     * 
     * @return          Identificator
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * Sets the idetificator
     * 
     * @param id        New identificator
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * Gets the datetime of the creation of the game 
     * 
     * @return          Date object
     */
    public Date getInitTime()
    {
        return initTime;
    }

    /**
     * Sets the datetime of the creation of the game
     * 
     * @param initTime  Date object
     */
    public void setInitTime(Date initTime)
    {
        this.initTime = initTime;
    }

    /**
     * Gets init values of the game (fixed values)
     * 
     * @return          Init values as a string, separated by commas.
     *                  Empty values are replaced by an empty string.  
     */
    public String getInitValues()
    {
        return initValues;
    }

    /**
     * Sets init values of the game (fixed values)
     * 
     * @param initValues Init values as a string, separated by commas.
     *                  Empty values are replaced by an empty string. 
     */
    public void setInitValues(String initValues)
    {
        if (initValues == null || !initValues.matches("^([1-9]?,){80}[1-9]?$"))
        {
            throw new IllegalArgumentException("Wrong init values: " + initValues);
        }
        
        this.initValues = initValues;
    }

    /**
     * Gets the type of the game
     * 
     * @return          Game type
     */
    public GameType getType()
    {
        return type;
    }

    /**
     * Sets the type of the game
     * 
     * @param type      New game type
     */
    public void setType(GameType type)
    {
        this.type = type;
    }

    /**
     * Gets the difficulty of the game (only for <code>GENERATED</code> games) 
     * 
     * @return          Game difficulty
     */
    public GameDifficulty getTypeDifficulty()
    {
        return typeDifficulty;
    }

    /**
     * Sets the difficulty of the game (only for <code>GENERATED</code> games)
     * 
     * @param typeDificulty New game difficulty
     */
    public void setTypeDifficulty(GameDifficulty typeDificulty)
    {
        this.typeDifficulty = typeDificulty;
    }

    /**
     * Gets a collenction of game solutions of the game
     * 
     * @return              Collection of game solutions
     */
    @XmlTransient
    public Collection<GameSolution> getGameSolutionsCollection()
    {
        return gameSolutionsCollection;
    }

    /**
     * Sets a collenction of game solutions of the game
     * 
     * @param gameSolutionsCollection New collection of game solutions
     */
    public void setGameSolutionsCollection(Collection<GameSolution> gameSolutionsCollection)
    {
        this.gameSolutionsCollection = gameSolutionsCollection;
    }

    /**
     * Gets the service from which the game was obtained (only for
     * <code>REMOTE</code> games) 
     * 
     * @return          Service entity        
     */
    public Service getTypeServiceId()
    {
        return typeServiceId;
    }

    /**
     * Sets the service from which the game was obtained (only for
     * <code>REMOTE</code> games) 
     * 
     * @param typeServiceId Service entity        
     */
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
        
        if ((this.id == null && other.getId() != null) ||
            (this.id != null && !this.id.equals(other.getId())))
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
