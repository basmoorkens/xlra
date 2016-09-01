package com.moorkensam.xlra.dto;

import com.moorkensam.xlra.model.rate.Measurement;

public class QuantityGeneratorDto {

  public static final double PALET_START = 1.0d;

  public static final double PALET_END = 34.0d;

  public static final double PALET_INCREMENT = 1.0d;

  public static final double KILO_START = 50d;

  public static final double KILO_END = 24000d;

  public static final double KILO_INCREMENT = 50d;

  public static final double LDM_START = 0.4d;

  public static final double LDM_END = 13.2d;

  public static final double LDM_INTERVAL = 0.4d;

  private double start;

  private double end;

  private double interval;

  public QuantityGeneratorDto(Measurement measurement) {
    setupValuesFromMeasurement(measurement);
  }

  private void setupValuesFromMeasurement(Measurement measurement) {
    switch (measurement) {
      case PALET:
        start = PALET_START;
        end = PALET_END;
        interval = PALET_INCREMENT;
        break;
      case KILO:
        start = KILO_START;
        end = KILO_END;
        interval = KILO_INCREMENT;
        break;

      case LDM:
        start = LDM_START;
        end = LDM_END;
        interval = LDM_INTERVAL;
        break;
      default:
        break;
    }
  }

  public double getInterval() {
    return interval;
  }

  public void setInterval(double interval) {
    this.interval = interval;
  }

  public double getEnd() {
    return end;
  }

  public void setEnd(double end) {
    this.end = end;
  }

  public double getStart() {
    return start;
  }

  public void setStart(double start) {
    this.start = start;
  }

}
