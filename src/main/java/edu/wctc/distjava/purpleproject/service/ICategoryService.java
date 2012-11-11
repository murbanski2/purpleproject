package edu.wctc.distjava.purpleproject.service;

import edu.wctc.distjava.purpleproject.domain.Category;
import edu.wctc.distjava.purpleproject.repository.CategoryRepository;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jlombardo
 */
public interface ICategoryService extends Serializable {

    CategoryRepository getCatRepo();

    List<Category> findAll();
    
    @Modifying
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    Category save(Category entity);

    @Modifying
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    Category saveAndFlush(Category entity);

    void setUserRepo(CategoryRepository catRepo);

}
