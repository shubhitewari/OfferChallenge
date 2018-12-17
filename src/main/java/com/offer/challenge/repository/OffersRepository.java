package com.offer.challenge.repository;

import com.offer.challenge.exception.DuplicateOfferIdException;
import com.offer.challenge.model.Offer;

import java.util.List;
import java.util.Optional;

public interface OffersRepository {

    void createOffer(Offer offer) throws DuplicateOfferIdException;

    void updateOffer(Offer offer);

    List<Offer> getAllActiveOffers();

    Optional<Offer> getOfferById(String id);

    public void clearOffers();
}
