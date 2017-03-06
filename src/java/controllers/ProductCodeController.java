package controllers;

import entities.ProductCode;
import ejbs.ProductCodeFacade;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.annotation.PostConstruct;

@ManagedBean(name = "productCodeController")
@ViewScoped
public class ProductCodeController extends AbstractController<ProductCode> {

    @EJB
    private ProductCodeFacade ejbFacade;

    // Flags to indicate if child collections are empty
    private boolean isProductListEmpty;

    /**
     * Initialize the concrete ProductCode controller bean. The
     * AbstractController requires the EJB Facade object for most operations.
     */
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
    }

    public ProductCodeController() {
        // Inform the Abstract parent controller of the concrete ProductCode Entity
        super(ProductCode.class);
    }

    /**
     * Set the "is[ChildCollection]Empty" property for OneToMany fields.
     */
    @Override
    protected void setChildrenEmptyFlags() {
        this.setIsProductListEmpty();
    }

    public boolean getIsProductListEmpty() {
        return this.isProductListEmpty;
    }

    private void setIsProductListEmpty() {
        ProductCode selected = this.getSelected();
        if (selected != null) {
            this.isProductListEmpty = ejbFacade.isProductListEmpty(selected);
        } else {
            this.isProductListEmpty = true;
        }
    }

    /**
     * Sets the "items" attribute with a collection of Product entities that are
     * retrieved from ProductCode?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Product page
     */
    public String navigateProductList() {
        ProductCode selected = this.getSelected();
        if (selected != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Product_items", ejbFacade.findProductList(selected));
        }
        return "/app/product/index";
    }

}
