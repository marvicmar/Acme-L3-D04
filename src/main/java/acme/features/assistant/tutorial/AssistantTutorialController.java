
package acme.features.assistant.tutorial;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.tutorial.Tutorial;
import acme.framework.controllers.AbstractController;
import acme.roles.Assistant;

@Controller
public class AssistantTutorialController extends AbstractController<Assistant, Tutorial> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AssistantTutorialShowService		showService;
	@Autowired
	private AssistantTutorialCreateService		createService;
	@Autowired
	private AssistantTutorialUpdateService		updateService;
	@Autowired
	private AssistantTutorialDeleteService		deleteService;
	@Autowired
	private AssistantTutorialListAllService		listAllService;
	@Autowired
	private AssistantTutorialListMineService	listMineService;
	@Autowired
	private AssistantTutorialPublishService		publishService;


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
