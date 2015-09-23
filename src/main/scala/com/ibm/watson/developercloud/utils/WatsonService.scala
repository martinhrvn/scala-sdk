package com.ibm.watson.developercloud.utils


import akka.actor.ActorSystem
import akka.io.IO
import com.ibm.watson.developercloud.natural_language_classifier.v1.Classification
import spray.can.Http
import spray.can.Http.HostConnectorSetup
import spray.client.pipelining._
import spray.http.HttpRequest
import org.apache.commons.codec.binary.Base64

import scala.concurrent.Future


class WatsonService(username: String, password: String) {
  val AUTHORIZATION = "Authorization"

  def getApiKey : String = getApiKey(username, password)

  def getApiKey(username: String, password: String) : String = {
    "Basic " + Base64.encodeBase64String((username+":"+password).getBytes)
  }
}

