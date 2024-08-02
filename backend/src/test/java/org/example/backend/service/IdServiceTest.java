package org.example.backend.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdServiceTest {

    private final IdService service = new IdService();

    @Test
    void idGenerator_shouldReturnUniqueString_whenCalled() {
        String id = service.generateUUID();
        assertFalse(id.isEmpty(), "Die generierte ID sollte nicht leer sein");
        assertTrue(id.matches("^[0-9a-fA-F-]{36}$"), "Die generierte ID sollte das UUID-Format haben");
    }
}