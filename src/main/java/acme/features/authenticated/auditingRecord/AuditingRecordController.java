
package acme.features.authenticated.auditingRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.audit_record.AuditingRecord;
import acme.framework.components.accounts.Authenticated;
import acme.framework.controllers.AbstractController;

@Controller
public class AuditingRecordController extends AbstractController<Authenticated, AuditingRecord> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AuditingRecordShowService	showAuditingRecordService;
	@Autowired
	protected AuditingRecordListService	listAuditingRecordService;


	// Constructors -----------------------------------------------------------
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showAuditingRecordService);
		super.addBasicCommand("list", this.listAuditingRecordService);

	}

}
