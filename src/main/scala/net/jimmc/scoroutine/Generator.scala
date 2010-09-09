/* Copyright 2010 Jim McBeath under GPLv2 */

package net.jimmc.scoroutine

import scala.collection.Iterator
import scala.util.continuations._

/** Generic generator class.
 * @author Jim McBeath
 * @since 1.0
 */
class Generator[T] extends Iterator[T] {
    /** The scheduler to use with this generator.  Subclass may override. */
    val sched = new DefaultCoScheduler
    private val buf = new CoQueue[T](sched,1)

    /** Subclass calls this method to generate values.
     * @param body The code for your generator.
     */
    protected def generate(body: => Unit @suspendable) {
        sched.addRoutine("gen") { body }
        sched.run
    }

    /** Yield the next generated value.
     * Call this code from your generator to deliver the next value.
     */
    protected def yld(x:T):Unit @suspendable = {
        buf.blockingEnqueue(x)
    }

    /** Retrieve the next generated value.
     * Call this from your main code.
     */
    def next:T = {
        sched.run
        buf.dequeue
    }

    /** True if there is another value to retrieve.
     * Call this from your main code.
     */
    def hasNext:Boolean = {
        sched.run
        !buf.dequeueBlocker.isBlocked
    }
}
