package q2008

import java.io.File

data class TimeTableEntry(val hr : Int, val min :Int){
    operator fun plus(other : TimeTableEntry): TimeTableEntry{
        val outHr = hr + other.hr
        val outMin = min + other.min
        if(outMin >= 60){
            return  TimeTableEntry(outHr+1, outMin - 60)
        }
        return TimeTableEntry(hr = outHr, min = outMin)
    }
}
enum class EvType {
    AVAILABLE, DEPARTURE
}
data class Event(val typ : EvType , val time : TimeTableEntry)
fun readInp(fn : String): List<Pair<List<Event>, List<Event>>>{
    val lines = File(fn).readLines()
    val nCase = lines[0].toInt()
    var currLine = 1
    val out = mutableListOf<Pair<List<Event>, List<Event>>>()
    repeat(nCase){
        val tt = lines[currLine].toInt()
        val nAB = lines[currLine+1].split(' ','\t','\r','\n').take(2)
        val nA = nAB[0].toInt(); val nB = nAB[1].toInt()
        currLine +=2
        val eventsA = mutableListOf<Event>()
        val eventsB = mutableListOf<Event>()
        repeat(nA){
            val times =  lines[currLine+it].split(' ','\t').take(2)
            val depTime = TimeTableEntry(times[0].split(':')[0].toInt(),
                times[0].split(':')[1].toInt())
            val avTime = TimeTableEntry(times[1].split(':')[0].toInt(),
                times[1].split(':')[1].toInt()) + TimeTableEntry(0, tt)
            eventsA.add(Event(EvType.DEPARTURE, depTime))
            eventsB.add(Event(EvType.AVAILABLE, avTime))
        }
        currLine +=nA
        repeat(nB){
            val times =  lines[currLine+it].split(' ','\t').take(2)
            val depTime = TimeTableEntry(times[0].split(':')[0].toInt(),
                times[0].split(':')[1].toInt())
            val avTime = TimeTableEntry(times[1].split(':')[0].toInt(),
                times[1].split(':')[1].toInt()) + TimeTableEntry(0, tt)
            eventsB.add(Event(EvType.DEPARTURE, depTime))
            eventsA.add(Event(EvType.AVAILABLE, avTime))
        }
        currLine += nB
        eventsA.sortWith(compareBy({it.time.hr}, {it.time.min}))
        eventsB.sortWith(compareBy({it.time.hr}, {it.time.min}))
        out.add(Pair(eventsA, eventsB))
    }
    return out
}
fun printNTrains(case : Int, eventsA: List<Event>, eventsB : List<Event>){
    var nTrainsA = 0
    var nTrainsB =0
    var nTrainsAvailA = 0
    var nTrainsAvailB = 0
    for (ev in eventsA){
        if(ev.typ == EvType.AVAILABLE){
            nTrainsAvailA +=1
        }else{
            if(nTrainsAvailA == 0){
                nTrainsA +=1
            }else{
                nTrainsAvailA -= 1
            }
        }
    }
    for (ev in eventsB){
        if(ev.typ == EvType.AVAILABLE){
            nTrainsAvailB +=1
        }else{
            if(nTrainsAvailB == 0){
                nTrainsB +=1
            }else{
                nTrainsAvailB -= 1
            }
        }
    }
    println ("Case #$case: $nTrainsA $nTrainsB")
}
fun main(args : Array<String>){
    val tc = readInp(args[0])
    for((ind, case) in tc.withIndex()){
        printNTrains(ind+1, case.first, case.second)
    }
}