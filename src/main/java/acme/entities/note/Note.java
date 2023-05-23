
package acme.entities.note;

import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Entity
public class Note extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotNull
	@Temporal(TemporalType.DATE)
	protected Date				moment;

	@NotBlank
	@Length(max = 75)
	protected String			title;

	@NotBlank
	@Length(max = 75)
	protected String			author;

	@NotBlank
	@Length(max = 100)
	protected String			message;

	@Email
	@Length(max = 255)
	protected String			emailAddress;

	@URL
	@Length(max = 255)
	protected String			link;

	// Derived attributes -----------------------------------------------------

	//The author must be computed as follows: “〈username〉 - 〈surname, name〉”

	// Relationships ----------------------------------------------------------
}
