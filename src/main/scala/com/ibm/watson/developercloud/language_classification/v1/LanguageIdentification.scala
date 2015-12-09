package com.ibm.watson.developercloud.language_classification.v1

import LanguageIdentificationProtocol._
import com.ibm.watson.developercloud.utils.{WatsonServiceConfig, WatsonService}
import com.typesafe.scalalogging.LazyLogging
import spray.client.pipelining._
import spray.http.{Uri, HttpResponse, FormData, HttpRequest}
import spray.httpx.SprayJsonSupport._

import scala.concurrent.Future


class LanguageIdentification(config: WatsonServiceConfig) extends WatsonService(config) with LazyLogging {
  import system.dispatcher

  val LANGUAGE_IDENT_URL  = "/v1/txtlid/0"
  val serviceType = "language_identification"

  def identify(text: String) : Future[IdentifiedLanguage] = {
    val data = FormData(Seq("sid" -> "lid-generic", "rt" -> "json", "txt" -> text))
    val request = Post(endpoint + LANGUAGE_IDENT_URL, data)
    send(request).map(unmarshal[IdentifiedLanguage])
  }
}
