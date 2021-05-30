package hr.sofascore.pokedex.util

class OrdinalNumberHelper {
    companion object {
        fun ordinalOf(i: Int) = "$i" + if (i % 100 in 11..13) "TH" else when (i % 10) {
            1 -> "ST"
            2 -> "ND"
            3 -> "RD"
            else -> "TH"
        }
    }
}