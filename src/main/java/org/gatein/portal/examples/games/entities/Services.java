/* 
 * Project       : Bachelor Thesis - Sudoku game implementation as portlet
 * Document      : Services.java
 * Author        : Ondřej Fibich <xfibic01@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package org.gatein.portal.examples.games.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Services Entity Class
 *
 * @author Ondřej Fibich
 */
@Entity
@Table(name = "services")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Services.findAll", query = "SELECT s FROM Services s"),
    @NamedQuery(name = "Services.findById", query = "SELECT s FROM Services s WHERE s.id = :id"),
    @NamedQuery(name = "Services.findByName", query = "SELECT s FROM Services s WHERE s.name = :name"),
    @NamedQuery(name = "Services.findByUrl", query = "SELECT s FROM Services s WHERE s.url = :url"),
    @NamedQuery(name = "Services.findByCheckTime", query = "SELECT s FROM Services s WHERE s.checkTime = :checkTime")
})
public class Services implements Serializable
{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    
    @Basic(optional = false)
    @Column(name = "url")
    private String url;
    
    @Basic(optional = false)
    @Column(name = "check_time")
    private int checkTime;
    
    @OneToMany(mappedBy = "typeServiceId")
    private Collection<Games> gamesCollection;

    public Services()
    {
    }

    public Services(Integer id)
    {
        this.id = id;
    }

    public Services(Integer id, String name, String url, int checkTime)
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

    @XmlTransient
    public Collection<Games> getGamesCollection()
    {
        return gamesCollection;
    }

    public void setGamesCollection(Collection<Games> gamesCollection)
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
        if (!(o instanceof Services)) {
            return false;
        }
        
        Services other = (Services) o;
        
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
