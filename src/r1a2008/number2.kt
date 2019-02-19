package r1a2008

import java.io.File

// this is based on the insight that (3+sqrt(5)^N + (3-sqrt(5)^N is an integer, and since (3-sqrt(5)) is less than 1,
// the integer part of (3+sqrt(5))^N is just (3+sqrt(5)^N + (3-sqrt(5)^N -1
data class NumPlusSqrtMod(val intPart : Int, val scaleP : Int, val root : Int = 5, val mod :Int = 1000){
    operator fun times(other : NumPlusSqrtMod): NumPlusSqrtMod{
        assert(root == other.root)

        val out =  NumPlusSqrtMod((intPart * other.intPart +
                scaleP*other.scaleP*root)%mod, (intPart*other.scaleP + other.intPart*scaleP)%mod, mod = mod)
        if (DEBUG) {
            println("($this) * ($other) = $out")
        }
        return out
    }
    operator fun plus(other: NumPlusSqrtMod) : NumPlusSqrtMod{
        assert(root == other.root)
        return NumPlusSqrtMod(
            (intPart + other.intPart) %mod,
            (scaleP + other.scaleP) % mod,
            root,
            mod
        )
    }
    override fun toString(): String = "$intPart + $scaleP SQRT($root)"

    fun pow(n : Int) : NumPlusSqrtMod{
        return  when{
            n == 0 -> {
                NumPlusSqrtMod(1, 0, root, mod)
            }
            n == 1 -> {
                NumPlusSqrtMod(intPart, scaleP, root, mod)
            }
            n ==2 ->{
                this *this
            }
            n%2 ==0-> {
                this.pow(n/2).pow(2)
            }
            else ->     {
                this.pow((n-1)/2).pow(2) * this
            }
        }
    }
}
fun findMod2(pow : Int, num : NumPlusSqrtMod = NumPlusSqrtMod(3, 1)): String{
    val out1 = num.pow(pow)
    val out2 = NumPlusSqrtMod(num.intPart, -num.scaleP, num.root, num.mod).pow(pow)
    return  ((out1.intPart + out2.intPart -1) % num.mod).toString().padStart(3, '0')

}

fun main(args : Array<String>){
    val nums = File(args[0]).readLines().drop(1).map { it.toInt() }
    for ((ii, num) in nums.withIndex()){
        println("Case #${ii+1}: ${findMod2(num)}")
    }
}

