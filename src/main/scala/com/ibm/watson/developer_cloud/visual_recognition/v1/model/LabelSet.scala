package com.ibm.watson.developer_cloud.visual_recognition.v1.model

import com.ibm.watson.developer_cloud.service.GenericModel

/**
  * Created by martinhrvn on 20/03/16.
  */
case class LabelSet(labelGroups: List[String], labels: List[String]) extends GenericModel {
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
