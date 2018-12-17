package com.offer.challenge.controller;

import com.offer.challenge.exception.DuplicateOfferIdException;
import com.offer.challenge.model.Offer;
import com.offer.challenge.service.OffersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/offers")
@Slf4j
public class OffersController {

    private final OffersService offersService;

    @Autowired
    public OffersController(OffersService offersService) {
        this.offersService = offersService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createOffer(@RequestBody @Valid Offer offer) {
        log.info("Creating offer {}", offer);
        try {
            this.offersService.createOffer(offer);
        } catch (DuplicateOfferIdException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Offer>> getAllOffers() {
        log.info("Retrieving all active offers ");
        List<Offer> offers = this.offersService.getAllActiveOffers();
        if (offers.isEmpty()) {
            return new ResponseEntity("There are no active offers", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Offer>>(offers, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getOffer(@PathVariable("id") String id) {
        log.info("Retrieving offer with id {}", id);
        Optional<Offer> offer = this.offersService.getOfferById(id);
        if (offer.isPresent()) {
            return new ResponseEntity<Offer>(offer.get(), HttpStatus.OK);
        }
        log.info("Offer with id {} not found", id);
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PutMapping
    public ResponseEntity updateOffer(@RequestBody Offer offer) {
        log.info("Updating offer with id {}", offer.getOfferId());
        Optional<Offer> offerToUpdate = this.offersService.getOfferById(offer.getOfferId());
        if (offerToUpdate.isPresent()) {
            offerToUpdate.get().setActive(offer.isActive());
            offerToUpdate.get().setExpiryDate(offer.getExpiryDate());
            offerToUpdate.get().setPrice(offer.getPrice());
            this.offersService.updateOffer(offerToUpdate.get());
            return new ResponseEntity<Offer>(offerToUpdate.get(), HttpStatus.NO_CONTENT);
        }
        log.info("Offer with id {} not found", offer.getOfferId());
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


}
