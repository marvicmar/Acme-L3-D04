
package acme.entities.enrolment;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Length;

import acme.entities.activities.Activity;
import acme.entities.courses.Course;
import acme.framework.data.AbstractEntity;
import acme.roles.Student;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Enrolment extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Column(unique = true)
	@NotBlank
	@Pattern(regexp = "^[A-Z]{1,3}[0-9]{3}$")
	protected String			code;

	@NotBlank
	@Length(max = 75)
	protected String			motivation;

	@NotBlank
	@Length(max = 100)
	protected String			goals;

	@Length(max = 75)
	protected String			cardHolderName;

	@Pattern(regexp = "^\\d{4}$")
	protected String			cardLowerNibble;

	protected boolean			draftMode;

	@Transient
	@CreditCardNumber
	protected String			cardNumber;
	@Transient
	@Pattern(regexp = "^\\d{2}/\\d{2}$")
	protected String			expirationDate;
	@Transient
	@Pattern(regexp = "^\\d{3}$")
	protected String			cvv;


	// Derived attributes -----------------------------------------------------
	@Transient
	private static Double workTime(final List<Activity> activities) {
		/*
		 * Se trata de una propiedad dericada que calucla el perido
		 * de tiempo de una inscripcción mediante sus actividades asociadas.
		 */
		double res = 0.0;
		if (!activities.isEmpty())
			for (final Activity activity : activities) {
				double hours = 0.;
				final Date startDate = activity.getStartDate();
				final Date endDate = activity.getEndDate();
				hours = Math.abs(endDate.getTime() / 3600000. - startDate.getTime() / 3600000.);
				res += hours;
			}
		return res;
	}


	// Relationships -------------------------------------------------------------
	@Valid
	@NotNull
	@ManyToOne
	protected Student	student;

	@Valid
	@NotNull
	@ManyToOne
	protected Course	course;
}
