package com.offer.challenge;

import com.offer.challenge.exception.DuplicateOfferIdException;
import com.offer.challenge.model.Offer;
import com.offer.challenge.service.OffersServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsServiceTest {

    @Autowired
    private OffersServiceImpl offersService;

    @Before
    public void setup() {
        offersService.getOffersRepository().clearOffers();
    }

    @Test
    public void testCreateAndGetOffer() throws Exception {

        Offer offer = new Offer("B1G1", "Buy 1 Get 1", "GBP", new BigDecimal(20), new Date());
        this.offersService.createOffer(offer);

        Assertions.assertThat(this.offersService.getOfferById("B1G1").get()).isEqualTo(offer);
    }

    @Test
    public void testCreateDuplicateOffer() throws Exception {
        Offer offer = new Offer("B1G1", "Buy 1 Get 1", "GBP", new BigDecimal(20), new Date());
        this.offersService.createOffer(offer);
        try {
            this.offersService.createOffer(offer);
            fail("Should have failed when adding duplicate offer");
        } catch (DuplicateOfferIdException ex) {
            assertThat(ex.getMessage()).isEqualTo("Offer id B1G1 already exists!");
        }

    }

    @Test
    public void testGetAllActiveOffers() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date today = calendar.getTime();
        Offer offer1 = new Offer("B1G1", "Buy 1 Get 1", "GBP", new BigDecimal(20), today);
        this.offersService.createOffer(offer1);

        Offer offer2 = new Offer("B1G2", "Buy 1 Get 2", "GBP", new BigDecimal(30), today);
        this.offersService.createOffer(offer2);

        Offer offer3 = new Offer("B1G1-2", "Buy 1 Get 2", "GBP", new BigDecimal(30), today);
        this.offersService.createOffer(offer3);

        Assertions.assertThat(this.offersService.getAllActiveOffers().size()).isEqualTo(3);

        offer3.setActive(false);
        this.offersService.updateOffer(offer3);
        Assertions.assertThat(this.offersService.getAllActiveOffers().size()).isEqualTo(2);
    }
}
