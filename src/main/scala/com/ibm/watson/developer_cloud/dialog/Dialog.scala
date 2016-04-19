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
package com.ibm.watson.developer_cloud.dialog

import com.ibm.watson.developer_cloud.dialog.model.Conversation
import com.ibm.watson.developer_cloud.service.{ConfigFactory, WatsonService}
import com.ibm.watson.developer_cloud.utils.Validation
import Dialog._
import spray.http.{FormData}
import spray.httpx.SprayJsonSupport._
import com.ibm.watson.developer_cloud.dialog.model.DialogProtocol._
import spray.client.pipelining._

import scala.concurrent.Future
/**
  * Created by Martin Harvan on 15/04/16.
  */
class Dialog(config: ConfigFactory) extends WatsonService(config) {
    def serviceType: String = "dialog"

    def converse(conversation: Conversation, newMessage: String) : Future[Conversation] = {
        Validation.notEmpty(conversation.dialogId, "dialog id cannot be empty")

        val params: Map[String, String] = Map("dialog_id" -> conversation.dialogId, "input" -> newMessage)
        val clientMap = conversation.clientId.map(c => "client_id" -> c.toString).toMap
        val conversationId = conversation.id.map(c => "conversation_id" -> c.toString).toMap

        val query = params ++ clientMap ++ conversationId
        val url = dialogPath.format(conversation.dialogId)
        val request = Post(url, FormData(query))
        val response = send(request)

        response.map(unmarshal[Conversation])
    }

}

object Dialog {
    val dialogPath = "/v1/dialogs/%s/conversation"
}