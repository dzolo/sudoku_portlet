/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : SavedGameSolution.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The game solution can be saved during the solving, using a saved game
 * solution entity.
 * 
 * The saved game solution entity contains an image of the current state of
 * solution, identified by a name.
 *
 * @author Ondřej Fibich
 */
@Entity
@Table(name = "saved_game_solutions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(
        name = "SavedGameSolution.findAll",
        query = "SELECT s FROM SavedGameSolution s"
    ),
    @NamedQuery(
        name = "SavedGameSolution.findByUser",
        query = "SELECT s FROM SavedGameSolution s "
              + "WHERE s.gameSolutionId.userId = :uid AND s.gameSolutionId.finished = 0"
    )
})
public class SavedGameSolution implements Serializable
{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "name", nullable = false)
    private String name;
    
    @Basic(optional = false)
    @Column(name = "saved", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date saved;
    
    @Basic(optional = false)
    @Column(name = "lasting", nullable = false)
    private int lasting;
    
    @Basic(optional = false)
    @Lob
    @Column(name = "values_", nullable = false)
    private String values;
    
    @JoinColumn(name = "game_solution_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private GameSolution gameSolutionId;

    /**
     * An empty constructor of the entity
     */
    public SavedGameSolution()
    {
    }

    /**
     * A constructor of the entity
     * 
     * @param id        An identificator of the entity
     * @param lasting   A lasting of the game solution
     * @param values    Values of the game solution
     */
    public SavedGameSolution(Integer id, int lasting, String values)
    {
        this.id = id;
        this.lasting = lasting;
        this.values = values;
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
     * Gets the name of the saved game solution
     * 
     * @return          Name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the nam of the saved game solution
     * 
     * @param name      New name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets the datetime of saving of the saved game solution
     * 
     * @return          Date object
     */
    public Date getSaved()
    {
        return saved;
    }

    /**
     * Sets the datetime of saving of the saved game solution
     * 
     * @param saved     Date object
     */
    public void setSaved(Date saved)
    {
        this.saved = saved;
    }

    /**
     * Gets the lasting of the saved game solution
     * 
     * @return          Lasting in seconds
     */
    public int getLasting()
    {
        return lasting;
    }

    /**
     * Sets the lasting of the game solution
     * 
     * @param lasting   Lasting in seconds
     */
    public void setLasting(int lasting)
    {
        this.lasting = lasting;
    }

    /**
     * Gets values of the saved game solution.
     * 
     * @return          Values as a string, separated by commas.
     *                  Empty values are replaced by an empty string.
     */
    public String getValues()
    {
        return values;
    }

    /**
     * Sets values of the game solution.
     * 
     * @param values    Values as a string, separated by commas.
     *                  Empty values are replaced by an empty string.
     */
    public void setValues(String values)
    {
        if (values == null || !values.matches("^([1-9]?,){80}[1-9]?$"))
        {
            throw new IllegalArgumentException("Wrong values: " + values);
        }
        
        this.values = values;
    }

    /**
     * Gets the parent game solution of the saved game solution
     * 
     * @return          GameSoltion object
     */
    public GameSolution getGameSolutionId()
    {
        return gameSolutionId;
    }

    /**
     * Sets the parent game solution of the saved game solution
     * 
     * @param gameSolutionId New GameSolution object
     */
    public void setGameSolutionId(GameSolution gameSolutionId)
    {
        this.gameSolutionId = gameSolutionId;
    }

    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof SavedGameSolution))
        {
            return false;
        }
        
        SavedGameSolution other = (SavedGameSolution) object;
        
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
        return "org.gatein.portal.examples.games.entities.SavedGames[ id=" + id + " ]";
    }
}
