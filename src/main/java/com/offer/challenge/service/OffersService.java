package com.offer.challenge.service;

import com.offer.challenge.model.Offer;

import java.util.List;
import java.util.Optional;

public interface OffersService {
    void createOffer(Offer offer);

    void updateOffer(Offer offer);

    List<Offer> getAllActiveOffers();

    Optional<Offer> getOfferById(String id);
}
