/* Copyright 2010 Jim McBeath under GPLv2 */

package net.jimmc.scoroutine

import scala.collection.Iterator
import scala.util.continuations._

/** Non-deterministic evaluation using AMB.
 * @author Jim McBeath
 * @since 1.0.1
 */
class AmbEval[T] extends StandaloneGenerator[T] {
    def amb[A](seq:Iterable[A]):A @cpsParam[Unit,Unit] = {
        shift { k:(A=>Unit) =>
            val it = seq.iterator
            reset[Unit,Unit] {
                //Use of var for v is workaround for Scala bug #3501
                var v:A = null.asInstanceOf[A]
                while (it.hasNext) {
                    v = it.next
                    k(v)
                    stepUntilDone
                }
            }
        }
    }

    def require(condition: =>Boolean):Unit @cpsParam[Unit,Unit] = {
        if (!condition) { amb(List()) }
    }
}
