package controllers;

import entities.Product;
import ejbs.ProductFacade;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.annotation.PostConstruct;

@ManagedBean(name = "productController")
@ViewScoped
public class ProductController extends AbstractController<Product> {

    @EJB
    private ProductFacade ejbFacade;
    @ManagedProperty(value = "#{manufacturerController}")
    private ManufacturerController manufacturerIdController;
    @ManagedProperty(value = "#{productCodeController}")
    private ProductCodeController productCodeController;

    // Flags to indicate if child collections are empty
    private boolean isPurchaseOrderListEmpty;

    /* Setter method for managed property manufacturerIdController */
    public void setManufacturerIdController(ManufacturerController manufacturerIdController) {
        this.manufacturerIdController = manufacturerIdController;
    }

    /* Setter method for managed property productCodeController */
    public void setProductCodeController(ProductCodeController productCodeController) {
        this.productCodeController = productCodeController;
    }

    /**
     * Initialize the concrete Product controller bean. The AbstractController
     * requires the EJB Facade object for most operations.
     */
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
    }

    public ProductController() {
        // Inform the Abstract parent controller of the concrete Product Entity
        super(Product.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        manufacturerIdController.setSelected(null);
        productCodeController.setSelected(null);
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
        Product selected = this.getSelected();
        if (selected != null) {
            this.isPurchaseOrderListEmpty = ejbFacade.isPurchaseOrderListEmpty(selected);
        } else {
            this.isPurchaseOrderListEmpty = true;
        }
    }

    /**
     * Sets the "items" attribute with a collection of PurchaseOrder entities
     * that are retrieved from Product?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for PurchaseOrder page
     */
    public String navigatePurchaseOrderList() {
        Product selected = this.getSelected();
        if (selected != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("PurchaseOrder_items", ejbFacade.findPurchaseOrderList(selected));
        }
        return "/app/purchaseOrder/index";
    }

    /**
     * Sets the "selected" attribute of the Manufacturer controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareManufacturerId(ActionEvent event) {
        Product selected = this.getSelected();
        if (selected != null && manufacturerIdController.getSelected() == null) {
            manufacturerIdController.setSelected(selected.getManufacturerId());
        }
    }

    /**
     * Sets the "selected" attribute of the ProductCode controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareProductCode(ActionEvent event) {
        Product selected = this.getSelected();
        if (selected != null && productCodeController.getSelected() == null) {
            productCodeController.setSelected(selected.getProductCode());
        }
    }

}
