package com.offer.challenge.repository;

import com.offer.challenge.exception.DuplicateOfferIdException;
import com.offer.challenge.model.Offer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OffersRepositoryInMemory implements OffersRepository {

    private final List<Offer> offers = new ArrayList<Offer>();

    @Override
    public void createOffer(Offer offer) throws DuplicateOfferIdException {
        if (getOfferById(offer.getOfferId()).isPresent()) {
            throw new DuplicateOfferIdException(
                    "Offer id " + offer.getOfferId() + " already exists!");
        }
        offers.add(offer);
    }

    @Override
    public List<Offer> getAllActiveOffers() {
        offers.forEach(offer -> checkAndUpdateOffer(offer));
        return offers.stream().filter(o -> o.isActive()).collect(Collectors.toList());
    }

    @Override
    public Optional<Offer> getOfferById(String id) {
        for (Offer offer : offers) {
            if (id.equalsIgnoreCase(offer.getOfferId())) {
                checkAndUpdateOffer(offer);
                return Optional.of(offer);
            }
        }
        return Optional.empty();
    }

    private void checkAndUpdateOffer(Offer offer) {
        if (offer.getExpiryDate().before(new Date())) {
            offer.setActive(false);
        }

    }

    public void updateOffer(Offer offer) {
        int index = offers.indexOf(offer);
        offers.set(index, offer);
    }


    @Override
    public void clearOffers() {
        offers.clear();
    }

}

