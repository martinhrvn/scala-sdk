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
package com.ibm.watson.developercloud.utils


import akka.actor.ActorSystem
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import spray.can.Http
import spray.client.pipelining.{SendReceive, sendReceive}
import spray.http.{HttpHeader, HttpRequest, HttpResponse, HttpHeaders}

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps


abstract class WatsonService(var config : WatsonServiceConfig) {
  config.setup(serviceType)

  implicit val system = ActorSystem("simple-spray-client")

  implicit val requestTimeout = Timeout(60 seconds)

  import system.dispatcher

  val headers = List(HttpHeaders.RawHeader(WatsonService.Authorization, config.apiKey),
    HttpHeaders.RawHeader(WatsonService.Accept, "application/json"),
    HttpHeaders.Host(config.host, WatsonService.SslPort))


  /**
    * Gets the service type for service (used to get correct entry from VCAP_SERVICES properties)
    * @return
    */
  def serviceType : String

  val pipeline: Future[SendReceive] =
    for (
      Http.HostConnectorInfo(connector, _) <-
      IO(Http) ? Http.HostConnectorSetup(config.host, port = WatsonService.SslPort, sslEncryption = true)
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
  def formHeaders(params: (String, String)*): Seq[HttpHeader] =
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
  val SslPort = 443
}
