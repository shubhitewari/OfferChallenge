package com.offer.challenge.service;

import com.offer.challenge.model.Offer;
import com.offer.challenge.repository.OffersRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OffersServiceImpl implements OffersService {

    @Getter
    private final OffersRepository offersRepository;

    @Autowired
    public OffersServiceImpl(OffersRepository offersRepository) {
        this.offersRepository = offersRepository;
    }

    @Override
    public void createOffer(Offer offer) {
        this.offersRepository.createOffer(offer);
    }

    @Override
    public void updateOffer(Offer offer) {
        this.offersRepository.updateOffer(offer);
    }

    @Override
    public List<Offer> getAllActiveOffers() {
        return this.offersRepository.getAllActiveOffers();
    }

    @Override
    public Optional<Offer> getOfferById(String id) {
        return this.offersRepository.getOfferById(id);
    }

}
