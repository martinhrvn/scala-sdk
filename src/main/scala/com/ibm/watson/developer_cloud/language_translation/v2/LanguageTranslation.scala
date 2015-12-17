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
package com.ibm.watson.developer_cloud.language_translation.v2

import com.ibm.watson.developer_cloud.utils.{Validation, WatsonService, WatsonServiceConfig}
import com.typesafe.scalalogging.LazyLogging
import spray.client.pipelining._
import spray.http.{MultipartFormData, BodyPart, HttpResponse, Uri}

import spray.json.{JsObject, JsString}
import LanguageTranslationProtocol._
import spray.httpx.SprayJsonSupport._
import scala.concurrent.{ExecutionContextExecutor, Future}
import LanguageTranslation._

/**
  * The IBM Watson Language Translation service translate text from one language to another and
  * identifies the language in which text is written.
  *
  * @version v2
  * @param config service config
  */
class LanguageTranslation(config: WatsonServiceConfig ) extends WatsonService(config) with LazyLogging {


  def serviceType : String = "language_translation"

  /**
    * Retrieves a list of models
    * @return future of language models
    */
  def getModels : Future[LanguageModels] = getModels(Map())

  /**
    * Retrieves a translation models.
    * @param showDefault show default models
    * @param source the source language
    * @param target the target language
    * @return the translation models
    */
  def getModels(showDefault: Boolean, source: String, target: String) : Future[LanguageModels] = {

    val map = Option(source).map(LanguageTranslation.source -> _).toMap ++
    Option(target).map(LanguageTranslation.target -> _).toMap ++
    Option(showDefault).map(LanguageTranslation.default -> _.toString)

    getModels(map.toMap)
  }

  /**
    * Retrieves the list of models.
    * @param params list of parameters to search for models
    * @return future
    */
  def getModels(params: Map[String,String]) : Future[LanguageModels] = {
    logger.info("Entering getModels")

    val request = Get(Uri(config.endpoint + modelsPath).withQuery(params))
    val response: Future[HttpResponse] = send(request)
    logger.info("Returning response")
    response.map(unmarshal[LanguageModels])
  }

  /**
    * Creates a translation models.
    *
    * @param options the create model options
    * @return the translation model
    */
  def createModel(options: CreateModelOptions) : Future[LanguageModel] = {
    val data: List[BodyPart] = List(BodyPart(options.baseModelId, formHeaders(LanguageTranslation.name -> LanguageTranslation.bodyPart))) ++
    Option(options.forcedGlossary).map(BodyPart(_, LanguageTranslation.forcedGlossary)).toList ++
    Option(options.monlingualCorpus).map(BodyPart(_, LanguageTranslation.monolingualCorpus)).toList ++
    Option(options.parallelCorpus).map(BodyPart(_, LanguageTranslation.parallelCorpus)).toList ++
    Option(options.name).map(BodyPart(_, LanguageTranslation.name)).toList

    val formData = MultipartFormData(data)

    val request = Post(config.endpoint + modelsPath, formData)
    send(request).map(unmarshal[LanguageModel])

  }

  /**
    * Deletes a model by ID
    * @param modelId identifier of model
    * @return future of HttpRequest
    */
  def deleteModel(modelId : String) : Future[HttpResponse] = {
    Validation.notEmpty(modelId, Validation.messageNotEmpty.format(LanguageTranslation.modelId))
    val request = Delete(config.endpoint + modelPath.format(modelId))
    send(request)
  }


  /**
    * Retrieves a list of identifiable languages
    * @return the list of identifiable languages
    */
  def getIdentifiableLanguages: Future[List[IdentifiableLanguage]] = {
    val request = Get(config.endpoint + identifiableLanguagePath)
    val response = send(request)
    response.map(unmarshal[List[IdentifiableLanguage]])
  }

  /**
    * Identifies language in which text is written.
    * @param text the text to identify
    * @return the identified language
    */
  def identify (text: String): Future[List[IdentifiedLanguage]] = {
    val request = Post(config.endpoint + identifyPath, text)
    val response = send(request)
    response.map(unmarshal[List[IdentifiedLanguage]])
  }

  /**
    * Translates text using model
    * @param text text to translate
    * @param modelId the model ID
    * @return translation result
    */
  def translate(text: String, modelId: Option[String]): Future[TranslationResult] = {
    Validation.notEmpty(modelId, Validation.messageNotEmpty.format(LanguageTranslation.modelId))
    translateRequest(text, modelId, None, None)
  }

  /**
    * Translate text using source and target languages
    * @param text text to translate
    * @param source source language
    * @param target target language
    * @return translated text
    */
  def translate(text: String, source: Option[String], target: Option[String]) : Future[TranslationResult]= {
    Validation.notEmpty(source, Validation.messageNotEmpty.format(LanguageTranslation.source))
    Validation.notEmpty(target, Validation.messageNotEmpty.format(LanguageTranslation.target))
    translateRequest(text, None, source, target)
  }

  /**
    * Translate paragraphs of text using a model and or source and target. model_id or source and
    * target needs to be specified. If both are specified, then only model_id will be used
    *
    * @param text text to translate
    * @param modelId id of the model
    * @param source source language
    * @param target target language
    * @return translated text
    */
  def translateRequest(text: String, modelId: Option[String], source: Option[String], target: Option[String]) : Future[TranslationResult] = {
    Validation.notEmpty(text, Validation.messageNotEmpty.format(LanguageTranslation.text))
    val map = Option(text).map(LanguageTranslation.text -> JsString(_)).toMap ++
    modelId.map(LanguageTranslation.modelId -> JsString(_)).toMap ++
    source.map(LanguageTranslation.source -> JsString(_)).toMap ++
    target.map(LanguageTranslation.target -> JsString(_)).toMap
    val jsonRequest = new JsObject(map)

    val response = send(Post(config.endpoint + translatePath, jsonRequest.toString()))
    response.map(unmarshal[TranslationResult])
  }
}

object LanguageTranslation {
  val modelId = "model_id"
  val source = "source"
  val target = "target"
  val text = "text"
  val name = "name"
  val parallelCorpus = "parallel_corpus"
  val monolingualCorpus = "monolingual_corpus"
  val forcedGlossary = "forced_glossary"
  val bodyPart = "body_part"
  val default = "default"
  val modelsPath  = "/v2/models"
  val modelPath = "/v2/models/%s"
  val identifyPath: String = "/v2/identify"
  val translatePath: String = "/v2/translate"
  val identifiableLanguagePath: String = "/v2/identifiable_languages"
}
