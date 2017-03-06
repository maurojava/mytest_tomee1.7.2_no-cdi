package controllers;

import entities.Customer;
import ejbs.CustomerFacade;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.annotation.PostConstruct;

@ManagedBean(name = "customerController")
@ViewScoped
public class CustomerController extends AbstractController<Customer> {

    @EJB
    private CustomerFacade ejbFacade;
    @ManagedProperty(value = "#{discountCodeController}")
    private DiscountCodeController discountCodeController;
    @ManagedProperty(value = "#{microMarketController}")
    private MicroMarketController zipController;

    // Flags to indicate if child collections are empty
    private boolean isPurchaseOrderListEmpty;

    /* Setter method for managed property discountCodeController */
    public void setDiscountCodeController(DiscountCodeController discountCodeController) {
        this.discountCodeController = discountCodeController;
    }

    /* Setter method for managed property zipController */
    public void setZipController(MicroMarketController zipController) {
        this.zipController = zipController;
    }

    /**
     * Initialize the concrete Customer controller bean. The AbstractController
     * requires the EJB Facade object for most operations.
     */
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
    }

    public CustomerController() {
        // Inform the Abstract parent controller of the concrete Customer Entity
        super(Customer.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        discountCodeController.setSelected(null);
        zipController.setSelected(null);
    }

    /**
     * Set the "is[ChildCollection]Empty" property for OneToMany fields.
     */
    @Override
    protected void setChildrenEmptyFlags() {
        this.setIsPurchaseOrderListEmpty();
    }

    public boolean getIsPurchaseOrderListEmpty() {
        return this.isPurchaseOrderListEmpty;
    }

    private void setIsPurchaseOrderListEmpty() {
        Customer selected = this.getSelected();
        if (selected != null) {
            this.isPurchaseOrderListEmpty = ejbFacade.isPurchaseOrderListEmpty(selected);
        } else {
            this.isPurchaseOrderListEmpty = true;
        }
    }

    /**
     * Sets the "items" attribute with a collection of PurchaseOrder entities
     * that are retrieved from Customer?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for PurchaseOrder page
     */
    public String navigatePurchaseOrderList() {
        Customer selected = this.getSelected();
        if (selected != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("PurchaseOrder_items", ejbFacade.findPurchaseOrderList(selected));
        }
        return "/app/purchaseOrder/index";
    }

    /**
     * Sets the "selected" attribute of the DiscountCode controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDiscountCode(ActionEvent event) {
        Customer selected = this.getSelected();
        if (selected != null && discountCodeController.getSelected() == null) {
            discountCodeController.setSelected(selected.getDiscountCode());
        }
    }

    /**
     * Sets the "selected" attribute of the MicroMarket controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareZip(ActionEvent event) {
        Customer selected = this.getSelected();
        if (selected != null && zipController.getSelected() == null) {
            zipController.setSelected(selected.getZip());
        }
    }

}
