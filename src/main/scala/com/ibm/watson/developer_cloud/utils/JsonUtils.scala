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

import spray.json.{JsString,JsValue}
/**
  * Created by martinhrvn on 10/12/15.
  */
object JsonUtils {
  def addIfNotEmpty(value: Option[String], name: String, map: Map[String, JsValue]) : Map[String, JsValue] = {
    value match {
      case Some(v) if v.nonEmpty => map + (name -> JsString(v))
      case _ => map
    }
  }

  def addIfNotEmpty(value: String, name: String, map: Map[String, JsValue]) : Map[String, JsValue] = {
    addIfNotEmpty(Option(value), name, map)
  }
}
