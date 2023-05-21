
package acme.forms;

import java.util.Map;

import acme.datatypes.Statistic;
import acme.entities.enums.Approach;
import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// Total number of tutorial (the index in the list represent type of tutorial).
	private Map<Approach, Integer>	totalNumberOfActivities;

	//Average, deviation, minimum, and maximum period of the activities in his or her workbook;
	private Statistic				sessionLength;
	// Average, deviation, minimum, and maximum time length of his or her enrolled.
	private Statistic				tutorialLength;
	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
