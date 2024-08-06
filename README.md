# Hbmartin's detekt rules

[![Join the chat at #detekt on KotlinLang](https://img.shields.io/badge/%23detekt-slack-red.svg?logo=slack)](https://kotlinlang.slack.com/archives/C88E12QH4)
[![PR Checks](https://github.com/hbmartin/hbmartin-detekt-rules/actions/workflows/pre-merge.yml/badge.svg)](https://github.com/hbmartin/hbmartin-detekt-rules/actions/workflows/pre-merge.yml)
[![codecov](https://codecov.io/github/hbmartin/hbmartin-detekt-rules/branch/main/graph/badge.svg?token=5CIMCMO3K3)](https://codecov.io/github/hbmartin/hbmartin-detekt-rules)
[![CodeFactor](https://www.codefactor.io/repository/github/hbmartin/hbmartin-detekt-rules/badge)](https://www.codefactor.io/repository/github/hbmartin/hbmartin-detekt-rules)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=hbmartin_hbmartin-detekt-rules&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=hbmartin_intellij-build-webhook-notifier)
[![Release](https://jitpack.io/v/hbmartin/hbmartin-detekt-rules.svg)](https://jitpack.io/#hbmartin/hbmartin-detekt-rules)


These are my opinions. There are many like them but these are mine. 😄

## Quick Start

Inside of your `dependencies` block add the following: (for more details see [adding more rule sets](https://github.com/detekt/detekt#adding-more-rule-sets))
```kotlin 
detektPlugins("com.github.hbmartin:hbmartin-detekt-rules:0.1.5")
```

Then add to your detekt configuration as in the section below to activate rules. Note that the AvoidFirstOrLastOnList and AvoidMutableCollections rules require [type resolution](https://detekt.dev/docs/gettingstarted/type-resolution) to be active.

## Configuration

Add below to your `detekt.yml` and modify to suit your needs. Only the `AvoidVarsExceptWithDelegate` rule has additional configuration options, where you may provide a regex to allow additional variable delegates. 

```yaml
HbmartinRuleSet:
  AvoidFirstOrLastOnList:
    active: true
  AvoidMutableCollections:
    active: true
  AvoidVarsExceptWithDelegate:
    active: true
    allowedDelegates:
      - 'remember\w*'
      - 'mutableState\w*'
  DontForceCast:
    active: true
  MutableTypeShouldBePrivate:
    active: true
  NoCallbacksInFunctions:
    active: true
    ignoreAnnotated: ['Composable']
    allowExtensions: true
    allowReceivers: true
  NoNotNullOperator:
    active: true
  NoVarsInConstructor:
    active: true
  WhenBranchSingleLineOrBraces:
    active: true
```

## Rules

### AvoidFirstOrLastOnList

Finds uses of `.first()` or `.list()` on a list type. These are dangerous calls since they will throw a `NoSuchElementException ` exception  if the list is empty. Prefer to use .firstOrNull() or .lastOrNull() instead. [See here](https://github.com/hbmartin/hbmartin-detekt-rules/blob/main/src/test/kotlin/me/haroldmartin/detektrules/AvoidFirstOrLastOnListTest.kt) for triggering and non-triggering examples. 

### AvoidMutableCollections

Finds uses of mutable collections e.g. `MutableList<>`. These are highly likely to lead to bugs, prefer to use functional patterns to create new lists modified as needed. [See here](https://github.com/hbmartin/hbmartin-detekt-rules/blob/main/src/test/kotlin/me/haroldmartin/detektrules/AvoidMutableCollectionsTest.kt) for triggering and non-triggering examples.

### AvoidVarsExceptWithDelegate

Finds uses of mutable `var` fields. These are highly likely to lead to bugs, prefer to use a `Flow` or some reactive type for any mutable state. There is an exception made for `var`s which are implemented with the [delegation pattern](https://kotlinlang.org/docs/delegation.html), which is particularly common when using Compose. [See here](https://github.com/hbmartin/hbmartin-detekt-rules/blob/main/src/test/kotlin/me/haroldmartin/detektrules/AvoidVarsExceptWithDelegateTest.kt) for triggering and non-triggering examples.

### DontForceCast

Finds uses of `as` to force cast. These are likely to lead to crashes, especially in unforeseen circumstances, prefer to safely cast with `as?` instead. [See here](https://github.com/hbmartin/hbmartin-detekt-rules/blob/main/src/test/kotlin/me/haroldmartin/detektrules/DontForceCastTest.kt) for triggering and non-triggering examples.

### MutableTypeShouldBePrivate

Finds publicly exposed mutable types e.g. `MutableStateFlow<>`. These are likely to lead to bugs, prefer to expose a non-mutable `Flow` (e.g. with `_mutableStateFlow.asStateFlow()`) or other non-mutable type. [See here](https://github.com/hbmartin/hbmartin-detekt-rules/blob/main/src/test/kotlin/me/haroldmartin/detektrules/MutableTypeShouldBePrivateTest.kt) for triggering and non-triggering examples.

### NoCallbacksInFunctions
Finds uses of callbacks in functions. This can lead to a mixed concurrency paradigm and are likely to lead to bugs or stalled threads, prefer to use a suspend function instead. Use the `ignoreAnnotated` configuration to allow callbacks in `@Composable` functions. [See here](https://github.com/hbmartin/hbmartin-detekt-rules/blob/main/src/test/kotlin/me/haroldmartin/detektrules/NoCallbacksInFunctionsTest.kt) for triggering and non-triggering examples.

### NoNotNullOperator

Finds uses of `!!` to force unwrap. These are likely to lead to crashes, prefer to safely unwrap with `?.` or `?:` instead. Otherwise the Kotlin docs will make fun of you for being an [NPE lover](https://kotlinlang.org/docs/null-safety.html#the-operator). [See here](https://github.com/hbmartin/hbmartin-detekt-rules/blob/main/src/test/kotlin/me/haroldmartin/detektrules/NoNotNullOperatorTest.kt) for triggering and non-triggering examples.

### NoVarsInConstructor

Finds uses of `var` in a constructor. These are likely to lead to bugs, always use `val` instead. [See here](https://github.com/hbmartin/hbmartin-detekt-rules/blob/main/src/test/kotlin/me/haroldmartin/detektrules/NoVarsInConstructorTest.kt) for triggering and non-triggering examples.

### WhenBranchSingleLineOrBraces

A stylistic rule that require that either a when expression be on a single line or use braces. Either case should have a single space after the arrow. [See here](https://github.com/hbmartin/hbmartin-detekt-rules/blob/main/src/test/kotlin/me/haroldmartin/detektrules/WhenBranchSingleLineOrBracesTest.kt) for triggering and non-triggering examples.

## Contributing

* Jump in and modify this project! Start by cloning it with `git clone git@github.com:hbmartin/hbmartin-detekt-rules.git`, then open it in IntelliJ and run the tests.
* Read the [detekt documentation](https://detekt.dev/docs/introduction/extensions/) to learn more about how to write rules.
* [PRs](https://github.com/hbmartin/hbmartin-detekt-rules/pulls) and [bug reports / feature requests](https://github.com/hbmartin/hbmartin-detekt-rules/issues) are all welcome!
* Checked with detekt, including the ruleauthors set and, of course, [running these rules on itself](https://github.com/hbmartin/hbmartin-detekt-rules/blob/main/build.gradle.kts#L20) 😏
* Treat other people with helpfulness, gratitude, and consideration! See the [JetBrains CoC](https://confluence.jetbrains.com/display/ALL/JetBrains+Open+Source+and+Community+Code+of+Conduct)

## Authors

* [Harold Martin](https://www.linkedin.com/in/harold-martin-98526971/) - harold.martin at gmail
* Significant inspiration from [kure-potlin by neeffect](https://github.com/neeffect/kure-potlin) and [Doist detekt-rules](https://github.com/Doist/detekt-rules)

