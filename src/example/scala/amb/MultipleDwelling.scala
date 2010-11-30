import scala.collection.Iterator
import scala.util.continuations._
import net.jimmc.scoroutine._

/* multiple-dwelling problem from the Logic Puzzle section of SICP.
 * http://mitpress.mit.edu/sicp/full-text/book/book-Z-H-28.html#%_sec_Temp_608

As stated in SICP:
  The following puzzle (taken from Dinesman 1968) is typical of a large
  class of simple logic puzzles:

    Baker, Cooper, Fletcher, Miller, and Smith live on different floors
    of an apartment house that contains only five floors. Baker does
    not live on the top floor. Cooper does not live on the bottom
    floor. Fletcher does not live on either the top or the bottom
    floor. Miller lives on a higher floor than does Cooper. Smith does
    not live on a floor adjacent to Fletcher's. Fletcher does not live
    on a floor adjacent to Cooper's. Where does everyone live?
 */
class MultipleDwelling extends AmbEval[List[(String,Int)]] {
    def distinct(vals:List[Int]):Boolean = {
        vals.distinct.length == vals.length
    }
    generate {
        val baker = amb(List(1,2,3,4,5))
        val cooper = amb(List(1,2,3,4,5))
        val fletcher = amb(List(1,2,3,4,5))
        val miller = amb(List(1,2,3,4,5))
        val smith = amb(List(1,2,3,4,5))
        require(distinct(List(baker,cooper,fletcher,miller,smith)))
        require(baker!=5)
        require(cooper!=1)
        require(fletcher!=5)
        require(fletcher!=1)
        require(miller>cooper)
        require(scala.math.abs(smith-fletcher)!=1)
        require(scala.math.abs(fletcher-cooper)!=1)
        yld(List(
            ("baker",baker),
            ("cooper",cooper),
            ("fletcher",fletcher),
            ("miller",miller),
            ("smith",smith)))
    }
}

//output: List((baker,3), (cooper,2), (fletcher,4), (miller,5), (smith,1))
