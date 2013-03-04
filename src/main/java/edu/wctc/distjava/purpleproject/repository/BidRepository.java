package edu.wctc.distjava.purpleproject.repository;

import edu.wctc.distjava.purpleproject.domain.Bid;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
/**
 * This is a Spring Data JPA interface contract that serves the proxy
 * mechanism of the Spring-Data-JPA API to automatically generate data access
 * code. It inherits basic CRUD functionality and may be customized at will.
 * When adding custom methods to write to the database, add the '@Modifying'
 * annotation.
 * 
 * @author      Jim Lombardo
 * @version     1.01
 * @see         <a href='http://static.springsource.org/spring-data/data-jpa/docs/current/reference/html/#jpa.query-methods'>Spring-Data-Jpa Reference</a>
 */
public interface BidRepository extends JpaRepository<Bid, Integer> {
    
    @Query("select MAX(b.amount) from Bid b where b.itemId = ?1")
    BigDecimal findHighestBidAmtForItem(Integer id);
    
    @Query("select Count(*) from Bid b where b.itemId = ?1")
    Number findBidCountForItem(Integer id);
}
