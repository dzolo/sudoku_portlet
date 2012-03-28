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
 * Service Entity Class
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

    public Service()
    {
    }

    public Service(Integer id)
    {
        this.id = id;
    }

    public Service(Integer id, String name, String url, int checkTime)
    {
        this.id = id;
        this.name = name;
        this.url = url;
        this.checkTime = checkTime;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public int getCheckTime()
    {
        return checkTime;
    }

    public void setCheckTime(int checkTime)
    {
        this.checkTime = checkTime;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    @XmlTransient
    public Collection<Game> getGamesCollection()
    {
        return gamesCollection;
    }

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
