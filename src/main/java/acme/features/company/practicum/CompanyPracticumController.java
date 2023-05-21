
package acme.features.company.practicum;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.practicum.Practicum;
import acme.framework.controllers.AbstractController;
import acme.roles.Company;

@Controller
public class CompanyPracticumController extends AbstractController<Company, Practicum> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private CompanyPracticumShowService		showService;
	@Autowired
	private CompanyPracticumCreateService	createService;
	@Autowired
	private CompanyPracticumUpdateService	updateService;
	@Autowired
	private CompanyPracticumDeleteService	deleteService;
	@Autowired
	private CompanyPracticumListAllService	listAllService;
	@Autowired
	private CompanyPracticumListMineService	listMineService;
	@Autowired
	private CompanyPracticumPublishService	publishService;


	// Constructors -----------------------------------------------------------
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("list-all", "list", this.listAllService);
		super.addCustomCommand("list-mine", "list", this.listMineService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
