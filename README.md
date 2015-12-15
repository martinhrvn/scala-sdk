# Watson Developer Cloud Scala SDK
[![Build Status](https://travis-ci.org/kane77/watson-scala-wrapper.svg)](https://travis-ci.org/kane77/watson-scala-wrapper)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/9f621de87f474787a7abd734deb30148)](https://www.codacy.com/app/martinhrvn/watson-scala-wrapper)
[![Codacy Badge](https://api.codacy.com/project/badge/coverage/9f621de87f474787a7abd734deb30148)](https://www.codacy.com/app/martinhrvn/watson-scala-wrapper)

Scala client library to use the [Watson Developer Cloud][wdc] services, a collection of REST
APIs and SDKs that use cognitive computing to solve complex problems.


## Table of Contents
  * [Installation](#installation)
  * [Usage](#usage)
  * [Getting the Service Credentials](#getting-the-service-credentials)
  * [Questions](#questions)
  * [Examples](#examples)
  * [IBM Watson Services](#ibm-watson-services)
    * [Alchemy Language](#alchemy-language)
    * [Alchemy Vision](#alchemy-vision)
    * [Alchemy Data News](#alchemy-data-news)
    * [Concept Insights](#concept-insights)
    * [Dialog](#dialog)
    * [Document Conversion](#document-conversion)
    * [Language Identification](#language-identification)
    * [Language Translation](#language-translation)
    * [Machine Translation](#machine-translation)
    * [Natural Language Classifier](#natural-language-classifier)
    * [Personality Insights](#personality-insights)
    * [Relationship Extraction](#relationship-extraction)
    * [Retrieve and Rank](#retrieve-and-rank)
    * [Speech to Text](#speech-to-text)
    * [Text to Speech](#text-to-speech)
    * [Tone Analyzer](#tone-analyzer)
    * [Tradeoff Analytics](#tradeoff-analytics)
    * [Visual Insights](#visual-insights)
    * [Visual Recognition](#visual-recognition)
  * [Running in Bluemix](#running-in-bluemix)
  * [Build + Test](#build--test)
  * [Eclipse and Intellij](#working-with-eclipse-and-intellij-idea)
  * [Open Source @ IBM](#open-source--ibm)
  * [License](#license)
  * [Contributing](#contributing)

## Installation


## Usage

The examples below assume that you already have service credentials. If not,
you will have to create a service in [Bluemix][bluemix].

If you are running your application in Bluemix, you don't need to specify the
credentials; the library will get them for you by looking at the `VCAP_SERVICES` environment variable.

## Getting the Service Credentials
You will need the `username` and `password` (`api_key` for AlchemyAPI) credentials for each service. Service credentials are different from your Bluemix account username and password.

To get your service credentials, follow these steps:
 1. Log in to Bluemix at https://bluemix.net.

 1. Create an instance of the service:
     1. In the Bluemix **Catalog**, select the service you want to use.
     1. Under **Add Service**, type a unique name for the service instance in the Service name field. For example, type `my-service-name`. Leave the default values for the other options.
     1. Click **Create**.

 1. Copy your credentials:
     1. On the left side of the page, click **Service Credentials** to view your service credentials.
     1. Copy `username` and `password`(`api_key` for AlchemyAPI).

Once you have credentials, copy config.properties.example to src/test/resources/config.properties, and fill them in as necessary.

To use manual username and password

## Questions

If you are having difficulties using the APIs or have a question about the IBM
Watson Services, please ask a question on
[dW Answers](https://developer.ibm.com/answers/questions/ask/?topics=watson)
or [Stack Overflow](http://stackoverflow.com/questions/ask?tags=ibm-watson).


## IBM Watson Services
The Watson Developer Cloud offers a variety of services for building cognitive
applications.

### Language Translation
Select a domain, then identify or select the language of text, and then translate the text from one supported language to another.  
Example: Translate 'hello' from English to Spanish using the [Language Translation][language_translation] service.

```scala
val service = new LanguageTranslation(new VCAPServicesConfig());

val translationResult = service.translate("hello", "en", "es");
```

### Natural Language Classifier
Use [Natural Language Classifier](http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/nl-classifier/) service to create a classifier instance by providing a set of representative strings and a set of one or more correct classes for each as training. Then use the trained classifier to classify your new question for best matching answers or to retrieve next actions for your application.

```scala
val service = new NaturalLanguageClassifier(new VCAPServicesConfig());

val classification = service.classify("<classifier-id>", "Is it sunny?");
```

**Note:** You will need to create and train a classifier in order to be able to classify phrases.


### Personality Insights
Use linguistic analytics to infer personality and social characteristics, including Big Five, Needs, and Values, from text.  
Example: Analyze text and get a personality profile using the [Personality Insights][personality_insights] service.

```scala
val service = new PersonalityInsights(new VCAPServicesConfig());

// Demo content from Moby Dick by Hermann Melville (Chapter 1)
val text = "Call me Ishmael. Some years ago-never mind how long precisely-having "
    + "little or no money in my purse, and nothing particular to interest me on shore, "
    + "I thought I would sail about a little and see the watery part of the world. "
    + "It is a way I have of driving off the spleen and regulating the circulation. "
    + "Whenever I find myself growing grim about the mouth; whenever it is a damp, "
    + "drizzly November in my soul; whenever I find myself involuntarily pausing before "
    + "coffin warehouses, and bringing up the rear of every funeral I meet; and especially "
    + "whenever my hypos get such an upper hand of me, that it requires a strong moral "
    + "principle to prevent me from deliberately stepping into the street, and methodically "
    + "knocking people's hats off-then, I account it high time to get to sea as soon as I can. "
    + "This is my substitute for pistol and ball. With a philosophical flourish Cato throws himself "
    + "upon his sword; I quietly take to the ship. There is nothing surprising in this. "
    + "If they but knew it, almost all men in their degree, some time or other, cherish "
    + "very nearly the same feelings towards the ocean with me. There now is your insular "
    + "city of the Manhattoes, belted round by wharves as Indian isles by coral reefs-commerce surrounds "
    + "it with her surf. Right and left, the streets take you waterward.";

val profile = service.getProfile(text);
```

**Note:** Don't forget to update the `text` variable! Also, if you experience
authentication errors, remember that the Personality Insights service is not
a free service.

### Visual Insights
Use the [Visual Insights][visual_insights] to get insight into the themes present in a collection of images based on their visual appearance/content.

```scala
val service = new VisualInsights(new VCAPServicesConfig());

val images = new File("src/test/resources/images.zip");
val summary = service.getSummary(images);
```

## Running in Bluemix
When running in Bluemix, the library will automatically get the credentials from `VCAP_SERVICES`.
If you have more than one plan, you can use `BluemixUtils` to get the service credentials for an specific plan.

To use VCAP_SERVICES values, use `VCAPServicesConfig` as parameter to service constructor

```scala
val service = new PersonalityInsights(new VCAPServicesConfig());
```

## Build + Test

To build and test the project you can use [Gradle][] (version 2.x): or [SBT][sbt].

Gradle:

  ```sh
  $ cd scala-sdk
  $ gradle jar  # build jar file (build/libs/watson-developer-cloud-2.1.0.jar)
  $ gradle test # run tests
  ```

or SBT:

  ```sh
  $ sbt package
  ```
SBT is the preferred way to build the project.

## Open Source @ IBM
Find more open source projects on the [IBM Github Page](http://ibm.github.io/)

## License

This library is licensed under Apache 2.0. Full license text is
available in [LICENSE](LICENSE).

[personality_insights]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/personality-insights/
[language_identification]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/lidapi/
[machine_translation]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/mtapi/
[document_conversion]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/document-conversion/
[relationship_extraction]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/sireapi/
[language_translation]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/language-translation/
[visual_recognition]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/visual-recognition/
[tradeoff_analytics]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/tradeoff-analytics/
[text_to_speech]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/text-to-speech/
[speech_to_text]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/speech-to-text/
[tone-analyzer]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/tone-analyzer/
[dialog]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/dialog/
[concept-insights]: https://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/concept-insights/
[visual_insights]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/visual-insights/
[retrieve_and_rank]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/retrieve-rank/

[alchemy_language]: http://www.alchemyapi.com/products/alchemylanguage
[sentiment_analysis]: http://www.alchemyapi.com/products/alchemylanguage/sentiment-analysis
[alchemy_vision]: http://www.alchemyapi.com/products/alchemyvision
[alchemy_data_news]: http://www.alchemyapi.com/products/alchemydata-news

[wdc]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/
[bluemix]: https://console.ng.bluemix.net
[Gradle]: http://www.gradle.org/
[OkHttp]: http://square.github.io/okhttp/
[gson]: https://github.com/google/gson
[apache_maven]: http://maven.apache.org/
