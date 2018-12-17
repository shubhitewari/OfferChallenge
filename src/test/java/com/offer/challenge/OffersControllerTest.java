package com.offer.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.offer.challenge.model.Offer;
import com.offer.challenge.service.OffersServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class OffersControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private OffersServiceImpl offersService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void prepareMockMvc() {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();

        offersService.getOffersRepository().clearOffers();

    }


    public void testCreateOffer() throws Exception {
        this.mockMvc.perform(post("/v1/offers").contentType(MediaType.APPLICATION_JSON)
                .content("{\"offerId\":\"B1G1\",\"description\":\"Buy 1 Get 1\",\"currency\":\"GBP\",\"price\":10.00,\"expiryDate\":\"2018-12-16T00:00:00\"}")).andExpect(status().isCreated());
    }


    @Test
    public void testCreateDuplicateOffer() throws Exception {
        this.mockMvc.perform(post("/v1/offers").contentType(MediaType.APPLICATION_JSON)
                .content("{\"offerId\":\"B1G1\",\"description\":\"Buy 1 Get 1\",\"currency\":\"GBP\",\"price\":10.00,\"expiryDate\":\"2018-12-16T00:00:00\"}")).andExpect(status().isCreated());

        this.mockMvc.perform(post("/v1/offers").contentType(MediaType.APPLICATION_JSON)
                .content("{\"offerId\":\"B1G1\",\"description\":\"Buy 1 Get 1\",\"currency\":\"GBP\",\"price\":20.00,\"expiryDate\":\"2018-12-16T00:00:00\"}")).andExpect(status().isBadRequest());
    }


    @Test
    public void testCreateOfferNoBody() throws Exception {
        this.mockMvc.perform(post("/v1/offers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    public void testGetOffer() throws Exception {
        Date today = Calendar.getInstance().getTime();
        Offer offer1 = new Offer("B1G1", "Buy 1 Get 1", "GBP", new BigDecimal(20.00), today);
        this.offersService.createOffer(offer1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.format(today);
        this.mockMvc.perform(get("/v1/offers/B1G1"))
                .andExpect(status().isOk())
                .andExpect(
                        content().string("{\"offerId\":\"B1G1\",\"description\":\"Buy 1 Get 1\",\"currency\":\"GBP\",\"price\":20,\"expiryDate\":\"" + today + "\",\"active\":true}"));
    }



    public void testGetAllActiveOffers() throws Exception {
        Date today = new Date();
        Offer offer1 = new Offer("B1G1", "Buy 1 Get 1", "GBP", new BigDecimal(20), today);
        this.offersService.createOffer(offer1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.format(today);
        this.mockMvc.perform(get("/v1/offers/"))
                .andExpect(status().isOk())
                .andExpect(
                        content().string("{\"offerId\":\"B1G1\",\"description\":\"Buy 1 Get 1\",\"currency\":\"GBP\",\"price\":20.00,\"active\":true,\"expiryDate\":\"" + today + "\"}"));

        this.mockMvc.perform(put("/v1/offers/").contentType(MediaType.APPLICATION_JSON)
                .content("{\"offerId\":\"B1G1\",\"description\":\"Buy 1 Get 1\",\"currency\":\"GBP\",\"price\":20.00,\"active\":false,\"expiryDate\":\"2018-12-16T00:00:00\"}"))
                .andExpect(status().isNoContent());

        this.mockMvc.perform(get("/v1/offers/"))
                .andExpect(status().isNoContent());
    }


}
