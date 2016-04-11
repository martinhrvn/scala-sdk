package com.ibm.watson.developer_cloud.language_translation.v2.model

import com.ibm.watson.developer_cloud.language_translation.v2.model.IdentifiableLanguage._
import spray.json.{RootJsonFormat, DefaultJsonProtocol}

/**
  * Created by Martin Harvan (martin.harvan@sk.ibm.com) on 20/03/16.
  */
object LanguageTranslationProtocol extends DefaultJsonProtocol {
  implicit val identifiableLanguage : RootJsonFormat[IdentifiableLanguage] = jsonFormat(IdentifiableLanguage.apply, "language", "name")
  implicit val identifiedLanguage: RootJsonFormat[IdentifiedLanguage] = jsonFormat(IdentifiedLanguage, "language", "confidence")
  implicit val languageModel: RootJsonFormat[LanguageModel] = jsonFormat(LanguageModel, "model_id", "source", "target", "base_model_id", "domain",
                                          "customizable", "default_model", "owner", "status", "name")
  implicit val translation: RootJsonFormat[Translation] = jsonFormat(Translation, "translation")
  implicit val models: RootJsonFormat[LanguageModels] = jsonFormat(LanguageModels, "models")
  implicit val translationResult: RootJsonFormat[TranslationResult] = jsonFormat(TranslationResult, "translations", "word_count", "character_count")
}
