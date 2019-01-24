package q2008

import java.io.File
//Google code jam 2008 qualification 1a. Find number of tims the search enging used has to be switched,

fun readInput(fn : String): Pair<List<Set<String>>, List<List<String>>>{
    val out = Pair(mutableListOf<Set<String>>(), mutableListOf<List<String>>())
    val lines = File(fn).readLines()

    val nCase = lines[0].toInt()
    var currInd = 1
    repeat(nCase){
        val nSearch = lines[currInd].toInt()
        out.first.add(setOf(*(lines.slice(currInd+1..currInd+nSearch).toTypedArray())))
        currInd  += nSearch +1
        val nQ = lines[currInd].toInt()
        out.second.add(lines.slice(currInd+1..currInd+nQ))
        currInd += nQ +1
    }
    return out
}

// Algorithm used: At the start of the queries the search engine could be anything. As you encounter
// search engines in the query stresm, your options reduce. If your options become empty, you have to
// switch, and you have the option to switch to any  search engine other than the current query.
// Options modeled as a set, for efficient removal of current query.
fun findSwitch(searchEng : Set<String>, queries : List<String>):Int{
    var out = 0
    var options = mutableSetOf<String>()
    options.addAll(searchEng)
    for (query in queries){
        options.remove(query)
        if (options.isEmpty()){
            out +=1
            options.addAll(searchEng)
            options.remove(query)
        }
    }
    return out
}

fun main(args : Array<String>){
    val input = readInput(args[0])
    for ((ind, query) in input.second.withIndex()){
        println("Case #${ind+1}: ${findSwitch(input.first[ind], query)}")
    }
}