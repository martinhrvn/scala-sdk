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
package com.ibm.watson.developer_cloud.visual_insights.v1

import java.io.File

import com.ibm.watson.developer_cloud.utils.{Validation, WatsonService, WatsonServiceConfig}
import spray.client.pipelining._
import spray.http._
import spray.httpx.SprayJsonSupport._
import VisualInsigthsProtocol._
import scala.concurrent.Future


/**
  * The IBM Watson Visual Insights gives insight into the themes present in a collection of images
  * based on their visual appearance / content.
  *
  * @version v1
  */
class VisualInsigths(config: WatsonServiceConfig) extends WatsonService(config: WatsonServiceConfig) {
  /**
    * Gets the service type for service (used to get correct entry from VCAP_SERVICES properties)
    * @return
    */
  override def serviceType: String = "visual_insights"


  /**
    * Returns a summary of the collection's visual classifiers.
    * @return summary of classifiers
    */
  def getClassifiers : Future[Classifiers] = {
    getClassifiers(None)
  }

  /**
    * Returns a summary of the collection's visual classifiers, filtered by name.
    * @param name the filter name
    * @return summary of classifiers
    */
  def getClassifiers(name: Option[String]) : Future[Classifiers] = {
    val inUri = Uri(config.endpoint + VisualInsights.classifiersPath)
    val uri = name.map(x => inUri.withQuery(VisualInsights.filterName -> x)).getOrElse(inUri)

    val request = Get(uri)
    val response = send(request)
    response.map(unmarshal[Classifiers])
  }

  /**
    * Uplad a set of images as a ZIP file for visual insight extraction.
    * @param imagesFile the images ZIP file
    * @return the summary of the collection's visual attributes
    */
  def getSummary(imagesFile: File) : Future[Summary] = {
    Validation.fileExists(imagesFile, "imagesFile cannot be null or empty")
    val data: List[BodyPart] = List(BodyPart(imagesFile, VisualInsights.imagesFile))
    val formData = MultipartFormData(data)
    val request = Post(VisualInsights.summaryPath, formData)
    val response = send(request)
    response.map(unmarshal[Summary])
  }
}

object VisualInsights {
  val classifiersPath = "/v1/classifiers"
  val filterName = "filter_name"
  val imagesFile = "images_file"
  val summaryPath = "/v1/summary"
}
