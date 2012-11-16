package edu.wctc.distjava.purpleproject.service;

import edu.wctc.distjava.purpleproject.domain.AuctionItem;
import edu.wctc.distjava.purpleproject.repository.AuctionItemRepository;
import edu.wctc.distjava.purpleproject.repository.BidRepository;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is a Spring-managed, transactional service class that 
 * decouples the DAOs from the view layer. By default all methods are
 * read only transactions for optimal performance. This behavior may be 
 * overridden on a method by method basis by using the following annotation:
 * '@Transactional(readOnlly=false)'
 * 
 * @author      Jim Lombardo
 * @version     1.01
 */
@Repository("auctionItemService")
@Transactional(readOnly=true)
public class AuctionItemService implements IAuctionItemService {
    private static final long serialVersionUID = 1L;
    private final Logger LOG = LoggerFactory.getLogger(AuctionItemService.class);
    
    @Autowired
    private AuctionItemRepository itemRepo;
    @Autowired
    private BidRepository bidRepo;    
    
    public AuctionItemService() {
    }
    
    public BigDecimal findHighestBidForItem(Integer itemId) {
        return bidRepo.findHighestBidForItem(itemId);
    }
    
    public Number findBidCountForItem(Integer itemId) {
        return bidRepo.findBidCountForItem(itemId);
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

    
    
}
