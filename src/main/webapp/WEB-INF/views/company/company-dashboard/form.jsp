<%--
- form.jsp
-
- Copyright (C) 2012-2023 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<h2>
	<acme:message code="company.dashboard.form.title.general-indicators"/>
</h2>

<h3>
	<acme:message code="company.dashboard.form.title.practica-length"/>
</h3>
<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="company.dashboard.form.label.practica-length.count"/>
		</th>
		<td>
			<acme:print value="${practicaLength.count}"/>
		</td>
		<th scope="row">
			<acme:message code="company.dashboard.form.label.practica-length.average"/>
		</th>
		<td>
			<acme:print value="${practicaLength.average}"/>
		</td>
		<th scope="row">
			<acme:message code="company.dashboard.form.label.practica-length.min"/>
		</th>
		<td>
			<acme:print value="${practicaLength.min}"/>
		</td>
		<th scope="row">
			<acme:message code="company.dashboard.form.label.practica-length.max"/>
		</th>
		<td>
			<acme:print value="${practicaLength.max}"/>
		</td>
		<th scope="row">
			<acme:message code="company.dashboard.form.label.practica-length.deviation"/>
		</th>
		<td>
			<acme:print value="${practicaLength.deviation}"/>
		</td>
	</tr>
</table>

<h3>
	<acme:message code="company.dashboard.form.title.session-length"/>
</h3>
<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="company.dashboard.form.label.session-length.count"/>
		</th>
		<td>
			<acme:print value="${sessionLength.count}"/>
		</td>
		<th scope="row">
			<acme:message code="company.dashboard.form.label.session-length.average"/>
		</th>
		<td>
			<acme:print value="${sessionLength.average}"/>
		</td>
		<th scope="row">
			<acme:message code="company.dashboard.form.label.session-length.min"/>
		</th>
		<td>
			<acme:print value="${sessionLength.min}"/>
		</td>
		<th scope="row">
			<acme:message code="company.dashboard.form.label.session-length.max"/>
		</th>
		<td>
			<acme:print value="${sessionLength.max}"/>
		</td>
		<th scope="row">
			<acme:message code="company.dashboard.form.label.session-length.deviation"/>
		</th>
		<td>
			<acme:print value="${sessionLength.deviation}"/>
		</td>
	</tr>
</table>


<h2>
	<acme:message code="company.dashboard.form.title.total-number-practica-by-month"/>
</h2>

<div>
	<div style="display: flex">
		<input type="radio" id="theory_session" name="type" value="theory_session">
		<label for="theory_session"><acme:message code="company.dashboard.form.graph.theory-session"/></label>

		<input type="radio" id="hands_on_session" name="type" value="hands_on_session" style="margin-left: 0.5rem">
		<label for="hands_on_session"><acme:message code="company.dashboard.form.graph.hands-on-session"/></label>
	</div>

	<canvas id="canvas"></canvas>
</div>

<script type="text/javascript">

	console.log("Hello World");
	console.log(document.getElementById('theory_session').checked);
	console.log()
	console.log("${totalNumberOfPracticaByMonthForTheorySession.get("DECEMBER")}")
	console.log("${totalNumberOfPracticaByMonthForHandsOnSession.get("DECEMBER")}")

	function getType() {
		console.log(document.getElementById('theory_session').checked);
		if (document.getElementById('theory_session').checked) {
			return [
				<jstl:out value="${totalNumberOfPracticaByMonthForTheorySession.get('JANUARY')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForTheorySession.get('FEBRUARY')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForTheorySession.get('MARCH')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForTheorySession.get('APRIL')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForTheorySession.get('MAY')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForTheorySession.get('JUNE')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForTheorySession.get('JULY')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForTheorySession.get('AUGUST')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForTheorySession.get('SEPTEMBER')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForTheorySession.get('OCTOBER')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForTheorySession.get('NOVEMBER')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForTheorySession.get('DECEMBER')}"/>
			]
		} else if (document.getElementById('hands_on_session').checked) {
			return [
				<jstl:out value="${totalNumberOfPracticaByMonthForHandsOnSession.get('JANUARY')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForHandsOnSession.get('FEBRUARY')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForHandsOnSession.get('MARCH')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForHandsOnSession.get('APRIL')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForHandsOnSession.get('MAY')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForHandsOnSession.get('JUNE')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForHandsOnSession.get('JULY')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForHandsOnSession.get('AUGUST')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForHandsOnSession.get('SEPTEMBER')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForHandsOnSession.get('OCTOBER')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForHandsOnSession.get('NOVEMBER')}"/>,
				<jstl:out value="${totalNumberOfPracticaByMonthForHandsOnSession.get('DECEMBER')}"/>
			]
		}
	}

	function graph() {
		console.log(getType() + " " + getType().length);
		var data = {
			labels: [
				"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"
			],
			datasets: [
				{
					data: getType()
				}
			]
		};
		var options = {
			scales: {
				yAxes: [
					{
						ticks: {
							suggestedMin: 0.0,
							suggestedMax: 1.0
						}
					}
				]
			},
			legend: {
				display: false
			}
		};

		var canvas, context;

		canvas = document.getElementById("canvas");
		context = canvas.getContext("2d");
		new Chart(context, {
			type: "bar",
			data: data,
			options: options
		});
	}

	$(document).ready(function () {
		graph();
	});

	const radios = document.getElementsByName('type');
	radios.forEach(radio => radio.addEventListener('change', graph));

</script>

<acme:return/>

