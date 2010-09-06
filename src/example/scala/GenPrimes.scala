import scala.util.continuations._

import net.jimmc.scoroutine.CoQueue
import net.jimmc.scoroutine.Generator

object GenPrimes {
    def main(args:Array[String]) = {
        val gen = new PrimeGen()
        while(gen.hasNext) {
            println("Prime: "+gen.next)
        }
    }
}

class PrimeGen extends Generator[Int] {
    val bufSize = 1
    val out1 = new CoQueue[Int](sched,bufSize)

    sched.addRoutine("prime2")(nextPrime(2,out1))
    generate {
        def gen(n:Int):Unit @suspendable = {
            out1.blockingEnqueue(n)
            gen(n+1)
        }
        gen(2)
    }

    def nextPrime(p:Int, in:CoQueue[Int]):Unit @suspendable = {
        var out:Option[CoQueue[Int]] = None
        yld(p)
        def sieve():Unit @suspendable = {
            val n = in.blockingDequeue()
            if ((n%p)!=0) {
                if (!out.isDefined) {
                    out = Some(new CoQueue[Int](sched,bufSize))
                    val rName = "prime"+n
                    sched.addRoutine(rName)(nextPrime(n,out.get))
                }
                out.get.blockingEnqueue(n)
            } else {
                in.dequeueBlocker.waitUntilNotBlocked
            }
            sieve()
        }
        sieve()
    }
}
