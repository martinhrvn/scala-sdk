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

package com.ibm.watson.developer_cloud.natural_language_classifier.v1

import spray.json._

case class Classification (var id: String, var url: String, var text: String, var topClass: String, var classes:  List[ClassifiedClass])

case class ClassifiedClass (var name: String, var confidence: Double)

case class TrainingData (var text: String, var classes: List[String])

case class Classifier (var id : String, var url: String, var status: String, var statusDescription: String)

object NaturalLanguageClassifierProtocol extends DefaultJsonProtocol {
  implicit val classifierFormat = jsonFormat(Classifier, "classifier_id", "url", "status", "status_description")
  implicit val classifiedClassFormat = jsonFormat(ClassifiedClass, "name", "confidence")
  implicit val classificationFormat = jsonFormat(Classification, "classification_id", "url", "text", "top_class", "classes")
  implicit val trainingDataFormat = jsonFormat(TrainingData, "text", "classes")
}
