// Copyright (C) 2015 IBM Corp. All Rights Reserved.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.ibm.watson.developercloud.language_translation.v2

import java.io.File

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

