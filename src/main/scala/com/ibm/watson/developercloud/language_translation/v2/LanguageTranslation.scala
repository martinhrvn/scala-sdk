package com.ibm.watson.developercloud.language_translation.v2

import java.io.{BufferedInputStream, FileInputStream}
import java.util.concurrent.{Executors, ExecutorService}

import LanguageTranslationProtocol._
import com.ibm.watson.developercloud.utils.{JsonUtils, Validation, WatsonServiceConfig, WatsonService}
import com.typesafe.scalalogging.LazyLogging
import spray.can.Http
import spray.client.pipelining._
import spray.client._
import spray.http.{BasicHttpCredentials, Uri, HttpRequest}
import spray.http.Uri.Query
import spray.httpx.unmarshalling._
import spray.io.ClientSSLEngineProvider
import spray.json.{JsObject, JsString, JsValue}
import spray.httpx.SprayJsonSupport._

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

import akka.actor.ActorSystem
import akka.util.Timeout
import akka.pattern.ask
import akka.io.IO

import spray.can.Http
import spray.http._
import HttpMethods._
/**
 * Created by harvan on 22.7.2015.
 */
class LanguageTranslation(config: WatsonServiceConfig ) extends WatsonService(config) with LazyLogging {
  import system.dispatcher

  val MODEL_URL  = "/v2/models"
  val PATH_MODEL = "/v2/models/%s"
  val PATH_IDENTIFY: String = "/v2/identify"
  val PATH_TRANSLATE: String = "/v2/translate"
  val PATH_IDENTIFIABLE_LANGUAGES: String = "/v2/identifiable_languages"
  val PATH_MODELS: String = "/v2/models"
  def serviceType = "language_translation"
  def getModels : Future[LanguageModels] = getModels(Map())

  def getModels(showDefault: Boolean, source: String, target: String) : Future[LanguageModels] = {

    val map = collection.mutable.Map[String, String]()

    Option(source) match {
      case Some(src) if src.nonEmpty => map.put("source", src)
      case _ =>
    }

    Option(target) match {
      case Some(trg) if trg.nonEmpty => map.put("target", trg)
      case _ =>
    }

    Option(showDefault) match {
      case Some(showDef) => map.put("default", showDef.toString)
      case _ =>
    }

    getModels(map.toMap)
  }

  def getModels(params: Map[String,String]) : Future[LanguageModels] = {
    logger.info("Entering getModels")

    val request = Get(Uri(endpoint + MODEL_URL).withQuery(params))
    val response: Future[HttpResponse] = send(request)
    logger.info("Returning response")
    response.map(unmarshal[LanguageModels])
  }

  def createModel(options: CreateModelOptions) : Future[LanguageModel] = {
    var data : List[BodyPart] = List(BodyPart(options.baseModelId, formHeaders("name" -> "body_part")))
    data = Option(options.forcedGlossary) match {
      case Some(glossary) => BodyPart(glossary, "forced_glossary") :: data
      case _ => data
    }

    data = Option(options.monlingualCorpus) match {
      case Some(corpus) => BodyPart(corpus, "monolingual_corpus") :: data
      case _ => data
    }

    data = Option(options.parallelCorpus) match {
      case Some(parallelCorpus) => BodyPart(parallelCorpus, "parallel_corpus") :: data
      case _ => data
    }

    data = Option(options.name) match {
      case Some(name) => BodyPart(name, "name") :: data
      case _ => data
    }

    val formData = MultipartFormData(data)

    val request = Post(endpoint + PATH_MODELS, formData)
    send(request).map(unmarshal[LanguageModel])

  }

  def deleteModel(modelId : String) : Future[HttpResponse] = {
    val id : String = Option(modelId) match {
      case Some(mid) if mid.nonEmpty => mid
      case _ => throw new IllegalArgumentException("modelId cannot be null")
    }
    val request = Delete(endpoint + PATH_MODEL.format(id))
    send(request)
  }

  def getIdentifiableLanguages() : Future[List[IdentifiableLanguage]] = {
    val request = Get(endpoint + PATH_IDENTIFIABLE_LANGUAGES)
    val response = send(request)
    response.map(unmarshal[List[IdentifiableLanguage]])
  }

  def identify (text: String): Future[List[IdentifiedLanguage]] = {
    val request = Post(endpoint + PATH_IDENTIFY, text)
    val response = send(request)
    response.map(unmarshal[List[IdentifiedLanguage]])
  }

  def translate(text: String, modelId: String): Future[TranslationResult] = {
    Validation.notEmpty(modelId, "ModelId cannot be empty")
    translateRequest(text, modelId, null, null)
  }

  def translate(text: String, source: String, target: String) : Future[TranslationResult]= {
    Validation.notEmpty(source, "Source cannot be empty")
    Validation.notEmpty(target, "Target cannot be empty")
    translateRequest(text, null, source, target)
  }

  def translateRequest(text: String, modelId: String, source: String, target: String) : Future[TranslationResult] = {
    Validation.notEmpty(text, "Text cannot be empty")
    var map : Map[String, JsValue] = Map("text" -> JsString(text))
    map = JsonUtils.addIfNotEmpty(modelId, "model_id", map)
    map = JsonUtils.addIfNotEmpty(source, "source", map)
    map = JsonUtils.addIfNotEmpty(target, "target", map)
    val jsonRequest = new JsObject(map)

    val response = send(Post(endpoint + PATH_TRANSLATE, jsonRequest.toString()))
    response.map(unmarshal[TranslationResult])
  }


}
