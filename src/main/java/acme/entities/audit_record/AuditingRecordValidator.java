
package acme.entities.audit_record;

import java.time.Duration;
import java.util.Date;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import acme.framework.helpers.MomentHelper;

public class AuditingRecordValidator implements Validator {

	@Override
	public boolean supports(final Class<?> clazz) {
		return AuditingRecord.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		final AuditingRecord auditingRecord = (AuditingRecord) target;
		final Date start = auditingRecord.getStartAudit();
		final Date end = auditingRecord.getEndAudit();
		final Duration duration = MomentHelper.computeDuration(start, end);
		if (MomentHelper.isBefore(start, end))
			errors.rejectValue("startAudit", "startAudit.afterEndAudit", "The Audit's start date is after Audit's end date");
		if (duration.toMinutes() < 30)
			errors.rejectValue("endAudit", "endAudit.notEnoughTime", "The Audit finish in less than 30 minuts");
	}

}
