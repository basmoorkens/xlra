package com.moorkensam.xlra.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;

import org.primefaces.event.RowEditEvent;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.controller.util.ZoneFactory;
import com.moorkensam.xlra.model.configuration.Interval;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.model.rate.ZoneType;
import com.moorkensam.xlra.service.CountryService;

@ManagedBean
@ViewScoped
public class CountryController {

	@Inject
	private CountryService countryService;

	private List<Country> countries;

	private Country selectedCountry;

	private Zone selectedZone;

	private boolean renderDetailGrid = false;

	private boolean renderAddZoneGrid = false;

	@PostConstruct
	public void initialize() {
		countries = countryService.getAllCountriesFullLoad();
	}

	public void setupNewZone() {
		selectedZone = ZoneFactory.createZone(selectedCountry);
		setRenderAddZoneGrid(true);
	}

	public void doSelectCountry(AjaxBehaviorEvent event) {
		renderDetailGrid = true;
		renderAddZoneGrid = false;
	}

	public void onZoneEdit(RowEditEvent event) {
		Zone zone = (Zone) event.getObject();
		countryService.updateZone(zone);
		MessageUtil.addMessage("Zone update", "Changed zone information for "
				+ zone.getName());
		refreshSelectedCountry();
	}

	public void saveNewZone() {
		if (isValidZone(selectedZone)) {
			countryService.createZone(selectedZone);
			refreshSelectedCountry();
			renderAddZoneGrid = false;
		}
	}

	private boolean isValidZone(Zone zone) {
		if (isNumericCountryZone()) {
			zone.convertNumericalPostalCodeStringToList();
			for (Interval interval : zone.getNumericalPostalCodes()) {
				if (interval.getEnd() == 0) {
					MessageUtil.addMessage("Stop code is 0.",
							"Stop postal code should not be 0.");
					return false;
				}

				if (interval.getEnd() == interval.getStart()) {
					MessageUtil.addMessage("Start code is equal to stop code.",
							"Start and stop code should be different.");
					return false;
				}
			}
			return true;
		}

		if (isAlphaNumericCountryZone()) {
			return true;
		}
		return false;
	}

	private void refreshSelectedCountry() {
		selectedCountry = countryService
				.getCountryById(selectedCountry.getId());
	}

	public void resetPage() {
		setRenderAddZoneGrid(false);
		selectedZone = null;
	}

	public Country getSelectedCountry() {
		return selectedCountry;
	}

	public void setSelectedCountry(Country country) {
		selectedCountry = country;
	}

	public CountryService getCountryService() {
		return countryService;
	}

	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public Zone getSelectedZone() {
		return selectedZone;
	}

	public void setSelectedZone(Zone zone) {
		this.selectedZone = zone;
	}

	public void setRenderDetailGrid(boolean toSet) {
		this.renderDetailGrid = toSet;
	}

	public boolean getRenderDetailGrid() {
		return this.renderDetailGrid;
	}

	public boolean isRenderAddZoneGrid() {
		return renderAddZoneGrid;
	}

	public void setRenderAddZoneGrid(boolean renderAddZoneGrid) {
		this.renderAddZoneGrid = renderAddZoneGrid;
	}

	public boolean isNumericCountryZone() {
		if (selectedCountry != null) {
			if (selectedCountry.getZoneType() == ZoneType.NUMERIC_CODES) {
				return true;
			}
		}
		return false;
	}

	public boolean isAlphaNumericCountryZone() {
		if (selectedCountry != null) {
			if (selectedCountry.getZoneType() == ZoneType.ALPHANUMERIC_LIST) {
				return true;
			}
		}
		return false;
	}

}
