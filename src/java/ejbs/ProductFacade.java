/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import entities.Product;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entities.Product_;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.PurchaseOrder;
import entities.Manufacturer;
import entities.ProductCode;
import java.util.List;

/**
 *
 * @author mauro
 */
@Stateless
public class ProductFacade extends AbstractFacade<Product> {

    @PersistenceContext(unitName = "mytest_tomee1.7.2_no-cdiPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductFacade() {
        super(Product.class);
    }

    public boolean isPurchaseOrderListEmpty(Product entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Product> product = cq.from(Product.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(product, entity), cb.isNotEmpty(product.get(Product_.purchaseOrderList)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public List<PurchaseOrder> findPurchaseOrderList(Product entity) {
        return this.getMergedEntity(entity).getPurchaseOrderList();
    }

    public boolean isManufacturerIdEmpty(Product entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Product> product = cq.from(Product.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(product, entity), cb.isNotNull(product.get(Product_.manufacturerId)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public Manufacturer findManufacturerId(Product entity) {
        return this.getMergedEntity(entity).getManufacturerId();
    }

    public boolean isProductCodeEmpty(Product entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Product> product = cq.from(Product.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(product, entity), cb.isNotNull(product.get(Product_.productCode)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public ProductCode findProductCode(Product entity) {
        return this.getMergedEntity(entity).getProductCode();
    }

    @Override
    public Product findWithParents(Product entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> product = cq.from(Product.class);
        product.fetch(Product_.manufacturerId);
        product.fetch(Product_.productCode);
        cq.select(product).where(cb.equal(product, entity));
        return em.createQuery(cq).getSingleResult();
    }
    
}
