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
package com.ibm.watson.developer_cloud.visual_recognition.v1

import java.io.File

import com.ibm.watson.developer_cloud.utils._
import com.ibm.watson.developer_cloud.visual_recognition.v1.model.{VisualRecognitionImages, LabelSet, VisualRecognitionProtocol}
import com.ibm.watson.developer_cloud.visual_recognition.v2.VisualRecognitionImages
import spray.httpx.SprayJsonSupport._
import spray.http._
import spray.client.pipelining._
import spray.json._
import VisualRecognitionProtocol._

import scala.concurrent.Future

/**
  * The Visual Recognition service analyzes images, enabling you to understand their content without
  * any accompanying descriptive text.
  *
  * @version v1
  * @see <a
  *      href="http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/visual-recognition.html">
  *      Visual Recognition</a>
  */
class VisualRecognition(configFactory: ConfigFactory = new VCAPConfigFactory()) extends WatsonService(configFactory) {
  /**
    * Gets the service type for service (used to get correct entry from VCAP_SERVICES properties)
    * @return
    */
  override def serviceType: String = "visual_recognition"

  /**
    * Gets the labels and label groups
    * @return labels and label groups
    */
  def labelSet : Future[LabelSet] = {
    val request = Get(config.endpoint + VisualRecognition.labelsPath)
    send(request).map(unmarshal[LabelSet])
  }

  /**
    * Classifies the images against the label groups and labels. The response includes a score for a
    * label if the score meets the minimum threshold of 0.5. If no score meets the threshold for an
    * image, no labels are returned.

    * @param image the image file
    * @param labelSet labels to classify against (optional)
    * @return the visual recognition image
    */
  def recognize(image: File, labelSet: Option[LabelSet] = None) : Future[VisualRecognitionImages] = {
    Validation.notNull(image, "Image cannot be null")
    val bodyPart = BodyPart(image, "imgFile")
    val list = List(bodyPart) ++
    labelSet.map({p => BodyPart(p.toJson.toString, VisualRecognition.labelsToCheck)}).toList
    val data = MultipartFormData(list)

    val request = Post(config.endpoint + VisualRecognition.recognizePath, data)
    send(request).map(unmarshal[VisualRecognitionImages])
  }

}

object VisualRecognition {
  val labelsPath = "/v1/tag/labels"
  val recognizePath = "/v1/tag/recognize"
  val labelsToCheck = "labels_to_check"
}
