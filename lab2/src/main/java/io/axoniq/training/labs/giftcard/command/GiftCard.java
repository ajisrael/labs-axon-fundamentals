package io.axoniq.training.labs.giftcard.command;

import io.axoniq.training.labs.giftcard.coreapi.CardIssuedEvent;
import io.axoniq.training.labs.giftcard.coreapi.CardRedeemedEvent;
import io.axoniq.training.labs.giftcard.coreapi.CardReimbursedEvent;
import io.axoniq.training.labs.giftcard.coreapi.IssueCardCommand;
import io.axoniq.training.labs.giftcard.coreapi.RedeemCardCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.modelling.command.ForwardMatchingInstances;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.ArrayList;
import java.util.List;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class GiftCard {

    @AggregateIdentifier
    private String id;

    // What does eventForwardingMode do?
    // Removes the need to check that an event is being handled by the corresponding object with a matching entity id
    @AggregateMember(eventForwardingMode = ForwardMatchingInstances.class)
    private List<GiftCardTransaction> transactions;

    private int remainingValue;

    @CommandHandler
    public GiftCard(IssueCardCommand command) {
        throwErrorIfAmountIsLessThanOrEqualToZero(command.getAmount());

        apply(new CardIssuedEvent(command.getCardId(), command.getAmount()));
    }

    @CommandHandler
    public void handle(RedeemCardCommand command) {
        throwErrorIfAmountIsLessThanOrEqualToZero(command.getAmount());
        throwErrorIfAmountIsGreaterThanRemainingValue(command.getAmount());
        throwErrorIfTransactionIdIsNotUnique(command.getTransactionId());

        apply(new CardRedeemedEvent(id, command.getTransactionId(), command.getAmount()));
    }

    // Question: Why no command handler for ReimburseCardCommand in solution?
    // Answer: It exists in the GiftCardTransaction class.

    @EventSourcingHandler
    public void on(CardIssuedEvent event) {
        id = event.getCardId();
        remainingValue = event.getAmount();
        transactions = new ArrayList<>();
    }

    @EventSourcingHandler
    public void on(CardRedeemedEvent event) {
        remainingValue -= event.getAmount();
        transactions.add(new GiftCardTransaction(event.getTransactionId(), event.getAmount()));
    }

    @EventSourcingHandler
    public void on(CardReimbursedEvent event) {
        remainingValue += event.getAmount();
    }

    private void throwErrorIfAmountIsLessThanOrEqualToZero(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount <= 0");
        }
    }

    private void throwErrorIfAmountIsGreaterThanRemainingValue(int amount) {
        if (amount > remainingValue) {
            throw new IllegalStateException("amount > remaining value");
        }
    }

    private void throwErrorIfTransactionIdIsNotUnique(String transactionId) {
        if (transactions.stream()
                .map(GiftCardTransaction::getTransactionId)
                .anyMatch(transactionId::equals)) {
            throw new IllegalStateException("TransactionId must be unique");
        }
    }

    public GiftCard() {
        // Required by Axon
    }
}
