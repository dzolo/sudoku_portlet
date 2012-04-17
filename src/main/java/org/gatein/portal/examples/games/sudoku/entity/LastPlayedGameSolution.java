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
 * A last played game solution is an association between a user and one of his/her
 * game solutions which is persisted in the last played game solution entity.
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

    /**
     * An an empty constructor
     */
    public LastPlayedGameSolution()
    {
    }

    /**
     * A constructor
     * 
     * @param userId        An indetificator of a user 
     */
    public LastPlayedGameSolution(String userId)
    {
        this.userId = userId;
    }

    /**
     * Gets the identificator of a user who owns this last played game solution
     * 
     * @return              A string identificator 
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * Sets the identificator of a user who owns this last played game solution
     * 
     * @param userId         A string identificator 
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * Gets the parent game solution of this last played game solution
     * 
     * @return              GameSolution entity
     */
    public GameSolution getGameSolutionId()
    {
        return gameSolutionId;
    }

    /**
     * Sets the parent game solution of this last played game solution
     * 
     * @param gameSolutionId GameSolution entity
     */
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
        
        if ((this.userId == null && other.getUserId() != null) ||
            (this.userId != null && !this.userId.equals(other.getUserId())))
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
