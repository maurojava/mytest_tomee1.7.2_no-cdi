package controllers;

import entities.DiscountCode;
import ejbs.DiscountCodeFacade;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.annotation.PostConstruct;

@ManagedBean(name = "discountCodeController")
@ViewScoped
public class DiscountCodeController extends AbstractController<DiscountCode> {

    @EJB
    private DiscountCodeFacade ejbFacade;

    // Flags to indicate if child collections are empty
    private boolean isCustomerListEmpty;

    /**
     * Initialize the concrete DiscountCode controller bean. The
     * AbstractController requires the EJB Facade object for most operations.
     */
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
    }

    public DiscountCodeController() {
        // Inform the Abstract parent controller of the concrete DiscountCode Entity
        super(DiscountCode.class);
    }

    /**
     * Set the "is[ChildCollection]Empty" property for OneToMany fields.
     */
    @Override
    protected void setChildrenEmptyFlags() {
        this.setIsCustomerListEmpty();
    }

    public boolean getIsCustomerListEmpty() {
        return this.isCustomerListEmpty;
    }

    private void setIsCustomerListEmpty() {
        DiscountCode selected = this.getSelected();
        if (selected != null) {
            this.isCustomerListEmpty = ejbFacade.isCustomerListEmpty(selected);
        } else {
            this.isCustomerListEmpty = true;
        }
    }

    /**
     * Sets the "items" attribute with a collection of Customer entities that
     * are retrieved from DiscountCode?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Customer page
     */
    public String navigateCustomerList() {
        DiscountCode selected = this.getSelected();
        if (selected != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Customer_items", ejbFacade.findCustomerList(selected));
        }
        return "/app/customer/index";
    }

}
