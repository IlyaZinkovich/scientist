package io.github.ilyazinkovich.scientist;

import static io.github.ilyazinkovich.scientist.Outcome.CANDIDATE_FAILED;
import static io.github.ilyazinkovich.scientist.Outcome.RESULTS_DO_NOT_MATCH;
import static io.github.ilyazinkovich.scientist.Outcome.RESULTS_MATCH;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Experiment {

  private final Consumer<Outcome> outcomeConsumer;

  public Experiment(final Consumer<Outcome> outcomeConsumer) {
    this.outcomeConsumer = outcomeConsumer;
  }

  public <T> T run(final Supplier<T> control, final Supplier<T> candidate) {
    final T controlResult = control.get();
    CompletableFuture.supplyAsync(candidate)
        .thenApply(candidateResult -> matchResults(controlResult, candidateResult))
        .exceptionally(exception -> CANDIDATE_FAILED)
        .thenAccept(outcomeConsumer);
    return controlResult;
  }

  private <T> Outcome matchResults(final T controlResult, final T candidateResult) {
    return Optional.ofNullable(controlResult)
        .filter(result -> result.equals(candidateResult))
        .map(result -> RESULTS_MATCH)
        .orElse(RESULTS_DO_NOT_MATCH);
  }
}
