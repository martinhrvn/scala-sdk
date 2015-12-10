package com.ibm.watson.developercloud.language_translation.v2

import java.io.File

import com.ibm.watson.developercloud.language_classification.v1.LanguageIdentificationProtocol._
import spray.json.DefaultJsonProtocol

case class IdentifiableLanguage(var language: String, var name: String)

case class IdentifiedLanguage(var language: String, var confidence: Double)

case class LanguageModel(var modelId : String, var source: String, var target: String, baseModelId: String,
                          var domain: String, var customizable: Boolean, var defaultModel: Boolean, var owner: String,
                          var status: String, var name: String)
case class LanguageModels(var models: List[LanguageModel])
case class Translation(var translation: String)

case class TranslationResult(var translations: List[Translation], var wordCount: Int, var characterCount: Int)

object LanguageTranslationProtocol extends DefaultJsonProtocol {
  implicit val identifiableLanguage = jsonFormat(IdentifiableLanguage, "language", "name")
  implicit val identifiedLanguage = jsonFormat(IdentifiedLanguage, "language", "confidence")
  implicit val languageModel = jsonFormat(LanguageModel, "model_id", "source", "target", "base_model_id", "domain",
                                          "customizable", "default_model", "owner", "status", "name")
  implicit val translation = jsonFormat(Translation, "translation")
  implicit val models = jsonFormat(LanguageModels, "models")
  implicit val translationResult = jsonFormat(TranslationResult, "translations", "word_count", "character_count")
}

case class CreateModelOptions(baseModelId: String, forcedGlossary: File, monlingualCorpus: File, name: String, parallelCorpus: File)

