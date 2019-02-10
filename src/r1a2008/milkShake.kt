package r1a2008

import java.io.File

//Algorithm: This is an application of the SAT problem, with HORN terems. The problem has a linear time solution. The
// algorithm proceeds as follows. We start off with everything non-malted. If there are no constraints that have 0
// non-malted options then, we are done. If not, for the the constraint with 0 non-malted options, if there is a
// malting option, we set the malting option =1, if not there are conflicting constraints. When we set malting=1, we
// reduce the number of non-malting options for others, so we need to track that.
fun readSolveTC(lines :List<String>, caseNo : Int) : Int{
    val nFlav = lines[0].toInt()
    val nCust = lines[1].toInt()
    data class CustFlavs(val ind : Int, val malted : Int, val nMalted :MutableSet<Int>)
    var currLine = 2
   // we have 2 sets 1 set gr
    val custByNopts = mutableMapOf<Int,MutableSet<CustFlavs>>()
    val custByNonMaltOpts = mutableMapOf<Int, MutableSet<CustFlavs>>()
    custByNopts[0] = mutableSetOf()
    for(ii in (0 until nCust)){
        val custPerfs = lines[currLine].split(' ','\t')
            .map{it.toInt()}.drop(1).chunked(2)
        val malt = custPerfs.filter { it[1] ==1 }
        assert(malt.size <= 1)
        val nonMalt = custPerfs.filter { it[1] ==0 }.map{it[0]}.toMutableSet()
        currLine++
        val cust = CustFlavs(if(malt.isNotEmpty()) malt[0][1] else -1, ii, nonMalt)
        custByNopts.getOrPut(nonMalt.size){mutableSetOf()}.add(cust)
        for (flav in nonMalt){
            custByNonMaltOpts.getOrPut(flav){mutableSetOf()}.add(cust)
        }
    }
    val malted  = mutableSetOf<Int>()
   //while there are customers with no non-malted options
    while(!(custByNopts[0]).isNullOrEmpty()){
        val cust = custByNopts[0]!!.first()
        custByNopts[0]!!.remove(cust)
        if (cust.malted ==-1){
            println("Case $caseNo: IMPOSSIBLE")
            return currLine
        }
        malted.add(cust.malted)

        for(custR in custByNonMaltOpts.remove(cust.malted)?: mutableSetOf()) {
            val len = custR.nMalted.size
            custR.nMalted.remove(cust.malted)
            custByNopts[len]!!.remove(custR)
            custByNopts.getOrPut(len - 1) { mutableSetOf() }.add(custR)
        }
    }
    val out = (1..nFlav).joinToString(" ") { if (malted.contains(it)) "1" else "0" }
    println("Case $caseNo: $out")
    return currLine
}

fun main(args : Array<String>){
    val lines = File(args[0]).readLines()
    val nCase = lines[0].toInt()
    var currLine = 1
    for (caseInd in 1..nCase){
        currLine += readSolveTC(lines.slice(currLine until lines.size), caseInd)
    }
}