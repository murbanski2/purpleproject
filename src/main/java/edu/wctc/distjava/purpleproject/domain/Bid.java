package edu.wctc.distjava.purpleproject.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jlombardo
 */
@Entity
@Table(name = "bid")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bid.findAll", query = "SELECT b FROM Bid b"),
    @NamedQuery(name = "Bid.findByBidId", query = "SELECT b FROM Bid b WHERE b.bidId = :bidId"),
    @NamedQuery(name = "Bid.findByAmount", query = "SELECT b FROM Bid b WHERE b.amount = :amount")})
public class Bid implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "bid_id")
    private Integer bidId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "amount")
    private BigDecimal amount;
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    @ManyToOne
    private AuctionItem itemId;
    @JoinColumn(name = "bidder_id", referencedColumnName = "username")
    @ManyToOne
    private User bidderId;

    public Bid() {
    }

    public Bid(Integer bidId) {
        this.bidId = bidId;
    }

    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public AuctionItem getItemId() {
        return itemId;
    }

    public void setItemId(AuctionItem itemId) {
        this.itemId = itemId;
    }

    public User getBidderId() {
        return bidderId;
    }

    public void setBidderId(User bidderId) {
        this.bidderId = bidderId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bidId != null ? bidId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bid)) {
            return false;
        }
        Bid other = (Bid) object;
        if ((this.bidId == null && other.bidId != null) || (this.bidId != null && !this.bidId.equals(other.bidId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.exceptions.Bid[ bidId=" + bidId + " ]";
    }

}
