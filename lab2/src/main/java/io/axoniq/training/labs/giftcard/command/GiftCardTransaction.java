package io.axoniq.training.labs.giftcard.command;

import io.axoniq.training.labs.giftcard.coreapi.CardReimbursedEvent;
import io.axoniq.training.labs.giftcard.coreapi.ReimburseCardCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.EntityId;

import java.util.Objects;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

public class GiftCardTransaction {

    // Required for a list of aggregate members
    @EntityId
    private final String transactionId;
    private final int transactionValue;
    private boolean reimbursed = false;

    public GiftCardTransaction(String transactionId, int transactionValue) {
        this.transactionId = transactionId;
        this.transactionValue = transactionValue;
    }

    public String getTransactionId() { return transactionId; }

    @CommandHandler
    public void handle(ReimburseCardCommand command) {
        throwErrorIfTransactionAlreadyReimbursed();
        apply(new CardReimbursedEvent(command.getCardId(), transactionId, transactionValue));
    }

    @EventSourcingHandler
    public void on(CardReimbursedEvent event) {
        // no longer needed due to
        // @AggregateMember(eventForwardingMode = ForwardMatchingInstances.class)
        // in GiftCard.java
        // if (!transactionId.equals(event.getTransactionId())) {
        //     return;
        // }

        reimbursed = true;
    }

    private void throwErrorIfTransactionAlreadyReimbursed() {
        if (reimbursed) {
            throw new IllegalStateException("Transaction already reimbursed");
        }
    }

    // Need to have these overrides for list aggregate members for testing.
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GiftCardTransaction that = (GiftCardTransaction) o;
        return transactionValue == that.transactionValue &&
                reimbursed == that.reimbursed &&
                Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, transactionValue, reimbursed);
    }
}
