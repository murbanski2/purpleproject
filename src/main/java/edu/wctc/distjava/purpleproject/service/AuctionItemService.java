package edu.wctc.distjava.purpleproject.service;

import edu.wctc.distjava.purpleproject.domain.AuctionItem;
import edu.wctc.distjava.purpleproject.domain.Bid;
import edu.wctc.distjava.purpleproject.domain.MemberSearch;
import edu.wctc.distjava.purpleproject.repository.AuctionItemRepository;
import edu.wctc.distjava.purpleproject.repository.BidRepository;
import edu.wctc.distjava.purpleproject.repository.MemberSearchRepository;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is a Spring-managed, transactional service class that decouples
 * the DAOs from the view layer. By default all methods are read only
 * transactions for optimal performance. This behavior may be overridden on a
 * method by method basis by using the following annotation:
 * '
 *
 * @Transactional(readOnlly=false)'
 *
 * @author Jim Lombardo
 * @version 1.01
 */
@Repository("auctionItemService")
@Transactional(readOnly = true)
public class AuctionItemService implements IAuctionItemService {
    private static final long serialVersionUID = 1L;
    private final Logger LOG = LoggerFactory.getLogger(AuctionItemService.class);
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
        if(userId == null || userId.isEmpty()) return; // only save for members
        List<MemberSearch> recs = 
                memSearchRepo.findByUserIdAndPhrase(userId, searchPhrase);
        if(recs.isEmpty()) {
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
     * Finds auction items by category and search phrase where the end date
     * of the auction item + 7 days > the current date. This makes sure that
     * the auction item displays for one week after the auction has ended. 
     * <P>
     * IMPORTANT: because JPA does not support date arithmetic, this method 
     * uses a native sql query deesigned exclusively for MySql RDMS.
     * 
     * @param category
     * @param phrase
     * @param recCount - limits the returned record count
     * @return 
     */
    @Override
    public List<AuctionItem> findByCategoryAndSearchPhrase(String category, String phrase, int recCount) {
        String[] words = phrase.trim().split(" ");
        String sql = "SELECT ai.* FROM auction_item ai, category c WHERE "
                + "(ai.cat_id = c.cat_id) AND (c.category = ?1) AND (";
        String sql2 = "";
        for (String s : words) {
            sql2 += "ai.title LIKE '%" + s + "%' OR ";
        }
        sql2 = sql2.substring(0, sql2.length() - 4);
        sql += sql2 + ") AND (ai.end_date + interval 7 day) > now() "
                + "order by ai.start_date DESC";
        Query query = em.createNativeQuery(sql,
                edu.wctc.distjava.purpleproject.domain.AuctionItem.class);
        query.setParameter(1, category);
        query.setMaxResults(recCount);
        return query.getResultList();
    }

    /**
     * Finds auction items by category where the end date
     * of the auction item + 7 days > the current date. This makes sure that
     * the auction item displays for one week after the auction has ended. 
     * <P>
     * IMPORTANT: because JPA does not support date arithmetic, this method 
     * uses a native sql query deesigned exclusively for MySql RDMS.
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
    public BigDecimal findHighestBidForItem(Integer itemId) {
        return bidRepo.findHighestBidForItem(itemId);
    }

    @Override
    public Number findBidCountForItem(Integer itemId) {
        return bidRepo.findBidCountForItem(itemId);
    }

    /**
     * Finds auction items by search phrase where the end date
     * of the auction item + 7 days > the current date. This makes sure that
     * the auction item displays for one week after the auction has ended. 
     * <P>
     * IMPORTANT: because JPA does not support date arithmetic, this method 
     * uses a native sql query deesigned exclusively for MySql RDMS.
     * 
     * @param phrase
     * @param recCount - limits the returned record count
     * @return 
     */
    @Override
    public List<AuctionItem> findBySearchPhrase(String phrase, int recCount) {
        String[] words = phrase.trim().split(" ");
        String sql = "SELECT * FROM auction_item ai WHERE (";
        String sql2 = "";
        for (String s : words) {
            sql2 += "ai.title LIKE '%" + s + "%' OR ";
        }
        sql2 = sql2.substring(0, sql2.length() - 4);
        sql += sql2;
        sql += ") AND (ai.end_date + interval 7 day) > now() "
                + "order by ai.start_date DESC";
        Query query = em.createNativeQuery(sql,
                edu.wctc.distjava.purpleproject.domain.AuctionItem.class);
        query.setMaxResults(recCount);
        return query.getResultList();
    }

    /**
     * Finds all auction items (limited by record count) where the end date
     * of the auction item + 7 days > the current date. This makes sure that
     * the auction item displays for one week after the auction has ended. 
     * <P>
     * IMPORTANT: because JPA does not support date arithmetic, this method 
     * uses a native sql query deesigned exclusively for MySql RDMS.
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
