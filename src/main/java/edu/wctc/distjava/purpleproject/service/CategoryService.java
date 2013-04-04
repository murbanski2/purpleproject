package edu.wctc.distjava.purpleproject.service;

import edu.wctc.distjava.purpleproject.domain.Category;
import edu.wctc.distjava.purpleproject.repository.CategoryRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import edu.umd.cs.findbugs.annotations.SuppressWarnings;

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
@Repository("catService")
@Transactional(readOnly=true)
public class CategoryService implements ICategoryService {
    private static final long serialVersionUID = 1L;
    
    /*
     * Note that @SuppressWarnings is only used by a source code analyzer
     * that I use caled "FindBugs". You don't need this unless you do to.
     */
    @SuppressWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
    private transient final Logger LOG = LoggerFactory.getLogger(CategoryService.class);
    
    @Autowired
    private CategoryRepository catRepo;
    
    public CategoryService() {
    }
    
    public List<Category> findAll() {
        return catRepo.findAllSorted();
    }
    
    public List<Category> findByCategoryName(String name) {
        return catRepo.findByCategory(name);
    }
    
    @Modifying   
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public Category saveAndFlush(Category entity) {
        return catRepo.saveAndFlush(entity);
    }
    @Modifying   
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public Category save(Category entity) {
        return catRepo.save(entity);
    }

    @Override
    public CategoryRepository getCatRepo() {
        return catRepo;
    }

    @Override
    public void setUserRepo(CategoryRepository catRepo) {
        this.catRepo = catRepo;
    }
    
    
}
