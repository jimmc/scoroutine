import scala.util.continuations._

import net.jimmc.scoroutine.DefaultCoScheduler
import net.jimmc.scoroutine.CoQueue

/** This example is the same functionality as ProdConTest,
 * but the coroutines are split up to include subroutines so you
 * can see how you might structure thing in more complicated cases.
 */
object ProdConTestWithSubs {
    def main(args:Array[String]) = {
        val prodcon = new ProduceAndConsume()
        prodcon.run
    }
}

class ProduceAndConsume() {
    val sched = new DefaultCoScheduler
    val buf = new CoQueue[Int](sched,2)

    def run() {
        setUpProducer()
        setUpConsumer()
        sched.runUntilBlockedOrDone
    }

    def setUpProducer() {
        println("produce - before routine")
        sched.addRoutine("producer"){
            println("produce - start routine")
            var i = 0
            while (i < 4) {
                produce(i)
                i = i + 1
            }
            println("produce - end routine")
        }
        println("produce - after routine")
    }
    def produce(n:Int):Unit @suspendable = {
        println("Producing "+n)
        buf.blockingEnqueue(n)
        println("Produced "+n)
    }

    def setUpConsumer() {
        println("consume - before routine")
        sched.addRoutine("consumer"){
            println("consume - start routine")
            val total = consume1()+ consume1()+ consume1()
            println("consume total is "+total)
            println("consume - end routine")
        }
        println("consume - after routine")
    }
    def consume1():Int @suspendable = {
        println("Consuming next")
        val n = buf.blockingDequeue
        println("Consumed "+n)
        n
    }
}
