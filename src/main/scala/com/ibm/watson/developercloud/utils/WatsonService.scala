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

  def host : String = config.host
  val AUTHORIZATION = "Authorization"
  val headers = List(HttpHeaders.RawHeader(AUTHORIZATION, config.apiKey),
    HttpHeaders.RawHeader("Accept", "application/json"),
    HttpHeaders.Host(host, 443))

  def endpoint : String = config.endpoint
  def apiKey : String = config.apiKey
  def serviceType : String


  val pipeline: Future[SendReceive] =
    for (
      Http.HostConnectorInfo(connector, _) <-
      IO(Http) ? Http.HostConnectorSetup(host, port = 443, sslEncryption = true)
    ) yield sendReceive(connector)

  def send(request: HttpRequest) : Future[HttpResponse] = {
    pipeline.flatMap(_(request.withHeaders(headers)))
  }


}

