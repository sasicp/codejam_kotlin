package r1a2008

import java.io.File

fun minScalarProd(fn: String){
    val lines  = File(fn).readLines()
    val nT = lines[0].toInt()
    var currLine = 1
    for(ii in (1..nT)){
        val vals1 = lines[currLine+1].split(' ','\t').map { it.toLong() }.sorted()
        val vals2 = lines[currLine+2].split(' ','\t').map { it.toLong() }.sortedDescending()
        val minSc = vals1.foldIndexed(0){ind, acc: Long, it:Long -> acc + it*vals2[ind]}
        println("Case #$ii : $minSc")
        currLine +=3
    }
}

fun main(args: Array<String>){
    minScalarProd(args[0])
}