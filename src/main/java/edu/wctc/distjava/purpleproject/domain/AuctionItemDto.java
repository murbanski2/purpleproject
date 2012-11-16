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
    private String image1;
    private Date endDate;
    private String highBid;
    private String bidCount;

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
        return formatter.print(period) + " left";
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

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
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

}
