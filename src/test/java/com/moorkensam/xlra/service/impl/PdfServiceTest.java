package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.error.TemplatingException;
import com.moorkensam.xlra.model.offerte.OfferteOptionDto;
import com.moorkensam.xlra.model.offerte.PriceCalculation;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Kind;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.model.translation.TranslationKey;
import com.moorkensam.xlra.service.FileService;
import com.moorkensam.xlra.service.util.ConfigurationLoader;
import com.moorkensam.xlra.service.util.TranslationConfigurationLoader;

import com.itextpdf.text.DocumentException;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class PdfServiceTest extends UnitilsJUnit4 {

  private PdfServiceImpl pdfService;

  private QuotationResult offerte;

  private Language offerteLanguage;

  @Mock
  private FileService fileService;

  private ConfigurationLoader configLoader;

  private TemplateParseServiceImpl templateParseService;

  private TranslationConfigurationLoader translationLoader;

  /**
   * init the test.
   */
  @Before
  public void init() {
    translationLoader = TranslationConfigurationLoader.getInstance();
    configLoader = ConfigurationLoader.getInstance();
    pdfService = new PdfServiceImpl();
    pdfService.setFileService(fileService);
    offerte = new QuotationResult();
    templateParseService = TemplateParseServiceImpl.getInstance();
    templateParseService.setTranslationLoader(translationLoader);
    pdfService.setTemplateParseService(templateParseService);
    pdfService.setConfigLoader(configLoader);
    offerteLanguage = Language.NL;
    QuotationQuery query = new QuotationQuery();
    offerte.setQuery(query);
    query.setQuotationDate(new Date());
    query.setCountry(new Country());
    query.getCountry().setNames(new HashMap<Language, String>());
    query.getCountry().setDutchName("Belgie");
    query.getCountry().setEnglishName("Belgium");
    query.setPostalCode("2222");
    query.setQuantity(10d);
    query.setMeasurement(Measurement.PALET);
    query.setKindOfRate(Kind.EXPRES);
    PriceCalculation calculation = new PriceCalculation();
    calculation.setAppliedOperations(Arrays.asList(TranslationKey.DIESEL_SURCHARGE,
        TranslationKey.EXPORT_FORM, TranslationKey.IMPORT_FORM, TranslationKey.CHF_SURCHARGE,
        TranslationKey.ADR_SURCHARGE));
    calculation.setBasePrice(new BigDecimal(100d));
    calculation.setChfPrice(new BigDecimal(10d));
    calculation.setAdrSurchargeMinimum(new BigDecimal(15d));
    calculation.setCalculatedAdrSurcharge(new BigDecimal(20d));
    calculation.setDieselPrice(new BigDecimal(10d));
    calculation.setExportFormalities(new BigDecimal(10d));
    calculation.setImportFormalities(new BigDecimal(10d));
    calculation.calculateTotalPrice();
    offerte.setCalculation(calculation);
    offerte.setOfferteUniqueIdentifier("uq123");
    offerte.setCreatedUserFullName("basie");
    offerte.getQuery().setCustomer(new Customer());
    offerte.getQuery().getCustomer().setName("testje");
    OfferteOptionDto option = new OfferteOptionDto();
    option.setI8nKey("calculation.fulldetail.diesel.surcharge");
    option.setKey(TranslationKey.DIESEL_SURCHARGE);
    option.setSelected(true);
    option.setCalculationOption(false);
    option.setValue("DIESELLOL");
    offerte.setSelectableOptions(Arrays.asList(option));
  }

  @Test
  public void testGeneratePdf() throws FileNotFoundException, DocumentException,
      TemplatingException {
    EasyMock.expect(fileService.getTemporaryFilePathForPdf("uq123")).andReturn("uq123.pdf");
    EasyMockUnitils.replay();
    pdfService.generateTransientOffertePdf(offerte, Language.NL);
  }

}
