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

import com.typesafe.scalalogging.LazyLogging
import org.apache.commons.codec.binary.Base64
import spray.json._
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

object VCAPServicesProtocol extends DefaultJsonProtocol {
  implicit val credentialsFormat = jsonFormat(VCAPCredentials, "url", "username", "password")
  implicit val serviceFormat = jsonFormat(VCAPService, "name", "label", "plan", "tags", "credentials")
  implicit val vcapPropertiesFormat = new JsonFormat[VCAPProperties] {
    def write(m: VCAPProperties) = JsObject {
      m.properties.map { field =>
        field._1.toJson match {
          case JsString(x) => x -> field._2.toJson
          case x => throw new SerializationException("Map key must be formatted as JsString, not '" + x + "'")
        }
      }
    }

    def read(value: JsValue) = {
      val map = value match {
        case x: JsObject => x.fields.map { field =>
          (JsString(field._1).convertTo[String], field._2.convertTo[List[VCAPService]])
        }
        case x => deserializationError("Expected Map as JsObject, but got " + x)
      }
      VCAPProperties(map)
    }
  }
}

abstract class WatsonServiceConfig extends LazyLogging {


  def username : String
  def password : String
  def apiKey : String = {
    "Basic " + Base64.encodeBase64String((username + ":" + password).getBytes)
  }
  def endpoint: String
  def setup(serviceType: String)
  def host: String = {
    endpoint.split("://").tail.mkString.split("/").head
  }
}

class VCAPServicesConfig(val serviceName: Option[String] = None) extends WatsonServiceConfig{
  val vcapProperties = VCAPServicesProtocol.vcapPropertiesFormat.read(sys.env("VCAP_SERVICES").parseJson)

  def setup(serviceType: String) : Unit = {
    logger.info("Entering setup")
    logger.info("Got vcapProperties: " + vcapProperties )
    logger.info("Got serviceType: " + serviceType)
    serviceProperties = Option(serviceName) match {
      case Some(s) if !s.isEmpty => vcapProperties.properties(serviceType).filter(_.name == s).head
      case _ => vcapProperties.properties(serviceType).head
    }
  }

  var serviceProperties : VCAPService = null
  def username : String = serviceProperties.credentials.username
  def password : String = serviceProperties.credentials.password
  def endpoint : String = serviceProperties.credentials.url
}

class ManualServicesConfig(val username: String, val password: String, val endpoint: String) extends WatsonServiceConfig {
  def setup(serviceType: String) : Unit = {}
}
case class VCAPCredentials(url: String, username: String, password: String)
case class VCAPService(name : String, label: String, plan: String, tags: List[String], credentials: VCAPCredentials)
case class VCAPProperties(properties: Map[String, List[VCAPService]])

