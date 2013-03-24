package edu.wctc.distjava.purpleproject.domain;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author jlombardo
 */
public class PopularItemDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer itemId;
    private Long bidCount;
    private String title;
    private Date startDate;
    private Date endDate;
    private String seller;

    public PopularItemDto() {
    }

    public PopularItemDto(Integer itemId, Long bidCount, String title, Date startDate, Date endDate, String seller) {
        this.itemId = itemId;
        this.bidCount = bidCount;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.seller = seller;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Long getBidCount() {
        return bidCount;
    }

    public void setBidCount(Long bidCount) {
        this.bidCount = bidCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.itemId != null ? this.itemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PopularItemDto other = (PopularItemDto) obj;
        if (this.itemId != other.itemId && (this.itemId == null || !this.itemId.equals(other.itemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PopularItemDto{" + "itemId=" + itemId + ", bidCount=" 
                + bidCount + ", title=" + title + ", startDate=" 
                + startDate + ", endDate=" + endDate + '}';
    }
    
    
}
