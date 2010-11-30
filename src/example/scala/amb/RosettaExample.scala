import scala.collection.Iterator
import scala.util.continuations._
import net.jimmc.scoroutine._

/* From the RosettaCode.org wiki page for Amb:
   http://rosettacode.org/wiki/Amb

The example is using amb to choose four words from the following strings:

set 1: "the" "that" "a"

set 2: "frog" "elephant" "thing"

set 3: "walked" "treaded" "grows"

set 4: "slowly" "quickly"

It is a failure if the last character of word 1 is not equal to the
first character of word 2, and similarly with word 2 and word 3, as
well as word 3 and word 4. (the only successful sentence is "that thing
grows slowly").
*/
class RosettaExample extends AmbEval[List[String]] {
    generate {
        def joins(s1:String, s2:String) = s1.endsWith(s2.substring(0,1))
        val w1 = amb(List("the","that","a"))
        val w2 = amb(List("frog","elephant","thing"))
        val w3 = amb(List("walked","treaded","grows"))
        val w4 = amb(List("slowly","quickly"))
        require(joins(w1,w2))
        require(joins(w2,w3))
        require(joins(w3,w4))
        yld(List(w1,w2,w3,w4))
    }
}

//output: List(that, thing, grows, slowly)
