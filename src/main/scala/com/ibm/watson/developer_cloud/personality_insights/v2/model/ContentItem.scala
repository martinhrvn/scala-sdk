package com.ibm.watson.developer_cloud.personality_insights.v2.model

import java.util.Date

import com.ibm.watson.developer_cloud.service.GenericModel

/**
  * Created by Martin Harvan (martin.harvan@sk.ibm.com) on 20/03/16.
  */
case class ContentItem(charset: String, content: String, contentType: String, created: Date, forward: Boolean,
                       id: String, language: String, parentId: String, reply: Boolean, sourceId: String,
                       update: Date, userId: String)extends GenericModel
