/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import entities.ProductCode;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entities.ProductCode_;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Product;
import java.util.List;

/**
 *
 * @author mauro
 */
@Stateless
public class ProductCodeFacade extends AbstractFacade<ProductCode> {

    @PersistenceContext(unitName = "mytest_tomee1.7.2_no-cdiPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductCodeFacade() {
        super(ProductCode.class);
    }

    public boolean isProductListEmpty(ProductCode entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<ProductCode> productCode = cq.from(ProductCode.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(productCode, entity), cb.isNotEmpty(productCode.get(ProductCode_.productList)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public List<Product> findProductList(ProductCode entity) {
        return this.getMergedEntity(entity).getProductList();
    }
    
}
