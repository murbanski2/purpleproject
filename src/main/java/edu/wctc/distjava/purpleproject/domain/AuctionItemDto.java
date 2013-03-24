package edu.wctc.distjava.purpleproject.domain;

import java.io.Serializable;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * This is a Data Transfer Object (DTO) used to represent data from
 * AuctionItem (entiy class) in a more usable way. The data must be
 * transformed before being populated.
 * 
 * @author jlombardo
 */
public class AuctionItemDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer itemId;
    private String title;
    private String description;
    private String thumbnail;
    private Date endDate;
    private String highBid;
    private double minBid;
    private double placedBid;
    private String bidCount;
    private String image1Url;
    private String image2Url;
    private String image3Url;
    private String image4Url;
    private String image5Url;
    /**
     * Use JodaTime to calculate remaining time before auction ends
     * 
     * @return time to go formatted as 23d 2h, or 2h 10m, or 10m, or Ended
     */
    public String getTimeRemaining() {
        DateTime today = DateTime.now();
        DateTime autctionEnd = new DateTime(endDate);

        Period period = new Period(today, autctionEnd, PeriodType.dayTime());

        PeriodFormatterBuilder bldr = new PeriodFormatterBuilder();

        // Display format varies by length of time to go
        if(endDate.before(today.toDate())) {
            return "Ended";
        } else if(period.getDays() > 1) {
            bldr.appendDays().appendSuffix("d ")
                .appendHours().appendSuffix("h ");
        } else if(period.getHours() <= 24 && period.getHours() > 1) {
            bldr.appendHours().appendSuffix("h ")
                 .appendMinutes().appendSuffix("m");
        } else {
            bldr.appendMinutes().appendSuffix("m");        }
                
        PeriodFormatter formatter = bldr.toFormatter();
        return formatter.print(period);
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
        final AuctionItemDto other = (AuctionItemDto) obj;
        if (this.itemId != other.itemId && (this.itemId == null || !this.itemId.equals(other.itemId))) {
            return false;
        }
        return true;
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

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getHighBid() {
        return highBid;
    }

    public void setHighBid(String highBid) {
        this.highBid = highBid;
    }

    public String getBidCount() {
        return bidCount;
    }

    public void setBidCount(String bidCount) {
        this.bidCount = bidCount;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMinBid() {
        return minBid;
    }

    public void setMinBid(double minBid) {
        this.minBid = minBid;
    }

    public double getPlacedBid() {
        return placedBid;
    }

    public void setPlacedBid(double placedBid) {
        this.placedBid = placedBid;
    }

    public String getImage1Url() {
        return image1Url;
    }

    public void setImage1Url(String image1Url) {
        this.image1Url = image1Url;
    }

    public String getImage2Url() {
        return image2Url;
    }

    public void setImage2Url(String image2Url) {
        this.image2Url = image2Url;
    }

    public String getImage3Url() {
        return image3Url;
    }

    public void setImage3Url(String image3Url) {
        this.image3Url = image3Url;
    }

    public String getImage4Url() {
        return image4Url;
    }

    public void setImage4Url(String image4Url) {
        this.image4Url = image4Url;
    }

    public String getImage5Url() {
        return image5Url;
    }

    public void setImage5Url(String image5Url) {
        this.image5Url = image5Url;
    }

    @Override
    public String toString() {
        return "AuctionItemDto{" + "itemId=" + itemId + ", title=" + title 
                + ", endDate=" + endDate + ", highBid=" + highBid 
                + ", minBid=" + minBid + ", placedBid=" + placedBid 
                + ", bidCount=" + bidCount + '}';
    }

}
