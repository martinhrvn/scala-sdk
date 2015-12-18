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
package com.ibm.watson.developer_cloud.utils

import org.apache.commons.codec.binary.Base64
import org.slf4j.LoggerFactory
import spray.json._
import spray.json.DefaultJsonProtocol._



import VCAPServicesProtocol._
case class WatsonServiceConfig(username: String, password: String, endpoint: String)  {

  val logger = LoggerFactory.getLogger(classOf[WatsonServiceConfig])

  def apiKey : String = {
    "Basic " + Base64.encodeBase64String((username + ":" + password).getBytes)
  }
  def host: String = {
    endpoint.split("://").tail.mkString.split("/").head
  }
}

abstract class ConfigFactory(serviceName: Option[String] = None) {
  def getConfigForServiceType(serviceType: String) : WatsonServiceConfig
}

class VCAPConfigFactory(serviceName: Option[String] = None) extends ConfigFactory(serviceName) {
  def getConfigForServiceType(serviceType: String): WatsonServiceConfig = {
    Validation.notEmpty(serviceType, "ServiceType cannot be empty")
    val vcapProperties: VCAPProperties = sys.env("VCAP_SERVICES").parseJson.convertTo[VCAPProperties]
    val serviceProperties = serviceName match {
      case Some(s) if !s.isEmpty => vcapProperties.properties(serviceType).filter(_.name == s).head
      case _ => vcapProperties.properties(serviceType).head
    }
    val credentials = serviceProperties.credentials
    new WatsonServiceConfig(credentials.username, credentials.password, credentials.url)
  }
}

class ManualConfig(username: String, password: String, endpoint: String) extends ConfigFactory {
  override def getConfigForServiceType(serviceType: String): WatsonServiceConfig = new WatsonServiceConfig(username, password, endpoint)
}
