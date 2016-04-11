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
package com.ibm.watson.developer_cloud.concept_insights.v2.model

import com.ibm.watson.developer_cloud.service.GenericModel
import com.ibm.watson.developer_cloud.utils.Validation

/**
  * Created by Martin Harvan on 11/04/16.
  */
case class Concept(_abstract: String, id: String, label: String, link: String, name: String,
                  ontology: List[String], thumbnail: String) extends GenericModel {

}

case class ConceptMetadata(_abstract: String, id: String, label: String, link: String, ontology: List[String], thumbnail: String, _type: String) extends GenericModel

case class Concepts(concepts: List[Concept])

object Concept {
//TODO from graph
}
