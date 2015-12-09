package com.ibm.watson.developercloud.language_translation.v2

import java.util.concurrent.{Executors, ExecutorService}

import LanguageTranslationProtocol._
import akka.actor.ActorSystem
import akka.io._
import com.ibm.watson.developercloud.utils.{WatsonServiceConfig, WatsonService}
import com.typesafe.scalalogging.LazyLogging
import spray.can.Http
import spray.client.pipelining._
import spray.client._
import spray.http.{BasicHttpCredentials, Uri, HttpRequest}
import spray.http.Uri.Query
import spray.httpx.unmarshalling._
import spray.io.ClientSSLEngineProvider
import spray.json.JsValue
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

    val response: Future[HttpResponse] = send(Get(Uri("/language-translation/api" + MODEL_URL).copy(query = Uri.Query(params))))
    logger.info("Returning response")
    response.map(unmarshal[LanguageModels])
  }
}