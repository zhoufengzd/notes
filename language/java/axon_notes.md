# axon notes

## Aggregate
* Aggregate: tree of entities to handle commands, business rules
    * event identifier: @AggregateIdentifier
    * jpa identifier: @Id
    * @CommandHandler public void on(Create... / Update...)
    * @EventSourcingHandler public void on(...Created / ...Updated) // handle its own event

## Sagas
* a long-running business process

## axon command handler
```
LockingRepository::doCreateNew
AbstractRepository::newInstance(factoryMethod){
    // Saving an aggregate must occur before publishing its events
    uow.onPrepareCommit(x -> prepareForCommit(aggregateReference.get()));
    aggregate = doCreateNew(factoryMethod);
    return aggregate;
}
AggregateConstructorCommandHandler::handle() {
    // repository: EventSourcingRepo
    // handler: Avon MethodCommandHandler -> delegate, payload
    aggregate = repository.newInstance(() -> (T) handler.handle(command, null));
    return resolveReturnValue(command, aggregate);
}
axonframework..AggregateAnnotationCommandHandler::handle(commandMessage) {
    return handlers.get(commandMessage.getCommandName()).handle(commandMessage);
}
```