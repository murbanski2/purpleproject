package edu.wctc.distjava.purpleproject.service;

import edu.wctc.distjava.purpleproject.domain.AuctionItem;
import edu.wctc.distjava.purpleproject.domain.Bid;
import edu.wctc.distjava.purpleproject.domain.MemberSearch;
import edu.wctc.distjava.purpleproject.domain.PopularItemDto;
import edu.wctc.distjava.purpleproject.repository.AuctionItemRepository;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jlombardo
 */
public interface IAuctionItemService extends Serializable {
    List<PopularItemDto> findByMostPopular(boolean isActive);
    
    Bid findHighestBidForItem(Integer itemId);
    
    List<AuctionItem> findByEndDatesToday();
    
    List<AuctionItem> findByEndDatesThisWeek();
    
    List<AuctionItem> findByEndDatesThisMonth();
    
    void saveBid(Bid bid);
    
    AuctionItem findById(Integer id);
    
    void updateMembersRecentSearch(String userId, String searchPhrase);

    BigDecimal findHighestBidAmtForItem(Integer itemId);
    
    Number findBidCountForItem(Integer itemId);
    
    List<MemberSearch> findRecentSearchesByUser(String userId);
    
    List<AuctionItem> findByCategory(String category, int recCount);
    
    List<AuctionItem> findByCategoryAndSearchPhrase(String category, String phrase, int recCount);
    
    List<AuctionItem> findBySearchPhrase(String phrase, int recCount);
    
    List<AuctionItem> findAll();
    
    List<AuctionItem> findAllLimited(int recCount);
    
    AuctionItemRepository getItemRepo();

    @Modifying
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    AuctionItem save(AuctionItem entity);

    void setItemRepo(AuctionItemRepository itemRepo);

}
