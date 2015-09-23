package com.ibm.watson.developercloud.language_classification.v1

import LanguageIdentificationProtocol._
import akka.actor.ActorSystem
import com.ibm.watson.developercloud.utils.WatsonService
import com.typesafe.scalalogging.LazyLogging
import spray.client.pipelining._
import spray.http.{FormData, HttpRequest}
import spray.httpx.SprayJsonSupport._

import scala.concurrent.Future


class LanguageIdentification(username: String, password: String) extends WatsonService(username, password) with LazyLogging {
  implicit val system = ActorSystem()
  import system.dispatcher

  val ENDPOINT = "https://gateway.watsonplatform.net/language-identification-beta/api"
  val LANGUAGE_IDENT_URL  = "/v1/txtlid/0"

  def identify(text: String) : Future[IdentifiedLanguage] = {
    val pipeline : HttpRequest => Future[IdentifiedLanguage] = addHeader(AUTHORIZATION, getApiKey) ~> sendReceive ~> unmarshal[IdentifiedLanguage]
    val response: Future[IdentifiedLanguage] = pipeline(
        Post(ENDPOINT + LANGUAGE_IDENT_URL,
          FormData(Seq("sid" -> "lid-generic", "rt" -> "json", "txt" -> text))))
    response
  }
}
