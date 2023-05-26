
package acme.entities.courses;

import acme.entities.enums.Approach;
import acme.framework.components.datatypes.Money;
import acme.framework.data.AbstractEntity;
import acme.roles.Lecturer;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@Setter
@Table(indexes = {
	@javax.persistence.Index(columnList = "id")
})
public class Course extends AbstractEntity {

	//	Serialisation identifier ---------------------------
	protected static final long	serialVersionUID	= 1L;

	//	Attributes -----------------------------------------
	@Column(unique = true)
	@NotBlank
	@Pattern(regexp = "^[A-Z]{1,3}\\d{3}$")
	protected String			code;

	@NotBlank
	@Length(max = 75)
	protected String			title;

	@NotBlank
	@Length(max = 100)
	protected String			courseAbstract;

	@NotNull
	protected Money				retailPrice;

	@URL
	protected String			link;

	protected boolean			draftMode;

	@NotNull
	protected Approach			type;

	//	Derived attributes ------------------------------------

	//	Relationships -----------------------------------------
	@Valid
	@NotNull
	@ManyToOne(optional = false)
	protected Lecturer			lecturer;

}
