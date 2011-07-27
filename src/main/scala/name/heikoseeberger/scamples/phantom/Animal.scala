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

package name.heikoseeberger.scamples.phantom

object Animal {

  def main(args: Array[String]) {
//    new Animal[AwakeOrAsleep].goToSleep // This must not compile!
//    new Animal[AwakeOrAsleep].wakeUp // This must not compile!
//    new Animal[Awake].wakeUp // This must not compile!
//    new Animal[Awake].goToSleep.goToSleep // This must not compile!
    new Animal[Awake].goToSleep.wakeUp.goToSleep
    () // TODO Why is this necessary?
  }
}

class Animal[A <: AwakeOrAsleep] {

  def goToSleep[B >: A <: Awake]: Animal[Asleep] = new Animal[Asleep]

  def wakeUp[B >: A <: Asleep]: Animal[Awake] = new Animal[Awake]
}

sealed trait AwakeOrAsleep

trait Awake extends AwakeOrAsleep

trait Asleep extends AwakeOrAsleep
