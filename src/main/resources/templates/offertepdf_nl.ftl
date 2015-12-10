<html>
<head>
	<style>
	
		body{
			font-size:10px;
		}
	
		#dirtyTable{
			margin-left:0;
			margin-right:0;
			width:100%;
			border-style:solid;
			border-width:2px; 
			border-color:black;
			font-size:11px;
		}
		
		.right{
			text-align:right;
		}
		
		#addCond{
			font-size: 8px;
		}
		
		#dirtyTable td{
			font-size:10px;
		}
		
		h1 { 
			margin-bottom:20px; 
			font-size: 24px;
		}
		
		h2 {
			font-size: 16px;
		}
		
		#headerTable{
			width:100%;
			padding:0;
			margin-bottom: 3%;
		}
		
		#extraInfo{
			text-align:right;
		}
	</style>
</head>
<body>
<table id="headerTable">
	<tr>
		<td valign="top" style="width:65%;">
			<img src="internal_logo.png" width="250px" height="60px" />
		</td>
		<td valign="top" style="width:35%;">
			<div id="extraInfo">
				<p>
					E X T R A Logistics NV<br />
					Tel.: +32 (0)2 752 40 00<br />
					Fax: +32 (0)2 752 40 06<br />
					E-Mail: info@extra-logistics.be<br />
				</p><br />
				
				<p>
				 	Offertedatum: ${offerteDate?date}
				</p>
			</div>
		</td>
	</tr>
</table>
<h1>Offerte ${offerteKeyValue}</h1>

Geachte ${customerName},<br />

<p>
	Hierbij ontvangt u een prijsofferte voor onderstaande diensten.<br /><br />
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
				<td style="width:40%">Land:</td><td class="right">${country}</td>
		</tr>;
		<tr>
				<td>Postcode:</td><td class="right">${postalCode}</td>
		</tr>
		<tr>
				<td>Hoeveelheid:</td><td class="right">${quantityAndMeasurement}</td>
		</tr>
		<tr>
				<td>Transport type:</td><td class="right">${transportType}</td>
		</tr>
		<tr>
			<td colspan="2">
				<h2>Detail berekening</h2>
			</td>
		</tr>
		
		<#if baseprice??>
		<tr>
			<td>${calculationfulldetailbasicprice}</td><td class="right">${baseprice} EUR</td>
		</tr>
		<#else></#if>
		
		<#if dieselprice??>
		<tr>
			<td>${calculationfulldetaildieselsurcharge}</td><td class="right">${dieselprice} EUR</td>
		</tr>
		<#else></#if>
		
		<#if chfprice??>
		<tr>
			<td>${calculationfulldetailswissfrancsurcharge}</td><td class="right">${chfprice} EUR</td>
		</tr>
		<#else></#if>
		
		<#if importforms??>
		<tr>
			<td>${calculationfulldetailimportformalities}</td><td class="right">${importforms} EUR</td>
		</tr>
		<#else></#if>
		
		<#if exportforms??>
		<tr>
			<td>${calculationfulldetailexportformalities}</td><td class="right">${exportforms} EUR</td>
		</tr>
		<#else></#if>
		
		<#if adr??>
		<tr>
			<td>${calculationfulldetailadrsurcharge}</td><td class="right">${adr} EUR</td>
		</tr>
		<#else></#if>
		
		<#if finalprice??>
		<tr>
			<td><h3>${calculationfulldetailtotalprice}</h3></td><td class="right"><h3>${finalprice} EUR</h3></td>
		</tr>
		<#else></#if>
		
		
		<#if conditions??>
		<tr>
			<td colspan="2"><h2>Additionele voorwaarden van toepassing</h2></td>
		</tr>
		
		<#list conditions?keys as key> 
    		<tr>
    			<td>${key}</td><td class="right"> ${conditions[key]}</td>
    		</tr> 
		</#list>
		<#else></#if>
		
		</table>
	</td>
	<td style="width:30%;">
	</td>
	</tr> 
</table>
<p style="margin-top:20px;font-size:10px;font-weight:bold;">
	Indien u dit transport wil confirmeren gelieve te bevestigen op het email adres: <br /> 
	international.be@extra-logistics.be met vermelding van uw offerte nummer ${offerteKeyValue}. 
	<br /> <br />
	Uw aanvraag werd behandelt door ${createdUserFullName}.
</p>
</body>
</html>