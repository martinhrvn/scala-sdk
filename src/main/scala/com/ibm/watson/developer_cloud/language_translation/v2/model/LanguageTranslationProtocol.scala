package com.ibm.watson.developer_cloud.language_translation.v2.model

import spray.json.DefaultJsonProtocol

/**
  * Created by Martin Harvan (martin.harvan@sk.ibm.com) on 20/03/16.
  */
object LanguageTranslationProtocol extends DefaultJsonProtocol {
  implicit val identifiableLanguage = jsonFormat(IdentifiableLanguage, "language", "name")
  implicit val identifiedLanguage = jsonFormat(IdentifiedLanguage, "language", "confidence")
  implicit val languageModel = jsonFormat(LanguageModel, "model_id", "source", "target", "base_model_id", "domain",
                                          "customizable", "default_model", "owner", "status", "name")
  implicit val translation = jsonFormat(Translation, "translation")
  implicit val models = jsonFormat(LanguageModels, "models")
  implicit val translationResult = jsonFormat(TranslationResult, "translations", "word_count", "character_count")
}
