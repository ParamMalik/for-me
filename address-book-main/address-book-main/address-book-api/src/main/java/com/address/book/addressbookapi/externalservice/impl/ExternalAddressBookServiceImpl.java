package com.address.book.addressbookapi.externalservice.impl;

import com.address.book.addressbookapi.dto.ContactDTO;
import com.address.book.addressbookapi.externalservice.ExternalAddressBookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
@PropertySource(value = "classpath:application.properties")
public class ExternalAddressBookServiceImpl implements ExternalAddressBookService {

    ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    RestTemplate restTemplate;

    private static final String URI = "http://10.50.2.203:8080";

    // Search
    @SneakyThrows
    public ContactDTO[] getContactList() {
        String addressList = restTemplate.getForObject(URI + "/getAllCustomer/isRemote=n", String.class);
        return objectMapper.readValue(addressList, ContactDTO[].class);
    }

    // Update
    public void deleteContact(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.exchange(URI + "/updateCustomer/" + id + "/isRemote=n", HttpMethod.PUT, entity, String.class);
    }

    // Find By First Name
    @SneakyThrows
    public ContactDTO[] getContactListByFirstName(String firstName) {

        String addressString = restTemplate.getForObject(URI + "/getCustomer/" + firstName + "/isRemote=n", String.class);
        return objectMapper.readValue(addressString, ContactDTO[].class);

    }

    // Save
    public ContactDTO saveContact(ContactDTO contact) {


        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String jsonString = null;

        ContactDTO contactDTO = null;
        try {
            jsonString = objectMapper.writeValueAsString(contact);
            HttpEntity<String> stringHttpEntity = new HttpEntity<>(jsonString, httpHeaders);
            String responseEntity = restTemplate.postForObject(URI + "/customer_save/isRemote=n", stringHttpEntity, String.class);
            contactDTO = objectMapper.readValue(responseEntity, ContactDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return contactDTO;
    }

}
