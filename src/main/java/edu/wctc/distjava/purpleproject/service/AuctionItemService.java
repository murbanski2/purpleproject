package edu.wctc.distjava.purpleproject.service;

import edu.wctc.distjava.purpleproject.domain.AuctionItem;
import edu.wctc.distjava.purpleproject.domain.Bid;
import edu.wctc.distjava.purpleproject.domain.MemberSearch;
import edu.wctc.distjava.purpleproject.domain.PopularItemDto;
import edu.wctc.distjava.purpleproject.repository.AuctionItemRepository;
import edu.wctc.distjava.purpleproject.repository.BidRepository;
import edu.wctc.distjava.purpleproject.repository.MemberSearchRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * This class is a Spring-managed, transactional service class that decouples
 * the DAOs from the view layer. By default all methods are read only
 * transactions for optimal performance. This behavior may be overridden on a
 * method by method basis by using the following annotation: '
 *
 * @Transactional(readOnlly=false)'
 *
 * @author Jim Lombardo
 * @version 1.01
 */
@Named
@Repository
@Transactional(readOnly = true)
public class AuctionItemService implements IAuctionItemService {

    private static final long serialVersionUID = 1L;
    /*
     * Note that @SuppressWarnings is only used by a source code analyzer
     * that I use caled "FindBugs". You don't need this unless you do to.
     */
    @SuppressWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
    private transient final Logger LOG = LoggerFactory.getLogger(AuctionItemService.class);
    private final int MAX_SEARCH_REDO = 10;
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private AuctionItemRepository itemRepo;
    @Autowired
    private BidRepository bidRepo;
    @Autowired
    private MemberSearchRepository memSearchRepo;

    public AuctionItemService() {
    }

    public List<AuctionItem> findItemsToEdit(String phrase, String type) {
        Query q = null;
        List<AuctionItem> items = null;
        Date startDate = null;
        Date endDate = new Date();
        Calendar cal = Calendar.getInstance();

        if (phrase != null && !phrase.isEmpty()) {
            String[] words = phrase.trim().split(" ");
            StringBuffer sql = new StringBuffer("SELECT * FROM auction_item ai WHERE (");
            StringBuffer sql2 = new StringBuffer();
            for (String s : words) {
                sql2.append("ai.title LIKE '%").append(s).append("%' OR ");
            }
            sql2 = new StringBuffer(sql2.substring(0, sql2.length() - 4));
            sql.append(sql2);
            sql.append(") order by ai.start_date DESC");
            Query query = em.createNativeQuery(sql.toString(),
                    edu.wctc.distjava.purpleproject.domain.AuctionItem.class);
            query.setMaxResults(200);
            return query.getResultList();

        } else {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            
            if (type.equals("Last 7 Days")) {
                cal.add(Calendar.DATE, -7);
                
            } else if (type.equals("Last 30 Days")) {
                cal.add(Calendar.DATE, -30);
                
            } else if (type.equals("Last 60 Days")) {
                cal.add(Calendar.DATE, -60);
                
            } else {
                 cal.add(Calendar.DATE, -90);               
            }
            
            startDate = cal.getTime();
            return itemRepo.findWithinDateRange(startDate, endDate);
        }

    }

    @Override
    public List<PopularItemDto> findByMostPopular(boolean forAllTime) {
        Date now = new Date();
        List<PopularItemDto> items = new ArrayList<PopularItemDto>();
        List<Object[]> bidsByItemId = itemRepo.getBidCountByItemId();
        // get total count of records
        int recCount = bidsByItemId.size();
        // now calc top third
        recCount /= 3;

        for (int i = 0; i < recCount; i++) {
            Object[] aItem = bidsByItemId.get(i);
            Integer itemId = (Integer) aItem[0];
            AuctionItem item = itemRepo.findOne(itemId);
            PopularItemDto dto = new PopularItemDto(
                    itemId, (Long) aItem[1],
                    item.getTitle(), item.getStartDate(),
                    item.getEndDate(), item.getSellerId().getUserName());
            if (!forAllTime && now.before(item.getEndDate())) {
                items.add(dto);
            } else if (forAllTime) {
                items.add(dto);
            }
        }

        return items;
    }

    @Override
    public List<AuctionItem> findByEndDatesThisMonth() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        // Using Jodatime library
        DateTime dt = new DateTime();
        DateTime monthsEnd = dt.dayOfMonth().withMaximumValue();
        return itemRepo
                .findWithinDateRange(today.getTime(), monthsEnd.toDate());
    }

    @Override
    public List<AuctionItem> findByEndDatesThisWeek() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        Calendar weeksEnd = Calendar.getInstance();
        weeksEnd.setTimeInMillis(today.getTimeInMillis());
        weeksEnd.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        weeksEnd.set(Calendar.HOUR, 23);
        weeksEnd.set(Calendar.MINUTE, 59);
        weeksEnd.set(Calendar.SECOND, 59);
        return itemRepo
                .findWithinDateRange(today.getTime(), weeksEnd.getTime());
    }

    @Override
    public List<AuctionItem> findByEndDatesToday() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.setTimeInMillis(today.getTimeInMillis());
        tomorrow.add(Calendar.DATE, 1);
        return itemRepo
                .findByEndDatesToday(today.getTime(), tomorrow.getTime());
    }

    @Override
    public AuctionItem findById(Integer id) {
        return itemRepo.findOne(id);
    }

    @Modifying
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public void saveBid(Bid bid) {
        bidRepo.saveAndFlush(bid);
    }

    @Modifying
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public void updateMembersRecentSearch(String userId, String searchPhrase) {
        if (userId == null || userId.isEmpty()) {
            return; // only save for members
        }
        List<MemberSearch> recs =
                memSearchRepo.findByUserIdAndPhrase(userId, searchPhrase);
        if (recs.isEmpty()) {
            MemberSearch ms = new MemberSearch();
            ms.setSearchPhrase(searchPhrase);
            ms.setUserId(userId);
            memSearchRepo.saveAndFlush(ms);
        }
    }

    @Override
    public List<MemberSearch> findRecentSearchesByUser(String userId) {
        String sql = "select ms from MemberSearch ms where ms.userId = ?1 "
                + " order by ms.searchId DESC";
        Query query = em.createQuery(sql);
        query.setParameter(1, userId);
        query.setMaxResults(MAX_SEARCH_REDO);
        return query.getResultList();
    }

    /**
     * TODO: needs testing since revising to StringBuffer Finds auction items by
     * category and search phrase where the end date of the auction item + 7
     * days > the current date. This makes sure that the auction item displays
     * for one week after the auction has ended.
     * <P>
     * IMPORTANT: because JPA does not support date arithmetic, this method uses
     * a native sql query deesigned exclusively for MySql RDMS.
     *
     * @param category
     * @param phrase
     * @param recCount - limits the returned record count
     * @return
     */
    @Override
    public List<AuctionItem> findByCategoryAndSearchPhrase(String category, String phrase, int recCount) {
        String[] words = phrase.trim().split(" ");
        StringBuffer sql = new StringBuffer("SELECT ai.* FROM auction_item ai, category c WHERE ");
        sql.append("(ai.cat_id = c.cat_id) AND (c.category = ?1) AND (");
        StringBuffer sql2 = new StringBuffer("");
        for (String s : words) {
            sql2.append("ai.title LIKE '%");
            sql2.append(s).append("%' OR ");
        }
        sql2 = new StringBuffer(sql2.toString().substring(0, sql2.length() - 4));
        sql.append(sql2.toString());
        sql.append(") AND (ai.end_date + interval 7 day) > now() ");
        sql.append("order by ai.start_date DESC");
        Query query = em.createNativeQuery(sql.toString(),
                edu.wctc.distjava.purpleproject.domain.AuctionItem.class);
        query.setParameter(1, category);
        query.setMaxResults(recCount);
        return query.getResultList();
    }

    /**
     * Finds auction items by category where the end date of the auction item +
     * 7 days > the current date. This makes sure that the auction item displays
     * for one week after the auction has ended.
     * <P>
     * IMPORTANT: because JPA does not support date arithmetic, this method uses
     * a native sql query deesigned exclusively for MySql RDMS.
     *
     * @param category
     * @param recCount - limits the returned record count
     * @return
     */
    @Override
    public List<AuctionItem> findByCategory(String category, int recCount) {
        Query query = em.createNativeQuery(
                "select ai.* from auction_item ai, category c "
                + "where ai.cat_id = c.cat_id AND c.category = ?1 "
                + "AND (ai.end_date + interval 7 day) > now() "
                + "order by ai.start_date DESC",
                edu.wctc.distjava.purpleproject.domain.AuctionItem.class);
        query.setMaxResults(recCount);
        query.setParameter(1, category);
        return query.getResultList();
    }

    @Override
    public BigDecimal findHighestBidAmtForItem(Integer itemId) {
        return bidRepo.findHighestBidAmtForItem(itemId);
    }

    @Override
    public Bid findHighestBidForItem(Integer itemId) {
        Query query = em.createQuery("SELECT b from Bid b WHERE b.itemId = ?1 ORDER BY b.amount DESC");
        query.setParameter(1, itemId);
        query.setMaxResults(1);
        Object objBid = query.getSingleResult();
        return (objBid != null) ? (Bid) objBid : null;
    }

    @Override
    public Number findBidCountForItem(Integer itemId) {
        return bidRepo.findBidCountForItem(itemId);
    }

    /**
     * Finds auction items by search phrase where the end date of the auction
     * item + 7 days > the current date. This makes sure that the auction item
     * displays for one week after the auction has ended.
     * <P>
     * IMPORTANT: because JPA does not support date arithmetic, this method uses
     * a native sql query deesigned exclusively for MySql RDMS.
     *
     * @param phrase
     * @param recCount - limits the returned record count
     * @return
     */
    @Override
    public List<AuctionItem> findBySearchPhrase(String phrase, int recCount) {
        String[] words = phrase.trim().split(" ");
        StringBuffer sql = new StringBuffer("SELECT * FROM auction_item ai WHERE (");
        StringBuffer sql2 = new StringBuffer();
        for (String s : words) {
            sql2.append("ai.title LIKE '%" + s + "%' OR ");
        }
        sql2 = new StringBuffer(sql2.substring(0, sql2.length() - 4));
        sql.append(sql2);
        sql.append(") AND (ai.end_date + interval 7 day) > now() ")
                .append("order by ai.start_date DESC");
        Query query = em.createNativeQuery(sql.toString(),
                edu.wctc.distjava.purpleproject.domain.AuctionItem.class);
        query.setMaxResults(recCount);
        return query.getResultList();
    }

    /**
     * Finds all auction items (limited by record count) where the end date of
     * the auction item + 7 days > the current date. This makes sure that the
     * auction item displays for one week after the auction has ended.
     * <P>
     * IMPORTANT: because JPA does not support date arithmetic, this method uses
     * a native sql query deesigned exclusively for MySql RDMS.
     *
     * @param recCount - limits the returned record count
     * @return
     */
    @Override
    public List<AuctionItem> findAllLimited(int recCount) {
        Query query = em.createNativeQuery("select * from auction_item ai where "
                + "(ai.end_date + interval 7 day) > now() "
                + "order by ai.start_date DESC",
                edu.wctc.distjava.purpleproject.domain.AuctionItem.class);
        query.setMaxResults(recCount);
        return query.getResultList();
    }

    @Override
    public List<AuctionItem> findAll() {
        return itemRepo.findAll();
    }

    @Modifying
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public AuctionItem save(AuctionItem entity) {
        return itemRepo.save(entity);
    }

    @Override
    public AuctionItemRepository getItemRepo() {
        return itemRepo;
    }

    @Override
    public void setItemRepo(AuctionItemRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    public BidRepository getBidRepo() {
        return bidRepo;
    }

    public void setBidRepo(BidRepository bidRepo) {
        this.bidRepo = bidRepo;
    }

    public MemberSearchRepository getMemSearchRepo() {
        return memSearchRepo;
    }

    public void setMemSearchRepo(MemberSearchRepository memSearchRepo) {
        this.memSearchRepo = memSearchRepo;
    }
}
