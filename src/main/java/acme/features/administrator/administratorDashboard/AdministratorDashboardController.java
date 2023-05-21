package acme.features.administrator.administratorDashboard;

import acme.forms.AdministratorDashboard;
import acme.framework.components.accounts.Administrator;
import acme.framework.controllers.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
public class AdministratorDashboardController extends AbstractController<Administrator, AdministratorDashboard> {

    // Internal state ---------------------------------------------------------
    @Autowired
    private AdministratorDashboardShowService showService;

    // Constructors -----------------------------------------------------------
    @PostConstruct
    protected void initialise() {
        super.addBasicCommand("show", this.showService);
    }
}
