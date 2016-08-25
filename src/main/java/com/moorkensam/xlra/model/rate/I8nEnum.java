package com.moorkensam.xlra.model.rate;

/**
 * interface to enforce i8n on an enum.
 * 
 * @author bas
 *
 */
public interface I8nEnum {

  public void setDescription(String description);

  public String getDescription();

  public String getI8nKey();


}
