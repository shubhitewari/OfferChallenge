package com.offer.challenge.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Slf4j
public class Offer {

  @NotNull
  private final String offerId;

  @NotNull
  private String description;

  private String currency;

  private BigDecimal price;

  private Date expiryDate;

  private boolean active = true;

  @JsonCreator
  public Offer(@JsonProperty("offerId") String offerId,
               @JsonProperty("description") String description,
               @JsonProperty("currency") String currency,
               @JsonProperty("price") BigDecimal price,
               @JsonProperty("expiryDate") Date expiryDate) {
    this.offerId = offerId;
    this.description = description;
    this.expiryDate = expiryDate;
    this.currency = currency;
    this.price = price;
  }

  
   
}
