package edu.wctc.distjava.purpleproject.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jlombardo
 */
@Entity
@Table(name = "auction_item")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AuctionItem.findAll", query = "SELECT a FROM AuctionItem a"),
    @NamedQuery(name = "AuctionItem.findByItemId", query = "SELECT a FROM AuctionItem a WHERE a.itemId = :itemId"),
    @NamedQuery(name = "AuctionItem.findByTitle", query = "SELECT a FROM AuctionItem a WHERE a.title = :title"),
    @NamedQuery(name = "AuctionItem.findByImage1", query = "SELECT a FROM AuctionItem a WHERE a.image1 = :image1"),
    @NamedQuery(name = "AuctionItem.findByImage2", query = "SELECT a FROM AuctionItem a WHERE a.image2 = :image2"),
    @NamedQuery(name = "AuctionItem.findByImage3", query = "SELECT a FROM AuctionItem a WHERE a.image3 = :image3"),
    @NamedQuery(name = "AuctionItem.findByImage4", query = "SELECT a FROM AuctionItem a WHERE a.image4 = :image4"),
    @NamedQuery(name = "AuctionItem.findByImage5", query = "SELECT a FROM AuctionItem a WHERE a.image5 = :image5"),
    @NamedQuery(name = "AuctionItem.findByStartDate", query = "SELECT a FROM AuctionItem a WHERE a.startDate = :startDate"),
    @NamedQuery(name = "AuctionItem.findByEndDate", query = "SELECT a FROM AuctionItem a WHERE a.endDate = :endDate"),
    @NamedQuery(name = "AuctionItem.findByWinBidderId", query = "SELECT a FROM AuctionItem a WHERE a.winBidderId = :winBidderId"),
    @NamedQuery(name = "AuctionItem.findByPayment", query = "SELECT a FROM AuctionItem a WHERE a.payment = :payment"),
    @NamedQuery(name = "AuctionItem.findByPayDate", query = "SELECT a FROM AuctionItem a WHERE a.payDate = :payDate"),
    @NamedQuery(name = "AuctionItem.findByComments", query = "SELECT a FROM AuctionItem a WHERE a.comments = :comments")})
public class AuctionItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "item_id")
    private Integer itemId;
    @Size(max = 80)
    @Column(name = "title")
    private String title;
    @Lob
    @Size(max = 16777215)
    @Column(name = "description")
    private String description;
    @Size(max = 80)
    @Column(name = "image1")
    private String image1;
    @Size(max = 80)
    @Column(name = "image2")
    private String image2;
    @Size(max = 80)
    @Column(name = "image3")
    private String image3;
    @Size(max = 80)
    @Column(name = "image4")
    private String image4;
    @Size(max = 80)
    @Column(name = "image5")
    private String image5;
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Size(max = 50)
    @Column(name = "win_bidder_id")
    private String winBidderId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "payment")
    private BigDecimal payment;
    @Column(name = "pay_date")
    @Temporal(TemporalType.DATE)
    private Date payDate;
    @Size(max = 255)
    @Column(name = "comments")
    private String comments;
    @JoinColumn(name = "seller_id", referencedColumnName = "username")
    @ManyToOne
    private User sellerId;
    @JoinColumn(name = "cat_id", referencedColumnName = "cat_id")
    @ManyToOne
    private Category catId;
    @OneToMany(mappedBy = "itemId")
    private Collection<Bid> bidCollection;

    public AuctionItem() {
    }

    public AuctionItem(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getWinBidderId() {
        return winBidderId;
    }

    public void setWinBidderId(String winBidderId) {
        this.winBidderId = winBidderId;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public User getSellerId() {
        return sellerId;
    }

    public void setSellerId(User sellerId) {
        this.sellerId = sellerId;
    }

    public Category getCatId() {
        return catId;
    }

    public void setCatId(Category catId) {
        this.catId = catId;
    }

    @XmlTransient
    public Collection<Bid> getBidCollection() {
        return bidCollection;
    }

    public void setBidCollection(Collection<Bid> bidCollection) {
        this.bidCollection = bidCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itemId != null ? itemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuctionItem)) {
            return false;
        }
        AuctionItem other = (AuctionItem) object;
        if ((this.itemId == null && other.itemId != null) || (this.itemId != null && !this.itemId.equals(other.itemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.exceptions.AuctionItem[ itemId=" + itemId + " ]";
    }

}
