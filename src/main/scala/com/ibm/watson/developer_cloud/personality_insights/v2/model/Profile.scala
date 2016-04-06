package com.ibm.watson.developer_cloud.personality_insights.v2.model

import com.ibm.watson.developer_cloud.service.GenericModel

/**
  * Created by martinhrvn on 20/03/16.
  */
case class Profile(id: String, processedLanguage: String, source: String, tree: Trait, wordCount: Int, wordCountMessage: String)extends GenericModel
