
package acme.forms;

import java.util.Map;

import acme.datatypes.Statistic;
import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// Total number of practicas (the index in the list represent the months).
	private Map<String, Long> totalNumberOfPracticaByMonthForTheorySession;
	private Map<String, Long> totalNumberOfPracticaByMonthForHandsOnSession;


	// Average, deviation, minimum, and maximum period length of the sessions in their practica.
	private Statistic			sessionLength;

	// Average, deviation, minimum, and maximum period length of their practica.
	private Statistic			practicaLength;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
