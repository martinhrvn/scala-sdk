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
package com.ibm.watson.developer_cloud.visual_recognition.v2

import spray.json.DefaultJsonProtocol

case class Label(name: String, score: Double)
case class LabelSet(labelGroups: List[String], labels: List[String]) {
  def withLabel(label: String) : LabelSet = {
    Option(labels) match {
      case Some(l) => LabelSet(labelGroups, label :: labels)
      case _ => LabelSet(labelGroups, List(label))
    }
  }

  def withLabelGroup(labelGroup: String) : LabelSet = {
    Option(labelGroups) match {
      case Some(lg) => LabelSet(labelGroup :: labelGroups, labels)
      case _ => LabelSet(List(labelGroup), labels)
    }
  }
}
case class RecognizedImage(id: String, labels: List[Label], name: String)
case class VisualRecognitionImages(images: List[RecognizedImage])

object VisualRecognitionProtocol extends DefaultJsonProtocol {
  implicit val labelFormat = jsonFormat(Label, "label_name", "label_score")
  implicit val labelSetFormat = jsonFormat(LabelSet, "label_groups", "labels")
  implicit val recognizedImageFormat = jsonFormat(RecognizedImage, "image_id", "labels", "image_name")
  implicit val visualRecognitionImageFormat = jsonFormat(VisualRecognitionImages, "images")
}