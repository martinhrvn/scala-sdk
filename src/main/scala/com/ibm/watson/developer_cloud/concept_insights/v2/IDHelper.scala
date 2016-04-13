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

import com.ibm.watson.developer_cloud.concept_insights.v2.model.{Corpus, Graph}
import com.ibm.watson.developer_cloud.utils.Validation

import scala.util.matching.Regex

/**
  * Created by Martin Harvan on 12/04/16.
  */
object IDHelper {
    val graphIdRegex: String = "^/graphs/[_\\-\\w\\s]*/[_\\-\\w\\s]*$"
    val corpusRegex: String = "^/corpora/[_\\-\\w\\s]*/[_\\-\\w\\s]*$"

    def corpusId(corpus: Corpus, accountId: String): String = {
        Validation.notNull(corpus, "graph object cannot be null")
        val id = Option(corpus.id) match {
            case Some(id) => {
                validateId(corpusRegex, id, "Provide a valid corpus.id (format is " + '"' +
                  " (/corpora/{account_id}/{graph}) +" + '"' + ")")
                id
            }
            case _ => {
                Validation.notNull(corpus.name, "graph.name cannot be null")
                "/corpora/" + accountId + "/" + corpus.name
            }
        }
        id
    }

    def graphId(graph: Graph, accountId: String) : String = {
        Validation.notNull(graph, "graph object cannot be null")
        val id = Option(graph.id) match {
            case Some(id) => {
                validateId(graphIdRegex, id, "Provide a valid graph.id (format is " + '"' +
                  " (/graphs/{account_id}/{graph}) +" + '"' + ")")
                id
            }
            case _ => {
                Validation.notNull(graph.name, "graph.name cannot be null")
                "/graphs/" + accountId + "/" + graph.name
            }
        }
        id
    }

    def validateId(regex: String, string: String, message: String) : Unit = {
        if(!string.matches(regex)) throw new IllegalArgumentException(message)
    }
}
