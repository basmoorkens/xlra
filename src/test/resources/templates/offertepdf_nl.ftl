<html>
<head>
	<style>
		#customerInfo{
			padding-top:0;
			float:left;
		}
		
		#extraInfo{
			margin-top:0;
			padding-top:0;
			float:right;
			margin-left:80%;
			text-align:right;
		}
		#dirtyTable{
			margin-left:0;
			margin-right:0;
			width:100%;
			border-style:solid;
			border-width:2px; 
			border-color:black;
		}
		
		h1 { 
			margin-bottom:20px; 
		}
		
	</style>
</head>
<body>

<div id="customerInfo">
	${customerName}<br />
	<#if customerStreetAndNumber??>${customerStreetAndNumber}<br /><#else></#if>
	<#if customerAdres??>${customerAdres}<br /><#else></#if>
</div>

<img src="internal_logo.png" style="float:left;margin-top:0;" />
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

<table id="dirtyTable">
	<tr>
		<td>
			<table>
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
		</td>
	</tr>
	<tr>
		<td>
			<h2>Detail berekening</h2>
			${detailCalculation}
		</td>
	</tr>
	<tr>
		<td>
			${additionalConditions}
		</td>
	</tr>
</table>
<p style="margin-top:20px;">
	Uw aanvraag werd behandelt door ${createdUserFullName}
</p>
</body>
</html>