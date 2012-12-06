package edu.wctc.distjava.purpleproject.repository;

import edu.wctc.distjava.purpleproject.domain.MemberSearch;
import java.util.List;
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
public interface MemberSearchRepository extends JpaRepository<MemberSearch, Integer> {
    
    @Query("select ms from MemberSearch ms where ms.userId = ?1")
    List<MemberSearch> findByUserId(String userId);
    
    @Query("select ms from MemberSearch ms where ms.userId = ?1 and ms.searchPhrase = ?2")
    List<MemberSearch> findByUserIdAndPhrase(String userId, String searchPhrase);
}
