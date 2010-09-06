import scala.util.continuations._

import net.jimmc.scoroutine.DefaultCoScheduler
import net.jimmc.scoroutine.CoQueue

object ProdConTest {
    def main(args:Array[String]) = {
        val prodcon = new ProduceAndConsume()
        prodcon.run
    }
}

class ProdCon() {
    val sched = new DefaultCoScheduler
    val buf = new CoQueue[Int](sched,2)

    def run() {
        sched.addRoutine("producer"){
            var i = 0
            while (i < 4) {
                buf.blockingEnqueue(i)
            }
        }
        sched.addRoutine("consumer"){
            val total = buf.blockingDequeue +
                    buf.blockingDequeue + buf.blockingDequeue
            println("consume total is "+total)
        }
        sched.run
    }
}
