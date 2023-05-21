
package acme.features.assistant.session;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.session.Session;
import acme.framework.controllers.AbstractController;
import acme.roles.Assistant;

@Controller
public class AssistantSessionController extends AbstractController<Assistant, Session> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AssistantSessionShowService		showService;
	@Autowired
	private AssistantSessionCreateService	createService;
	@Autowired
	private AssistantSessionUpdateService	updateService;
	@Autowired
	private AssistantSessionDeleteService	deleteService;
	@Autowired
	private AssistantSessionListService		listService;
	@Autowired
	private AssistantSessionPublishService	publishService;
	@Autowired
	private AssistantSessionListAllService	listAllService;


	// Constructors -----------------------------------------------------------
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("list", this.listService);

		super.addCustomCommand("publish", "update", this.publishService);
		super.addCustomCommand("list-all", "list", this.listAllService);

	}

}
