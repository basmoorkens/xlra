package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dto.QuantityGeneratorDto;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.service.RatesGeneratorService;

import java.math.BigDecimal;

import javax.ejb.Stateless;

@Stateless
public class RatesGeneratorServiceImpl implements RatesGeneratorService {

  private static final double KILO_LOWEST_INCREMENT = 50.0d;

  private static final double KILO_SWITCH_TO_100_INCREMENT = 100.0d;

  private static final double KILO_100_INCREMENT = 100.0d;

  private static final double KILO_SWITCH_TO_1000_INCREMENT = 4000.0d;

  private static final double KILO_1000_INCREMENT = 1000.0d;


  @Override
  public RateFile generateFreeCreateRateFile(final RateFile rateFile) {
    QuantityGeneratorDto genDto = new QuantityGeneratorDto(rateFile.getMeasurement());
    switch (rateFile.getMeasurement()) {
      case PALET:
        generateLinearRates(rateFile, genDto);
        break;
      case LDM:
        generateLinearRates(rateFile, genDto);
        break;
      case KILO:
        generateKiloRates(rateFile, genDto);
        break;
      default:
        break;
    }


    rateFile.fillUpRelationalProperties();
    rateFile.fillUpRateLineRelationalMap();
    return rateFile;
  }

  private void generateKiloRates(final RateFile rateFile,
      final QuantityGeneratorDto quantityGeneratorDto) {
    double currentKiloWeight = quantityGeneratorDto.getStart();
    double currentIncrement = KILO_LOWEST_INCREMENT;
    do {
      for (Zone zone : rateFile.getZones()) {
        createRateLine(rateFile, zone, currentKiloWeight);
      }
      currentKiloWeight += currentIncrement;
      if (currentKiloWeight >= KILO_SWITCH_TO_100_INCREMENT
          && currentKiloWeight < KILO_SWITCH_TO_1000_INCREMENT) {
        currentIncrement = KILO_100_INCREMENT;
      }
      if (currentKiloWeight >= KILO_SWITCH_TO_1000_INCREMENT) {
        currentIncrement = KILO_1000_INCREMENT;
      }
    } while (currentKiloWeight <= quantityGeneratorDto.getEnd());

  }

  private void createRateLine(final RateFile rateFile, Zone zone, double measurement) {
    RateLine rateLine = new RateLine();
    rateLine.setZone(zone);
    rateLine.setMeasurement(measurement);
    rateLine.setValue(new BigDecimal(0.0d));
    rateFile.addRateLine(rateLine);
  }

  private void generateLinearRates(final RateFile rateFile,
      final QuantityGeneratorDto quantityGeneratorDto) {
    for (Zone zone : rateFile.getZones()) {
      for (double d = quantityGeneratorDto.getStart(); d <= quantityGeneratorDto.getEnd(); d +=
          quantityGeneratorDto.getInterval()) {
        createRateLine(rateFile, zone, d);
      }
    }
  }
}
