/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import entities.PurchaseOrder;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entities.PurchaseOrder_;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Customer;
import entities.Product;

/**
 *
 * @author mauro
 */
@Stateless
public class PurchaseOrderFacade extends AbstractFacade<PurchaseOrder> {

    @PersistenceContext(unitName = "mytest_tomee1.7.2_no-cdiPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PurchaseOrderFacade() {
        super(PurchaseOrder.class);
    }

    public boolean isCustomerIdEmpty(PurchaseOrder entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<PurchaseOrder> purchaseOrder = cq.from(PurchaseOrder.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(purchaseOrder, entity), cb.isNotNull(purchaseOrder.get(PurchaseOrder_.customerId)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public Customer findCustomerId(PurchaseOrder entity) {
        return this.getMergedEntity(entity).getCustomerId();
    }

    public boolean isProductIdEmpty(PurchaseOrder entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<PurchaseOrder> purchaseOrder = cq.from(PurchaseOrder.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(purchaseOrder, entity), cb.isNotNull(purchaseOrder.get(PurchaseOrder_.productId)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public Product findProductId(PurchaseOrder entity) {
        return this.getMergedEntity(entity).getProductId();
    }

    @Override
    public PurchaseOrder findWithParents(PurchaseOrder entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PurchaseOrder> cq = cb.createQuery(PurchaseOrder.class);
        Root<PurchaseOrder> purchaseOrder = cq.from(PurchaseOrder.class);
        purchaseOrder.fetch(PurchaseOrder_.customerId);
        purchaseOrder.fetch(PurchaseOrder_.productId);
        cq.select(purchaseOrder).where(cb.equal(purchaseOrder, entity));
        return em.createQuery(cq).getSingleResult();
    }
    
}
