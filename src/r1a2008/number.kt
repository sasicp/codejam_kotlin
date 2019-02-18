package r1a2008

import java.io.File
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.sqrt
// This requiires a few algorithms (a + b.sqrt(x))*(c + d.sqrt(x)) = (ac + bdx) + (ad +bc)(sqrt(x). The next algorithm
// is a fast way to compute (a + b. sqrt(x)**N very fast. We are using the standard divide and conquer method, which
// takes log(N) steps (each step can be long, because we are dealing with large numbers). The final algorith needed is a
// way to compute square root to a given precision, so that integer part of a.sqrt(x) cam be calculated. This is done
// using the N-R (or Babylonian) method, which has quadratic convergence.
// Unfortunately, the power computation seems to be ab N^2 algorithm, since each number has ~N digits, multiplying them
// is N^2.
// **** Does not work for large input ***
//fun nChoosek(n: Int, k : Int) : BigInteger{
//    val kInt = if(n-k > k)  k else n - k
//    var out : BigInteger = BigInteger.ONE
//    for(ii in 0 until kInt){
//        out =  BigInteger((n-ii).toString())
//        out /= BigInteger((ii + 1).toString())
//    }
//    return out
//}
const val DEBUG = false

data class NumPlusSqrt(val intPart : BigInteger, val scaleP : BigInteger, val root : Int){
    operator fun times(other : NumPlusSqrt): NumPlusSqrt{
        assert(root == other.root)

        val out =  NumPlusSqrt(intPart * other.intPart + scaleP*other.scaleP *BigInteger(root.toString()),
            intPart*other.scaleP + other.intPart*scaleP, root)
        if (DEBUG) {
            println("($this) * ($other) = $out")
        }
        return out

    }
    operator fun plus(other: NumPlusSqrt) : NumPlusSqrt{
        assert(root == other.root)
        return NumPlusSqrt(intPart + other.intPart, scaleP + other.scaleP, root)
    }
    override fun toString(): String = "$intPart + $scaleP SQRT($root)"

    fun pow(n : Int) : NumPlusSqrt{
       return  when{
            n == 0 -> {
                NumPlusSqrt(BigInteger.ONE, BigInteger.ZERO, root)
            }
            n == 1 -> {
                NumPlusSqrt(intPart, scaleP, root)
            }
            n%2 ==0-> {
                this.pow(n/2) * this.pow(n/2)
            }
            else ->     {
                this.pow((n-1)/2) * this.pow((n-1)/2) * this
            }
        }
    }
}

fun precSqrt(inp : Int, scale : Int) : BigDecimal{
    var x0 = BigDecimal("0")
    var x1 = BigDecimal(sqrt(inp.toDouble()))
    x0.setScale( scale, BigDecimal.ROUND_HALF_EVEN)
    x1.setScale(scale, BigDecimal.ROUND_HALF_EVEN)
    val inpBig = BigDecimal(inp)
    while(x0 != x1){
        x0 = x1
        x1 = inpBig.divide(x0, scale, BigDecimal.ROUND_HALF_UP)
        x1 = x1.add(x0)
        x1 = x1.divide(BigDecimal("2"), scale, BigDecimal.ROUND_HALF_UP)
    }
    return x1
}
fun findMod(pow : Int, inp : NumPlusSqrt = NumPlusSqrt(BigInteger("3"), BigInteger.ONE, 5)) : String{
    val out = inp.pow(pow)
    val int1 = out.intPart.mod(BigInteger("1000"))
    val scale = out.scaleP.toString().length +2
    println("$out")
    val sqrt = precSqrt(5, scale)
    println("$sqrt")
    val int2 = sqrt.multiply(out.scaleP.toBigDecimal()).remainder(BigDecimal("1000")).toInt()
    println("$int2")
    val output = (int1.toInt() + int2).toString()
    println(output)
    return when{
        output.length >= 3->{  output.substring(output.length-3 until  output.length)}
        else ->  "0".repeat(3 - output.length) + output
    }
}
fun main(args : Array<String>){
    val nums = File(args[0]).readLines().drop(1).map { it.toInt() }
    for ((ii, num) in nums.withIndex()){
        println("Case #${ii+1}: ${findMod(num)}")
    }
}