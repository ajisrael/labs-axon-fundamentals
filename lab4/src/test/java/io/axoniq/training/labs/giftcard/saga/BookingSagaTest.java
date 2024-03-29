package io.axoniq.training.labs.giftcard.saga;

import org.axonframework.test.saga.SagaTestFixture;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.fail;

class BookingSagaTest {

    private SagaTestFixture<BookingSaga> fixture;

    @BeforeEach
    void setUp() {
        this.fixture = new SagaTestFixture<>(BookingSaga.class);
    }

    @Test
    void shouldRedeemFromGiftCardWhenBookingIsPlaced() {
        fail("To be implemented");
    }

    @Test
    void shouldRejectBookingOnPartialPaymentDeadlineExpired() {
        fail("To be implemented");
    }

    @Test
    void shouldEndSagaOnBookingRejectedEvent() {
        fail("To be implemented");
    }
}
