/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : Service.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.sudoku.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The service entity encases information about a periodically remote service,
 * such as a name, a URL, an expiration time and an indicator for enabling/disabling.
 * 
 * The expiration time, stored in the check time attribute, contains lasting in
 * seconds after which the service, located at the URL, should be checked for
 * a new game.
 *
 * @author Ondřej Fibich
 */
@Entity
@Table(name = "services")
@XmlRootElement
@NamedQueries({
    @NamedQuery(
        name = "Service.findAll",
        query = "SELECT s FROM Service s"
    )
})
public class Service implements Serializable
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
    @Column(name = "url", nullable = false)
    private String url;
    
    @Basic(optional = false)
    @Column(name = "check_time", nullable = false)
    private int checkTime;
    
    @Column(name = "enabled", nullable = false)
    private boolean enabled;
    
    @OneToMany(mappedBy = "typeServiceId")
    private Collection<Game> gamesCollection;

    /**
     * An empty constructor of the entity
     */
    public Service()
    {
    }

    /**
     * A constructor of the entity
     * 
     * @param id        An indetificator of the entity
     * @param name      A name
     * @param url       A URL
     * @param checkTime A check time
     */
    public Service(Integer id, String name, String url, int checkTime)
    {
        this.id = id;
        this.name = name;
        this.url = url;
        this.checkTime = checkTime;
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
     * Gets the name of the service
     * 
     * @return          Name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the service
     * 
     * @param name      New name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets the URL of the service
     * 
     * @return          URL as a string
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * Sets the URL of the service
     * 
     * @param url       New URL as a string
     */
    public void setUrl(String url)
    {
        this.url = url;
    }

    /**
     * Gets the check time after which the service should be checked for a new game.
     * 
     * @return          Check time in seconds
     */
    public int getCheckTime()
    {
        return checkTime;
    }

    /**
     * Sets the check time after which the service should be checked for a new game.
     * 
     * @param checkTime New check time in seconds
     */
    public void setCheckTime(int checkTime)
    {
        this.checkTime = checkTime;
    }

    /**
     * Gets the state of the service.
     * New games may be obtained only from enabled services.
     * 
     * @return          Is the service enabled?
     */
    public boolean isEnabled()
    {
        return enabled;
    }

    /**
     * Sets the state of the service.
     * New games may be obtained only from enabled services.
     * 
     * @param enabled   enable/disable service
     */
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    /**
     * Gets the collection of game which were obtained from the service
     * 
     * @return          Collection of games 
     */
    @XmlTransient
    public Collection<Game> getGamesCollection()
    {
        return gamesCollection;
    }

    /**
     * Sets the collection of game which were obtained from the service
     * 
     * @param gamesCollection Collection of games 
     */
    public void setGamesCollection(Collection<Game> gamesCollection)
    {
        this.gamesCollection = gamesCollection;
    }

    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Service))
        {
            return false;
        }
        
        Service other = (Service) o;
        
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
        return "org.gatein.portal.examples.games.entities.Services[ id=" + id + " ]";
    }
}
