# detekt custom rule template

This repository is a template. You can use it to generate your own repository to write and share your custom rules.

## How to use it

1. Create a new repository using this one as a template. [Click here][create_template]
2. Edit MyRule to fit your use case
3. Share your rule! You can upload your rule to [Maven Central][maven_central] if you want. If you don't want to do all
   the steps that Maven Central requires you can just share your rule using [jitpack][jitpack].
4. Extra: you can remove all this README and explain what your rule does and how to configure it.

## Documentation

You can find the documentation about how to write [custom rules here][custom_rule_documentation].

## Note

- Remember that, by default, all rules are disabled. To configure your rules edit the file in
`src/main/resources/config/config.yml`.
- Once you have your rules ready you can publish them on the [detekt's marketplace][detekt_marketplace] to improve the discoverability. To do so create a PR editing [this file][detekt_marketplace_edit].

[create_template]: https://github.com/detekt/detekt-custom-rule-template/generate

[maven_central]: https://search.maven.org/

[custom_rule_documentation]: https://detekt.github.io/detekt/extensions.html

[jitpack]: https://jitpack.io/

[detekt_marketplace]: https://detekt.dev/marketplace
[detekt_marketplace_edit]: https://github.com/detekt/detekt/blob/main/website/src/data/marketplace.js
