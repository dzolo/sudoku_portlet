/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : SavedGame.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * LastPlayedGameSolution Entity Class
 *
 * @author Ondřej Fibich
 */
@Entity
@Table(name = "last_played_game_solutions")
@XmlRootElement
public class LastPlayedGameSolution implements Serializable
{

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "user_id")
    private String userId;
    
    @JoinColumn(name = "game_solution_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private GameSolution gameSolutionId;

    public LastPlayedGameSolution()
    {
    }

    public LastPlayedGameSolution(String userId)
    {
        this.userId = userId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public GameSolution getGameSolutionId()
    {
        return gameSolutionId;
    }

    public void setGameSolutionId(GameSolution gameSolutionId)
    {
        this.gameSolutionId = gameSolutionId;
    }

    @Override
    public int hashCode()
    {
        return userId != null ? userId.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof LastPlayedGameSolution))
        {
            return false;
        }
        
        LastPlayedGameSolution other = (LastPlayedGameSolution) object;
        
        if ((this.userId == null && other.userId != null) ||
            (this.userId != null && !this.userId.equals(other.userId)))
        {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString()
    {
        return "org.gatein.portal.examples.games.entities.LastPlayedGameSolution[ id=" + userId + " ]";
    }
}
