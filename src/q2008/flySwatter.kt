package q2008

import java.io.File
import kotlin.math.sqrt
import kotlin.random.Random

fun flySwatProb(outRad : Double, thick : Double, width : Double, spacing : Double, flyRad : Double ) : Double{
    val nSims = 10_000_000
    var escCount = 0
    var simCout =0
    repeat(nSims){

        val x = Random.nextDouble(0.0, outRad - thick -flyRad)
        val y = Random.nextDouble(0.0, outRad - thick - flyRad)
        if (sqrt(x*x + y*y) < outRad - thick - flyRad){
            simCout++
            val xRem = x % (width + spacing)
            val yRem = y % (width + spacing)
            if ( (xRem > width/2 + flyRad) and (yRem > width/2 + flyRad) and
                (xRem < spacing + width/2 -flyRad) and (yRem < spacing + width/2 -flyRad)){
                escCount++
            }
        }
    }
    if (simCout == 0){
        return 1.0
    }
    return 1.0 - escCount.toDouble()/simCout.toDouble()*(outRad - thick - flyRad)*
            (outRad - thick - flyRad)/outRad/outRad
}
fun main(args : Array<String>){
    val lines = File(args[0]).readLines()
    var caseNo =1
    for (line in lines.slice(1 until lines.size)) {
        val doubles = line.split(' ', '\t').map { it.toDouble() }
        val out = flySwatProb(
            flyRad = doubles[0],
            outRad = doubles[1],
            thick = doubles[2],
            width = doubles[3] * 2,
            spacing = doubles[4]
        )
        println("Case #$caseNo: $out")
        caseNo++
    }
}