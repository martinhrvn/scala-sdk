package com.ibm.watson.developercloud.utils


import akka.actor.ActorSystem
import akka.io.IO
import akka.pattern.ask
import spray.can.Http
import spray.client.pipelining._
import spray.http.{HttpRequest, HttpResponse, _}

import scala.concurrent.Future


abstract class WatsonService(var config : WatsonServiceConfig) {
  config.setup(serviceType)

  implicit val system = ActorSystem("simple-spray-client")

  import system.dispatcher

  val headers = List(HttpHeaders.RawHeader(WatsonService.Authorization, config.apiKey),
    HttpHeaders.RawHeader(WatsonService.Accept, "application/json"),
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
    * @param request HttpRequest to send
    * @return Future of HttpResponse
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
    Seq(HttpHeaders.`Content-Disposition`(WatsonService.FormData, Map(params: _*)))
}

object WatsonService {
  val Accept = "Accept"
  val Authorization = "Authorization"
  val FormData = "form-data"
  val IncludeRaw = "include_raw"
  val ContentType = "Content-Type"
  val ContentLanguage = "Content-Language"
  val AcceptLanguage = "Accept-Language"
}
