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

/**
 *
 * @author jlombardo
 */
@Entity
@Table(name = "log_action")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LogAction.findAll", query = "SELECT l FROM LogAction l"),
    @NamedQuery(name = "LogAction.findByLogId", query = "SELECT l FROM LogAction l WHERE l.logId = :logId"),
    @NamedQuery(name = "LogAction.findByRecId", query = "SELECT l FROM LogAction l WHERE l.recId = :recId"),
    @NamedQuery(name = "LogAction.findByAction", query = "SELECT l FROM LogAction l WHERE l.action = :action")})
public class LogAction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "log_id")
    private Integer logId;
    @Size(max = 32)
    @Column(name = "rec_id")
    private String recId;
    @Size(max = 256)
    @Column(name = "action")
    private String action;

    public LogAction() {
    }

    public LogAction(Integer logId) {
        this.logId = logId;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (logId != null ? logId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // Caution - this method won't work in the case the id fields are not set
        if (!(object instanceof LogAction)) {
            return false;
        }
        LogAction other = (LogAction) object;
        if ((this.logId == null && other.logId != null) || (this.logId != null && !this.logId.equals(other.logId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.LogAction[ logId=" + logId + " ]";
    }

}
