/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : SavedGames.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * SavedGames Entity Class
 *
 * @author Ondřej Fibich
 */
@Entity
@Table(name = "saved_games")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SavedGames.findAll", query = "SELECT s FROM SavedGames s"),
    @NamedQuery(name = "SavedGames.findById", query = "SELECT s FROM SavedGames s WHERE s.id = :id"),
    @NamedQuery(name = "SavedGames.findByLasting", query = "SELECT s FROM SavedGames s WHERE s.lasting = :lasting")
})
public class SavedGames implements Serializable
{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "lasting")
    private int lasting;
    
    @Basic(optional = false)
    @Lob
    @Column(name = "values")
    private String values;
    
    @JoinColumn(name = "game_solution_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GameSolutions gameSolutionId;

    public SavedGames()
    {
    }

    public SavedGames(Integer id)
    {
        this.id = id;
    }

    public SavedGames(Integer id, int lasting, String values)
    {
        this.id = id;
        this.lasting = lasting;
        this.values = values;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public int getLasting()
    {
        return lasting;
    }

    public void setLasting(int lasting)
    {
        this.lasting = lasting;
    }

    public String getValues()
    {
        return values;
    }

    public void setValues(String values)
    {
        this.values = values;
    }

    public GameSolutions getGameSolutionId()
    {
        return gameSolutionId;
    }

    public void setGameSolutionId(GameSolutions gameSolutionId)
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
        if (!(object instanceof SavedGames)) {
            return false;
        }
        
        SavedGames other = (SavedGames) object;
        
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
