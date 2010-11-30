import scala.collection.Iterator
import scala.util.continuations._
import net.jimmc.scoroutine._

/* Exercise 4.35 from SICP:
  "implement a procedure that finds Pythagorean triples, i.e., triples
  of integers (i,j,k) between the given bounds such that i < j and
  i^2 + j^2  = k^2"
 */
class APythagoreanTripleBetween(low:Int,high:Int)
        extends AmbEval[(Int,Int,Int)] {
    generate {
        val i = amb(low to high)
        val j = amb(i to high)
        val k = amb(j to high)
        require(i*i + j*j == k*k)
        yld((i,j,k))
    }
}

//output using (low=1,high=20):
// (3,4,5)
// (5,12,13)
// (6,8,10)
// (8,15,17)
// (9,12,15)
// (12,16,20)
