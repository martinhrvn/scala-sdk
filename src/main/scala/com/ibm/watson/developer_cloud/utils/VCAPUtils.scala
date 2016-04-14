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

import spray.json._
import spray.json.DefaultJsonProtocol._


object VCAPServicesProtocol extends DefaultJsonProtocol {
  implicit val credentialsFormat = jsonFormat(VCAPCredentials, "url", "username", "password")
  implicit val serviceFormat = jsonFormat(VCAPService, "name", "label", "plan", "tags", "credentials")
  implicit val vcapPropertiesFormat = new JsonFormat[VCAPProperties] {
    def write(m: VCAPProperties) = JsObject {
      m.properties.map { case (k: String, v: List[VCAPService])  =>
        k.toJson match {
          case JsString(x) => x -> v.toJson
          case x => throw new SerializationException("Map key must be formatted as JsString, not '" + x + "'")
        }
      }
    }

    def read(value: JsValue) : VCAPProperties = {
      val map = value match {
        case x: JsObject => x.fields.map { case (k,v) =>
          (JsString(k).convertTo[String], v.convertTo[List[VCAPService]])
        }
        case x => deserializationError("Expected Map as JsObject, but got " + x)
      }
      VCAPProperties(map)
    }
  }
}

case class VCAPCredentials(url: String, username: String, password: String)
case class VCAPService(name : String, label: String, plan: String, tags: Option[List[String]], credentials: VCAPCredentials)
case class VCAPProperties(properties: Map[String, List[VCAPService]])


