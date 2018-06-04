package io.github.ilyazinkovich.scientist;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Experiment {

  private final Consumer<Boolean> resultsConsumer;

  public Experiment(final Consumer<Boolean> resultsConsumer) {
    this.resultsConsumer = resultsConsumer;
  }

  public <T> T run(final Supplier<T> control, final Supplier<T> candidate) {
    final T controlResult = control.get();
    final T candidateResult = candidate.get();
    final boolean experimentResult = matchResults(controlResult, candidateResult);
    resultsConsumer.accept(experimentResult);
    return controlResult;
  }

  public <T> T runAsync(final Supplier<T> control, final Supplier<T> candidate) {
    final T controlResult = control.get();
    CompletableFuture.supplyAsync(candidate)
        .thenApply(candidateResult -> matchResults(controlResult, candidateResult))
        .thenAccept(resultsConsumer);
    return controlResult;
  }

  private <T> boolean matchResults(final T controlResult, final T candidateResult) {
    return Optional.ofNullable(controlResult)
        .map(result -> result.equals(candidateResult))
        .orElse(false);
  }
}
