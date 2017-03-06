package controllers;

import entities.MicroMarket;
import ejbs.MicroMarketFacade;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.annotation.PostConstruct;

@ManagedBean(name = "microMarketController")
@ViewScoped
public class MicroMarketController extends AbstractController<MicroMarket> {

    @EJB
    private MicroMarketFacade ejbFacade;

    // Flags to indicate if child collections are empty
    private boolean isCustomerListEmpty;

    /**
     * Initialize the concrete MicroMarket controller bean. The
     * AbstractController requires the EJB Facade object for most operations.
     */
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
    }

    public MicroMarketController() {
        // Inform the Abstract parent controller of the concrete MicroMarket Entity
        super(MicroMarket.class);
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
        MicroMarket selected = this.getSelected();
        if (selected != null) {
            this.isCustomerListEmpty = ejbFacade.isCustomerListEmpty(selected);
        } else {
            this.isCustomerListEmpty = true;
        }
    }

    /**
     * Sets the "items" attribute with a collection of Customer entities that
     * are retrieved from MicroMarket?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Customer page
     */
    public String navigateCustomerList() {
        MicroMarket selected = this.getSelected();
        if (selected != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Customer_items", ejbFacade.findCustomerList(selected));
        }
        return "/app/customer/index";
    }

}
