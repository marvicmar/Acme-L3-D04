
package acme.entities.banner;

import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

@Entity
@Getter
@Setter
public class Banner extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Temporal(TemporalType.TIMESTAMP)
	@PastOrPresent
	private Date				instant;

	@Temporal(TemporalType.TIMESTAMP)
	private Date				displayStart;

	@Temporal(TemporalType.TIMESTAMP)
	private Date				displayEnd;

	@URL
	@Length(max = 255)
	private String				picture;

	@NotBlank
	@Length(max = 75)
	private String				slogan;

	@URL
	@Length(max = 255)
	private String				link;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
