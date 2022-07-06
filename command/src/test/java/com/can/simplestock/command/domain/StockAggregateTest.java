package com.can.simplestock.command.domain;

import com.can.simplestock.command.api.commands.CloseStockCommand;
import com.can.simplestock.command.api.commands.DecreaseStockCommand;
import com.can.simplestock.command.api.commands.IncreaseStockCommand;
import com.can.simplestock.command.api.commands.OpenStockCommand;
import com.can.simplestock.common.constants.ProductType;
import com.can.simplestock.common.events.StockClosedEvent;
import com.can.simplestock.common.events.StockDecreasedEvent;
import com.can.simplestock.common.events.StockIncreasedEvent;
import com.can.simplestock.common.events.StockOpenedEvent;
import com.can.simplestock.cqrsescore.messages.BaseEvent;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StockAggregateTest {
    private final StockAggregate stockAggregate = new StockAggregate();
    private final OpenStockCommand openStockCommand = new OpenStockCommand("E100", "Iphone", ProductType.ELECTRONICS, 10);
    private final String id = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        openStockCommand.setId(id);
        stockAggregate.openStock(openStockCommand);
    }

    @Nested
    @DisplayName("Open Stock Command Cases")
    class OpenStockCommandTests {
        @Test
        @DisplayName("when OpenStockCommand supplied, StockOpenedEvent should be fired")
        void should_StockOpenedEvent_fired_when_OpenStockCommand_supplied() {
            assertAll(
                    () -> assertEquals(1, stockAggregate.getUnconsumedDomainEvents().size()),
                    () -> assertEquals(StockOpenedEvent.class, stockAggregate.getUnconsumedDomainEvents().get(0).getClass()));
        }

        @Test
        @DisplayName("when OpenStockCommand supplied, aggregate state should be updated")
        void should_open_stock_when_StockOpenedEvent_fired() {
            assertAll(
                    () -> assertEquals(id, stockAggregate.getId()),
                    () -> assertEquals("E100", stockAggregate.getProductCode()),
                    () -> assertEquals("Iphone", stockAggregate.getProductName()),
                    () -> assertEquals(ProductType.ELECTRONICS, stockAggregate.getProductType()),
                    () -> assertEquals(10, stockAggregate.getAvailableInStock()),
                    () -> assertTrue(stockAggregate.isActive())
            );
        }

        @Test
        @DisplayName("when unknown event supplied, NoSuchMethodException should be fired ")
        void should_ThrowNoSuchMethodException_When_Given_EventThatDoesNotHaveApplyMethod() {
            var id = UUID.randomUUID().toString();
            var dummyEvent = DummyEvent.builder()
                    .id(id)
                    .version(1)
                    .build();
            stockAggregate.raiseEvent(dummyEvent);
            assertFalse(stockAggregate.getUnconsumedDomainEvents().contains(dummyEvent));
        }
    }


    @Nested
    @DisplayName("Increase Stock Cases")
    class IncreaseStockCommandTests {

        @Test
        @DisplayName("when StockAggregate is not active, IllegalStateException should be thrown")
        void should_throw_IllegalStateException_when_StockAggregate_is_not_active() {
            stockAggregate.setActive(false);
            IncreaseStockCommand increaseStockCommand = new IncreaseStockCommand(5);
            assertThrows(IllegalStateException.class, () -> stockAggregate.increaseStock(increaseStockCommand));
        }

        @Test
        @DisplayName("when stock increase amount is negative, IllegalStateException should be thrown")
        void should_throw_IllegalStateException_when_increase_amount_is_negative() {
            IncreaseStockCommand increaseStockCommand = new IncreaseStockCommand(-5);
            assertThrows(IllegalStateException.class, () -> stockAggregate.increaseStock(increaseStockCommand));
        }

        @Test
        @DisplayName("when IncreaseStockCommand supplied, StockIncreasedEvent should be fired")
        void should_StockIncreasedEvent_fired_when_IncreaseStockCommand_supplied() {
            IncreaseStockCommand increaseStockCommand = new IncreaseStockCommand(5);
            stockAggregate.increaseStock(increaseStockCommand);
            assertAll(
                    () -> assertEquals(2, stockAggregate.getUnconsumedDomainEvents().size()),
                    () -> assertEquals(StockIncreasedEvent.class, stockAggregate.getUnconsumedDomainEvents().get(1).getClass()));
        }

        @Test
        @DisplayName("when IncreaseStockCommand supplied, stock aggregate amount should be updated")
        void should_increase_stock_amount_when_StockIncreasedEvent_fired() {
            IncreaseStockCommand increaseStockCommand = new IncreaseStockCommand(5);
            stockAggregate.increaseStock(increaseStockCommand);
            assertEquals(15, stockAggregate.getAvailableInStock());
        }
    }

    @Nested
    @DisplayName("Decrease Stock Cases")
    class DecreaseStockCommandTests {

        @Test
        @DisplayName("when StockAggregate is not active, IllegalStateException should be thrown")
        void should_throw_IllegalStateException_when_StockAggregate_is_not_active() {
            stockAggregate.setActive(false);
            DecreaseStockCommand decreaseStockCommand = new DecreaseStockCommand(5);
            assertThrows(IllegalStateException.class, () -> stockAggregate.decreaseStock(decreaseStockCommand));
        }

        @Test
        @DisplayName("when stock increase amount is negative, IllegalStateException should be thrown")
        void should_throw_IllegalStateException_when_increase_amount_is_negative() {
            DecreaseStockCommand increaseStockCommand = new DecreaseStockCommand(-5);
            assertThrows(IllegalStateException.class, () -> stockAggregate.decreaseStock(increaseStockCommand));
        }

        @Test
        @DisplayName("when IncreaseStockCommand supplied, StockOpenedEvent should be fired")
        void should_StockIncreasedEvent_fired_when_IncreaseStockCommand_supplied() {
            DecreaseStockCommand decreaseStockCommand = new DecreaseStockCommand(5);
            stockAggregate.decreaseStock(decreaseStockCommand);
            assertAll(
                    () -> assertEquals(2, stockAggregate.getUnconsumedDomainEvents().size()),
                    () -> assertEquals(StockDecreasedEvent.class, stockAggregate.getUnconsumedDomainEvents().get(1).getClass()));
        }

        @Test
        @DisplayName("when OpenStockCommand supplied, aggregate state should be updated")
        void should_decrease_stock_amount_when_StockOpenedEvent_fired() {
            IncreaseStockCommand increaseStockCommand = new IncreaseStockCommand(5);
            stockAggregate.increaseStock(increaseStockCommand);
            assertEquals(15, stockAggregate.getAvailableInStock());
        }
    }

    @Nested
    @DisplayName("Close Stock Cases")
    class CloseStockCommandTests {

        @Test
        @DisplayName("when StockAggregate is not active, IllegalStateException should be thrown")
        void should_throw_IllegalStateException_when_StockAggregate_is_not_active() {
            stockAggregate.setActive(false);
            CloseStockCommand closeStockCommand = new CloseStockCommand(id);
            assertThrows(IllegalStateException.class, () -> stockAggregate.closeStock(closeStockCommand));
        }

        @Test
        @DisplayName("when CloseStockCommand supplied, StockClosedEvent should be fired")
        void should_StockClosedEvent_fired_when_CloseStockCommand_supplied() {
            CloseStockCommand closeStockCommand = new CloseStockCommand(id);
            stockAggregate.closeStock(closeStockCommand);
            assertAll(
                    () -> assertEquals(2, stockAggregate.getUnconsumedDomainEvents().size()),
                    () -> assertEquals(StockClosedEvent.class, stockAggregate.getUnconsumedDomainEvents().get(1).getClass()));
        }
    }
}

@Data
@SuperBuilder
class DummyEvent extends BaseEvent {

}