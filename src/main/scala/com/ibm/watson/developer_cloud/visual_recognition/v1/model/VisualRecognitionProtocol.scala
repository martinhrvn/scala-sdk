package com.ibm.watson.developer_cloud.visual_recognition.v1.model

import spray.json.DefaultJsonProtocol
import com.ibm.watson.developer_cloud.visual_recognition.v1.model._
/**
  * Created by martinhrvn on 20/03/16.
  */
object VisualRecognitionProtocol extends DefaultJsonProtocol {
  implicit val labelFormat = jsonFormat(Label, "label_name", "label_score")
  implicit val labelSetFormat = jsonFormat(LabelSet, "label_groups", "labels")
  implicit val recognizedImageFormat = jsonFormat(RecognizedImage, "image_id", "labels", "image_name")
  implicit val visualRecognitionImageFormat = jsonFormat(VisualRecognitionImages, "images")
}
