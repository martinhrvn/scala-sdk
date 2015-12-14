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

import com.ibm.watson.developercloud.utils.{JsonUtils, Validation, WatsonService, WatsonServiceConfig}
import com.typesafe.scalalogging.LazyLogging
import spray.client.pipelining._
import spray.http.{MultipartFormData, BodyPart, HttpResponse, Uri}

import spray.json.{JsObject, JsString, JsValue}
import LanguageTranslationProtocol._
import spray.httpx.SprayJsonSupport._
import scala.concurrent.Future

/**
  * The IBM Watson Language Translation service translate text from one language to another and
  * identifies the language in which text is written.
  *
  * @version v2
  * @param config service config
  */
class LanguageTranslation(config: WatsonServiceConfig ) extends WatsonService(config) with LazyLogging {
  import system.dispatcher

  val MODEL_URL  = "/v2/models"
  val PATH_MODEL = "/v2/models/%s"
  val PATH_IDENTIFY: String = "/v2/identify"
  val PATH_TRANSLATE: String = "/v2/translate"
  val PATH_IDENTIFIABLE_LANGUAGES: String = "/v2/identifiable_languages"
  val PATH_MODELS: String = "/v2/models"

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

    val map = collection.mutable.Map[String, String]()

    Option(source) match {
      case Some(src) if src.nonEmpty => map.put(LanguageTranslation.Source, src)
      case _ =>
    }

    Option(target) match {
      case Some(trg) if trg.nonEmpty => map.put(LanguageTranslation.Target, trg)
      case _ =>
    }

    Option(showDefault) match {
      case Some(showDef) => map.put(LanguageTranslation.Default, showDef.toString)
      case _ =>
    }

    getModels(map.toMap)
  }

  /**
    * Retrieves the list of models.
    * @param params list of parameters to search for models
    * @return future
    */
  def getModels(params: Map[String,String]) : Future[LanguageModels] = {
    logger.info("Entering getModels")

    val request = Get(Uri(config.endpoint + MODEL_URL).withQuery(params))
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
    var data: List[BodyPart] = List(BodyPart(options.baseModelId, formHeaders(LanguageTranslation.Name -> LanguageTranslation.BodyPart)))
    data = Option(options.forcedGlossary) match {
      case Some(glossary) => BodyPart(glossary, LanguageTranslation.ForcedGlossary) :: data
      case _ => data
    }

    data = Option(options.monlingualCorpus) match {
      case Some(corpus) => BodyPart(corpus, LanguageTranslation.MonolingualCorpus) :: data
      case _ => data
    }

    data = Option(options.parallelCorpus) match {
      case Some(parallelCorpus) => BodyPart(parallelCorpus, LanguageTranslation.ParallelCorpus) :: data
      case _ => data
    }

    data = Option(options.name) match {
      case Some(name) => BodyPart(name, LanguageTranslation.Name) :: data
      case _ => data
    }

    val formData = MultipartFormData(data)

    val request = Post(config.endpoint + PATH_MODELS, formData)
    send(request).map(unmarshal[LanguageModel])

  }

  /**
    * Deletes a model by ID
    * @param modelId identifier of model
    * @return future of HttpRequest
    */
  def deleteModel(modelId : String) : Future[HttpResponse] = {
    Validation.notEmpty(modelId, Validation.MessageNotEmpty.format(LanguageTranslation.ModelId))
    val request = Delete(config.endpoint + PATH_MODEL.format(modelId))
    send(request)
  }


  /**
    * Retrieves a list of identifiable languages
    * @return the list of identifiable languages
    */
  def getIdentifiableLanguages: Future[List[IdentifiableLanguage]] = {
    val request = Get(config.endpoint + PATH_IDENTIFIABLE_LANGUAGES)
    val response = send(request)
    response.map(unmarshal[List[IdentifiableLanguage]])
  }

  /**
    * Identifies language in which text is written.
    * @param text the text to identify
    * @return the identified language
    */
  def identify (text: String): Future[List[IdentifiedLanguage]] = {
    val request = Post(config.endpoint + PATH_IDENTIFY, text)
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
    Validation.notEmpty(modelId, Validation.MessageNotEmpty.format(LanguageTranslation.ModelId))
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
    Validation.notEmpty(source, Validation.MessageNotEmpty.format(LanguageTranslation.Source))
    Validation.notEmpty(target, Validation.MessageNotEmpty.format(LanguageTranslation.Target))
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
    Validation.notEmpty(text, Validation.MessageNotEmpty.format(LanguageTranslation.Text))
    var map: Map[String, JsValue] = Map(LanguageTranslation.Text -> JsString(text))
    map = JsonUtils.addIfNotEmpty(modelId, LanguageTranslation.ModelId, map)
    map = JsonUtils.addIfNotEmpty(source, LanguageTranslation.Source, map)
    map = JsonUtils.addIfNotEmpty(target, LanguageTranslation.Target, map)
    val jsonRequest = new JsObject(map)

    val response = send(Post(config.endpoint + PATH_TRANSLATE, jsonRequest.toString()))
    response.map(unmarshal[TranslationResult])
  }
}

object LanguageTranslation {
  val ModelId = "model_id"
  val Source = "source"
  val Target = "target"
  val Text = "text"
  val Name = "name"
  val ParallelCorpus = "parallel_corpus"
  val MonolingualCorpus = "monolingual_corpus"
  val ForcedGlossary = "forced_glossary"
  val BodyPart = "body_part"
  val Default = "default"
}
