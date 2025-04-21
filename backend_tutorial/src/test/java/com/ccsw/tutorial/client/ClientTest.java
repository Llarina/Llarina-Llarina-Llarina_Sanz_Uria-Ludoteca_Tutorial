package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientTest {

    public static final Long NOT_EXISTS_CATEGORY_ID = 0L;
    public static final Long EXISTS_CATEGORY_ID = 1L;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    public void getExistsClientIdShouldReturnCategory() {

        Client client = mock(Client.class);
        when(client.getId()).thenReturn(EXISTS_CATEGORY_ID);
        when(clientRepository.findById(EXISTS_CATEGORY_ID)).thenReturn(Optional.of(client));

        Client clientResponse = clientService.get(EXISTS_CATEGORY_ID);

        assertNotNull(clientResponse);
        assertEquals(EXISTS_CATEGORY_ID, client.getId());
    }

    @Test
    public void getNotExistsClientIdShouldReturnNull() {

        when(clientRepository.findById(NOT_EXISTS_CATEGORY_ID)).thenReturn(Optional.empty());

        Client client = clientService.get(NOT_EXISTS_CATEGORY_ID);

        assertNull(client);
    }
}
