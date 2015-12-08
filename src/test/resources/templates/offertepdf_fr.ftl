<html>
<head>
	<style>
		#overviewTable{
			border-style:solid;
			border-width:1px;
			border-color:black;
		}
		
		#customerInfo{
			float:left;
		}
		
		#extraInfo{
			float:left;
			top:0px;
			right:0px;
		}
		#content{
			border-width:1px;
			border-style:solid;
			border-color:black;
		}
	</style>
</head>
<body>

<div id="customerInfo">
	${customerName}<br />
	<#if customerStreetAndNumber??>
		${customerStreetAndNumber}<br />
	</#if>
	<#if customerAdres??>
		${customerAdres}
	</#if>
</div>

<div id="extraInfo">
	<p>
		E X T R A Logistics NV<br />
		Cyriel Buyssestraat 1<br />
		B-1800 Vilvoorde<br />
		BTW: BE0475.454.606<br />
	</p>

	<p>
		Tel.: +32 (0)2 752 40 00<br />
		Fax: +32 (0)2 752 40 06<br />
		E-Mail: info@extra-logistics.be<br />
	</p>
	
	<p>
	 	Offertedatum: ${offerteDate?date}
	</p>
</div>
<h1>Offerte ${offerteKeyValue}</h1>

Geachte, <br />

<p>
	Hierbij ontvangt u een prijsofferte voor onderstaande diensten.
</p>

<div id="content">
	<table id="overviewTable">
		<tr>
			<td colspan="2">
				<h2>Uw aanvraag</h2>
			</td>
		</tr>
		<tr>
			<td>Land:</td><td>${country}</td>
		</tr>
		<tr>
			<td>Postcode:</td><td>${postalCode}</td>
		</tr>
		<tr>
			<td>Hoeveelheid:</td><td>${quantityAndMeasurement}</td>
		</tr>
		<tr>
			<td>Transport type:</td><td>${transportType}</td>
		</tr>
	</table>
	
	<h2>Detail berekening</h2>
	${detailCalculation}
	
	${additionalConditions}
</div>
Uw aanvraag werd behandelt door ${createdUserFullName}
</body>
</html>