// Copyright (C) 2016 IBM Corp. All Rights Reserved.
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
package com.ibm.watson.developer_cloud.concept_expansion.v1.model

import spray.json._


/**
  * Created by Martin Harvan on 10/04/16.
  */
object ConceptExpansionProtocol extends DefaultJsonProtocol{
  implicit val statusFormat = new RootJsonFormat[StatusEnum] {
    override def read(json: JsValue): StatusEnum = json match {
      case JsString(str) => StatusEnum.byId(str) match {
        case Some(status) => status
        case _ => throw new DeserializationException("Wrong Status ID")
      }
      case _ => throw new DeserializationException("Wrong Status ID")
    }

    override def write(obj: StatusEnum): JsValue = JsString(obj.id)
  }
  implicit val datasetFormat = jsonFormat(Dataset, "id", "status")
  implicit val conceptFormat = jsonFormat(Concept, "name", "prevalence")
  implicit val jobFormat = jsonFormat(Job, "jobid", "status")
}

//implicit object CurrencyJsonFormat extends RootJsonFormat[Currency.CurrencyType] {
//  def write(obj: Currency.CurrencyType): JsValue = JsString(obj.toString)
//
//  def read(json: JsValue): Currency.CurrencyType = json match {
//    case JsString(str) => Currency.withName(str)
//    case _ => throw new DeserializationException("Enum string expected")
//  }
//}
