package com.miw.gildedrose.controller;

import com.miw.gildedrose.dto.ItemDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ItemControllerIT {
    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    void findAllItems() {
        String url = "http://localhost:" + port + "/items";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void findItemById() {
        String url = "http://localhost:" + port + "/items";
        ResponseEntity<List> listResponse = restTemplate.getForEntity(url, List.class);
        Map<String, Object> listItemMap = (Map) listResponse.getBody().get(0);
        Long itemId = Long.getLong(listItemMap.get("id").toString());

        // Now load the item from the read endpoint
        ResponseEntity<ItemDTO> response = restTemplate.getForEntity(url + "/" + itemId, ItemDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(itemId, response.getBody().getId());
    }
}
