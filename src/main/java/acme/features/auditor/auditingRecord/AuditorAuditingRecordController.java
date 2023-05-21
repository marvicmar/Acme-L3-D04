
package acme.features.auditor.auditingRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.audit_record.AuditingRecord;
import acme.framework.controllers.AbstractController;
import acme.roles.Auditor;

@Controller
public class AuditorAuditingRecordController extends AbstractController<Auditor, AuditingRecord> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AuditorAuditingRecordShowService		showAuditingRecordService;
	@Autowired
	protected AuditorAuditingRecordListService		listAuditingRecordService;
	@Autowired
	protected AuditorAuditingRecordCreateService	createAuditingRecordService;
	@Autowired
	protected AuditorAuditingRecordDeleteService	deleteAuditingRecordService;
	@Autowired
	protected AuditorAuditingRecordUpdateService	updateAuditingRecordService;


	// Constructors -----------------------------------------------------------
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createAuditingRecordService);
		super.addBasicCommand("show", this.showAuditingRecordService);
		super.addBasicCommand("list", this.listAuditingRecordService);
		super.addBasicCommand("delete", this.deleteAuditingRecordService);
		super.addBasicCommand("update", this.updateAuditingRecordService);

	}

}
