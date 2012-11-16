package edu.wctc.distjava.purpleproject.service;

import edu.wctc.distjava.purpleproject.domain.AuctionItem;
import edu.wctc.distjava.purpleproject.domain.Category;
import edu.wctc.distjava.purpleproject.repository.AuctionItemRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jlombardo
 */
public interface IAuctionItemService {

    BigDecimal findHighestBidForItem(Integer itemId);
    
    Number findBidCountForItem(Integer itemId);
    
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
