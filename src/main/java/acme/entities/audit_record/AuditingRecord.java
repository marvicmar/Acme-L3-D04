
package acme.entities.audit_record;

import java.time.Duration;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.entities.audit.Audit;
import acme.framework.data.AbstractEntity;
import acme.framework.helpers.MomentHelper;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AuditingRecord extends AbstractEntity {
	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Length(max = 75)
	protected String			subject;

	@NotBlank
	@Length(max = 100)
	protected String			assessment;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Past
	protected Date				start;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Past
	protected Date				end;

	@NotBlank
	@Pattern(regexp = "^A\\+?|B|C|F-?$", message = "A+ / A / B / C / F / F-")
	protected String			mark;

	@URL
	@Length(max = 255)
	protected String			link;

	protected boolean			special				= false; //false by default


	@Override
	public String toString() {
		//I adjust the getMark to get A+ or F- in case AP or FL
		return "AuditingRecord [subject=" + this.subject + ", assessment=" + this.assessment + ", startAudit=" + this.start + ", endAudit=" + this.end + ", mark=" + this.mark.toString() + ", link=" + this.link + "]";
	}

	// Derived attributes -----------------------------------------------------

	@Transient
	public Duration getDuration() {
		return MomentHelper.computeDuration(this.start, this.end);
	}

	// Relationships ----------------------------------------------------------


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Audit audit;
}
