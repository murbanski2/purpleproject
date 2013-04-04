package edu.wctc.distjava.purpleproject.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 *
 * @author jlombardo
 */
@Entity
@Table(name = "member_search")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MemberSearch.findAll", query = "SELECT ms FROM MemberSearch ms"),
    @NamedQuery(name = "MemberSearch.findByUserId", query = "SELECT ms FROM MemberSearch ms WHERE ms.userId = :userId")})
public class MemberSearch implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "search_id")
    private Integer searchId;
    @Size(max = 50)
    @Column(name = "user_id")
    private String userId;
    @Size(max = 180)
    @Column(name = "search_phrase")
    private String searchPhrase;
    @Size(max = 50)
    @Column(name = "category")
    private String category;
    
    public MemberSearch() {
    }

    public MemberSearch(Integer searchId) {
        this.searchId = searchId;
    }

    public Integer getSearchId() {
        return searchId;
    }

    public void setSearchId(Integer searchId) {
        this.searchId = searchId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSearchPhrase() {
        return searchPhrase;
    }

    public void setSearchPhrase(String searchPhrase) {
        this.searchPhrase = searchPhrase;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (this.searchId != null ? this.searchId.hashCode() : 0);
        return hash;
    }

    @SuppressWarnings
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MemberSearch other = (MemberSearch) obj;
        if (this.searchId != other.searchId && (this.searchId == null || !this.searchId.equals(other.searchId))) {
            return false;
        }
        return true;
    }

    
}
