
package acme.features.company.companyDashboard;

import java.time.Month;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.datatypes.Statistic;
import acme.entities.enums.Approach;
import acme.forms.CompanyDashboard;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyDashboardShowService extends AbstractService<Company, CompanyDashboard> {

	// Constants --------------------------------------------------------------
	protected static final String[]		PROPERTIES	= {
		"totalNumberOfPracticaByMonthForTheorySession", "totalNumberOfPracticaByMonthForHandsOnSession", "sessionLength", "practicaLength"
	};

	// Internal state ---------------------------------------------------------
	@Autowired
	private CompanyDashboardRepository	repository;


	// AbstractService Interface ----------------------------------------------
	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		final Company company;
		Principal principal;
		int userAccountId;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		company = this.repository.findOneCompanyByUserAccountId(userAccountId);

		status = company != null && principal.hasRole(Company.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final int companyId;
		final CompanyDashboard companyDashboard;
		final Principal principal;
		final int userAccountId;
		final Company company;
		final Date currentMoment;

		Statistic sessionLength;
		double averageSessionLength;
		double deviationSessionLength;
		double minimumSessionLength;
		double maximumSessionLength;
		int countSession;

		Statistic practicaLength;
		double averagePracticaLength;
		double deviationPracticaLength;
		double minimumPracticaLength;
		double maximumPracticaLength;
		int countPractica;

		final Map<String, Long> totalNumberOfPracticaByMonthForTheorySession;
		final Map<String, Long> totalNumberOfPracticaByMonthForHandsOnSession;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		company = this.repository.findOneCompanyByUserAccountId(userAccountId);
		companyId = company.getId();
		currentMoment = MomentHelper.getCurrentMoment();

		averageSessionLength = this.repository.findAverageSessionLength(companyId);
		deviationSessionLength = this.repository.findDeviationSessionLength(companyId);
		minimumSessionLength = this.repository.findMinimumSessionLength(companyId);
		maximumSessionLength = this.repository.findMaximumSessionLength(companyId);
		countSession = this.repository.findCountSession(companyId);
		sessionLength = new Statistic(countSession, averageSessionLength, maximumSessionLength, minimumSessionLength, deviationSessionLength);

		averagePracticaLength = this.repository.findAveragePracticaLength(companyId);
		deviationPracticaLength = this.repository.findDeviationPracticaLength(companyId);
		minimumPracticaLength = this.repository.findMinimumPracticaLength(companyId);
		maximumPracticaLength = this.repository.findMaximumPracticaLength(companyId);
		countPractica = this.repository.findCountPractica(companyId);
		practicaLength = new Statistic(countPractica, averagePracticaLength, maximumPracticaLength, minimumPracticaLength, deviationPracticaLength);

		totalNumberOfPracticaByMonthForTheorySession = this.parse(this.repository.findTotalNumberOfPracticaByMonth(companyId, currentMoment, Approach.THEORY_SESSION));
		totalNumberOfPracticaByMonthForHandsOnSession = this.parse(this.repository.findTotalNumberOfPracticaByMonth(companyId, currentMoment, Approach.HANDS_ON_SESSION));
		System.out.println(totalNumberOfPracticaByMonthForTheorySession);
		System.out.println(totalNumberOfPracticaByMonthForHandsOnSession);

		companyDashboard = new CompanyDashboard();

		companyDashboard.setTotalNumberOfPracticaByMonthForTheorySession(totalNumberOfPracticaByMonthForTheorySession);
		companyDashboard.setTotalNumberOfPracticaByMonthForHandsOnSession(totalNumberOfPracticaByMonthForHandsOnSession);
		companyDashboard.setSessionLength(sessionLength);
		companyDashboard.setPracticaLength(practicaLength);

		super.getBuffer().setData(companyDashboard);
	}

	@Override
	public void unbind(final CompanyDashboard companyDashboard) {
		Tuple tuple;

		tuple = super.unbind(companyDashboard, CompanyDashboardShowService.PROPERTIES);

		System.out.println(tuple);
		super.getResponse().setData(tuple);
	}

	// Ancillary methods ------------------------------------------------------
	private Map<String, Long> parse(final List<Object[]> list) {
		return list.stream().collect(Collectors.toMap(key -> Month.of((int) key[0]).toString(), value -> (long) value[1]));
	}
}
