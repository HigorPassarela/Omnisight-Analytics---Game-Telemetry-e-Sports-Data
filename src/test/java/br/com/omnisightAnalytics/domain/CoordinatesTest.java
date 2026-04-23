package br.com.omnisightAnalytics.domain;

import br.com.omnisightAnalytics.domain.Coordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CoordinatesTest {

    @Test
    void shouldCreateCoordinatesWithDefaultConstructorAndSetters() {
        Coordinates coordinates = new Coordinates();
        assertNull(coordinates.getX());
        assertNull(coordinates.getY());

        coordinates.setX(10.5);
        coordinates.setY(20.7);

        assertEquals(10.5, coordinates.getX());
        assertEquals(20.7, coordinates.getY());
    }

    @Test
    void shouldCreateCoordinatesWithParameterizedConstructor() {
        Coordinates coordinates = new Coordinates(15.0, 30.0);

        assertEquals(15.0, coordinates.getX());
        assertEquals(30.0, coordinates.getY());
    }
}