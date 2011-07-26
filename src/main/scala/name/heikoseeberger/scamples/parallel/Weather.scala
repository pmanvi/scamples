/*
 * Copyright 2011 Heiko Seeberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package name.heikoseeberger.scamples.parallel

import dispatch._
import scala.util.control.Exception._
import scala.xml.Elem

object Weather {

  def main(args: Array[String]): Unit = {
    val locations = Seq(
        "Berlin-Germany",
        "Cologne-Germany",
        "Hamburg-Germany",
        "Munich-Germany",
        "Nuremberg-Germany",
        "Passau-Germany",
        "Stuttgart-Germany",
        "Bath-England",
        "London-England",
        "Manchester-England",
        "Dublin-Ireland",
        "Lille-France",
        "Marseille-France",
        "Paris-France",
        "Boston-USA",
        "Chicago-USA",
        "Dallas-USA",
        "Denver-USA",
        "Los Angeles-USA",
        "Miami-USA",
        "New Orleans-USA",
        "New York-USA",
        "San Francisco-USA",
        "Seattle-USA")
    val isParallel = args.headOption map {_ == "par"} getOrElse false
    val time0 = System.currentTimeMillis
    // TODO: Waiting for r25235 to be ported to 2.9.x. Then move decision about seq or par to locations, i.e. make currentTemperatures unaware of specific kind of collection.
    val currentTemperatures =
      if (isParallel) (locations.par map currentTemperature).flatten.seq
      else (locations map currentTemperature).flatten
    val time1 = System.currentTimeMillis
    val averageTemperature = currentTemperatures.sum / currentTemperatures.size
    println("Calculated average temperature (%s out of %s) of about %s °C in %s ms.".format(
        currentTemperatures.size,
        locations.size,
        averageTemperature,
        time1 - time0))
  }

  def currentTemperature(location: String): Option[Int] =
    catching(classOf[Exception]) opt {
      def extractTemperature(weatherResponse: Elem) =
        (weatherResponse \\ "current_conditions" \ "temp_c").headOption map { temp => (temp \ "@data").text.toInt }
      val weatherRequest = :/("www.google.com") / "ig" / "api" <<? Map("weather" -> location, "hl" -> "en")
      Http(weatherRequest <> extractTemperature)
    } flatMap { x => x }
}
