package com.lament.z.omni.mhub.service.handler;

import java.util.function.Supplier;

import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;


/**
 * A Transaction Handler
 */
@Service
public class TransactionHandler {

	private final TransactionalOperator transactionalOperator;

	public TransactionHandler(ReactiveTransactionManager transactionManager) {
		this.transactionalOperator = TransactionalOperator.create(transactionManager);
	}


	public <T> Mono<T> runWithTransaction(Supplier<Mono<T>> supplier) {
		return transactionalOperator.execute(status -> supplier.get())
				.then(Mono.empty());
	}

}
