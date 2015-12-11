package com.ibm.watson.developercloud.utils


import java.util.concurrent.Executors

import akka.actor.ActorSystem
import akka.io.IO
import akka.util.Timeout
import spray.can.Http
import spray.can.Http.{HostConnectorInfo, HostConnectorSetup}
import spray.client.pipelining._
import spray.http.{HttpResponse, HttpHeaders, HttpRequest}
import org.apache.commons.codec.binary.Base64
import akka.actor.ActorSystem
import akka.io._
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


abstract class WatsonService(var config : WatsonServiceConfig) {
  config.setup(serviceType)

  implicit val system = ActorSystem("simple-spray-client")
  implicit val requestTimeout = Timeout(60 seconds)


  import system.dispatcher

  val AUTHORIZATION = "Authorization"
  val headers = List(HttpHeaders.RawHeader(AUTHORIZATION, config.apiKey),
    HttpHeaders.RawHeader("Accept", "application/json"),
    HttpHeaders.Host(config.host, 443))


  /**
    * Gets the service type for service (used to get correct entry from VCAP_SERVICES properties)
    * @return
    */
  def serviceType : String

  val pipeline: Future[SendReceive] =
    for (
      Http.HostConnectorInfo(connector, _) <-
      IO(Http) ? Http.HostConnectorSetup(config.host, port = 443, sslEncryption = true)
    ) yield sendReceive(connector)

  /**
    * Sends the request and applies correct headers (apiKey, host and port)
    * @param request
    * @return
    */
  def send(request: HttpRequest) : Future[HttpResponse] = {
    pipeline.flatMap(_(request.withHeaders(headers)))
  }

  /**
    * Helper method to return headers for form data
    * @param params params to apply
    * @return sequence of headers
    */
  def formHeaders(params: (String, String)*) =
    Seq(HttpHeaders.`Content-Disposition`("form-data", Map(params: _*)))
}

