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
package com.ibm.watson.developer_cloud.concept_insights.v2

import com.ibm.watson.developer_cloud.concept_insights.v2.model.Annotations
import com.ibm.watson.developer_cloud.service.ManualConfig
import org.scalatest.FlatSpec
import spray.json._
import com.ibm.watson.developer_cloud.concept_insights.v2.model.ConceptInsightsProtocol._
/**
  * Created by Martin Harvan on 12/04/16.
  */
class ConceptInsightsTest extends FlatSpec {
    "Service type for ConceptInsights" should "be concept_insights" in {
        val service = new ConceptInsights(new ManualConfig("test1", "test2", "test3"))
        assert(service.serviceType.equals("concept_insights"))
    }

    "Annotations" should "be parsed correctly" in {
        val json = scala.io.Source.fromURL(getClass.getResource("/concept_insights/annotations.json")).mkString
        val annotations = json.parseJson.convertTo[Annotations]

        assert(annotations.annotations.nonEmpty)
    }
}
