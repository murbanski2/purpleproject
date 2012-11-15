package edu.wctc.distjava.purpleproject.service;

import edu.wctc.distjava.purpleproject.domain.AuctionItem;
import edu.wctc.distjava.purpleproject.repository.AuctionItemRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jlombardo
 */
public interface IAuctionItemService {

    List<AuctionItem> findAll();
    
    AuctionItemRepository getItemRepo();

    @Modifying
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    AuctionItem save(AuctionItem entity);

    void setItemRepo(AuctionItemRepository itemRepo);

}
