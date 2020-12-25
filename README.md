# Scientist

Minimalistic experimentation library for Java inspired by [GitHub Scientist](https://github.com/github/scientist).

The following code sets up an experiment.

```java

  Science science = new Science();
  Experiment experiment = science.experiment("sample_experiment");

  Supplier<ReturnType> controlFunction = functionYouWouldLikeToRefactor;
  Supplier<ReturnType> candidateFunction = refactoredFunction;

  ReturnType resultFromControlFunction = experiment.run(controlFunction, candidateFunction);

```

Setup the `INFO` log level for `io.github.ilyazinkovich.scientist.ExperimentResultsLogger`.

```xml

<logger name="io.github.ilyazinkovich.scientist.ExperimentResultsLogger" level="INFO" />

```

And observe experiment aggregate results logged each 5 minutes in the following format (in one line):

```js

{
  "experiment_name": "sample_experiment",
  "results": {
    "results_match": 31,
    "results_do_not_match": 5,
    "candidate_failed": 2
  }
}

```
