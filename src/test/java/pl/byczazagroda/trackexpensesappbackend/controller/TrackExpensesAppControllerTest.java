package pl.harshita_gupta.trackexpensesappbackend.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.harshita_gupta.trackexpensesappbackend.general.TrackExpensesAppController;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TrackExpensesAppControllerTest {

    @InjectMocks
    TrackExpensesAppController trackExpensesAppController;

    @Test
    void sayHello() {
        String result = trackExpensesAppController.sayHello();
        assertTrue(result.startsWith("TrackExpensesApp by"));
    }

}
