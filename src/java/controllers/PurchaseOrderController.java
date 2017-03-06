package controllers;

import entities.PurchaseOrder;
import ejbs.PurchaseOrderFacade;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.annotation.PostConstruct;

@ManagedBean(name = "purchaseOrderController")
@ViewScoped
public class PurchaseOrderController extends AbstractController<PurchaseOrder> {

    @EJB
    private PurchaseOrderFacade ejbFacade;
    @ManagedProperty(value = "#{customerController}")
    private CustomerController customerIdController;
    @ManagedProperty(value = "#{productController}")
    private ProductController productIdController;

    /* Setter method for managed property customerIdController */
    public void setCustomerIdController(CustomerController customerIdController) {
        this.customerIdController = customerIdController;
    }

    /* Setter method for managed property productIdController */
    public void setProductIdController(ProductController productIdController) {
        this.productIdController = productIdController;
    }

    /**
     * Initialize the concrete PurchaseOrder controller bean. The
     * AbstractController requires the EJB Facade object for most operations.
     */
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
    }

    public PurchaseOrderController() {
        // Inform the Abstract parent controller of the concrete PurchaseOrder Entity
        super(PurchaseOrder.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        customerIdController.setSelected(null);
        productIdController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Customer controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareCustomerId(ActionEvent event) {
        PurchaseOrder selected = this.getSelected();
        if (selected != null && customerIdController.getSelected() == null) {
            customerIdController.setSelected(selected.getCustomerId());
        }
    }

    /**
     * Sets the "selected" attribute of the Product controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareProductId(ActionEvent event) {
        PurchaseOrder selected = this.getSelected();
        if (selected != null && productIdController.getSelected() == null) {
            productIdController.setSelected(selected.getProductId());
        }
    }

}
