
package acme.entities.audit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.entities.courses.Course;
import acme.framework.data.AbstractEntity;
import acme.roles.Auditor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
public class Audit extends AbstractEntity {
	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotNull
	@ManyToOne(optional = false)
	protected Course			course;

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{1,3}\\d{3}$")
	protected String			code;

	@NotBlank
	@Length(max = 100)
	protected String			conclusion;

	@NotBlank
	@Length(max = 100)
	protected String			strongPoints;

	@NotBlank
	@Length(max = 100)
	protected String			weakPoints;

	protected boolean			draftMode;

	// Relationships ----------------------------------------------------------
	@ManyToOne(optional = false)
	protected Auditor			auditor;
}
