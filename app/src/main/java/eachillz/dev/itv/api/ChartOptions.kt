package eachillz.dev.itv.adapter

enum class Metric {
    NEGATIVE, POSITIVE, DEATH
}

enum class TimeScale( val numDays: Int){
    WEEK( 7),
    MONTH( 30),
    MAX(-1 )
}