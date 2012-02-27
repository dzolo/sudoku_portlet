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
    @NamedQuery(name = "Game.findAll", query = "SELECT g FROM Game g"),
    @NamedQuery(name = "Game.findById", query = "SELECT g FROM Game g WHERE g.id = :id"),
    @NamedQuery(name = "Game.findByInitDate", query = "SELECT g FROM Game g WHERE g.initDate = :initDate"),
    @NamedQuery(name = "Game.findByType", query = "SELECT g FROM Game g WHERE g.type = :type")
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
