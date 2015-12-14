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
package com.ibm.watson.developercloud.visual_insights.v1

import spray.json.DefaultJsonProtocol

case class Classifier(name: String)
case class Classifiers(classifiers : List[Classifier])
case class Summary(name: String, score: Double)

object VisualInsigthsProtocol extends DefaultJsonProtocol {
  implicit val classifierFormat = jsonFormat(Classifier, "name")
  implicit val classifiersFormat = jsonFormat(Classifiers, "classifiers")
  implicit val summaryFormat = jsonFormat(Summary, "name", "score")
}