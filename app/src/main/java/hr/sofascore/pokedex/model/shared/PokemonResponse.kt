package hr.sofascore.pokedex.model.shared

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import hr.sofascore.pokedex.R
import java.io.Serializable

@Entity
data class PokemonResponse @JvmOverloads constructor (
    @PrimaryKey
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    @Ignore
    var types: List<PokemonType>? = arrayListOf()
) : Serializable {

    fun getFormattedId() = "%03d".format(id)
    fun getImageURL() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png"

    fun getFormattedHeight(): String {
        val heightInMeters = ("%.1f").format((height/10).toFloat())
        val heightInInches = height * 3.93701
        val heightInFeet = (heightInInches / 12).toInt()
        val inchesLeftover = ("%02d").format((heightInInches % 12).roundToInt())
        return "$heightInFeet'$inchesLeftover\" (${heightInMeters} m)"
    }

    fun getFormattedWeight(): String {
        val weightInKilograms = ("%.1f").format((weight/10).toFloat())
        val weightInPounds = ("%.1f").format((weight/4.536).toFloat())
        return "$weightInPounds lbs. (${weightInKilograms}kg)"
    }


}

data class PokemonList(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<Result>
) : Serializable

data class Result(
    val name: String,
    val url: String
) : Serializable

data class PokemonType (
    val slot: Int,
    val type: PokemonTypeDescription
): Serializable

data class PokemonTypeDescription (
    val name: String,
    val url: String
): Serializable {
    fun getTypeColor(): Int {
        when(name) {
            "bug" -> return R.color.flat_pokemon_type_bug
            "dark" -> return R.color.flat_pokemon_type_dark
            "dragon" -> return R.color.flat_pokemon_type_dragon
            "electric" -> return R.color.flat_pokemon_type_electric
            "fairy" -> return R.color.flat_pokemon_type_fairy
            "fighting" -> return R.color.flat_pokemon_type_fighting
            "fire" -> return R.color.flat_pokemon_type_fire
            "flying" -> return R.color.flat_pokemon_type_flying
            "ghost" -> return R.color.flat_pokemon_type_ghost
            "grass" -> return R.color.flat_pokemon_type_grass
            "ground" -> return R.color.flat_pokemon_type_ground
            "ice" -> return R.color.flat_pokemon_type_ice
            "normal" -> return R.color.flat_pokemon_type_normal
            "poison" -> return R.color.flat_pokemon_type_poison
            "psychic" -> return R.color.flat_pokemon_type_psychic
            "rock" -> return R.color.flat_pokemon_type_rock
            "steel" -> return R.color.flat_pokemon_type_steel
            "water" -> return R.color.flat_pokemon_type_water
        }
        return R.color.flat_pokemon_type_undefined
    }

    fun getTypeTheme(): Int {
        when(name) {
            "bug" -> return R.style.Theme_PokemonBugType
            "dark" -> return R.style.Theme_PokemonDarkType
            "dragon" -> return R.style.Theme_PokemonDragonType
            "electric" -> return R.style.Theme_PokemonElectricType
            "fairy" -> return R.style.Theme_PokemonFairyType
            "fighting" -> return R.style.Theme_PokemonFightingType
            "fire" -> return R.style.Theme_PokemonFireType
            "flying" -> return R.style.Theme_PokemonFlyingType
            "ghost" -> return R.style.Theme_PokemonGhostType
            "grass" -> return R.style.Theme_PokemonGrassType
            "ground" -> return R.style.Theme_PokemonGroundType
            "ice" -> return R.style.Theme_PokemonIceType
            "normal" -> return R.style.Theme_PokemonNormalType
            "poison" -> return R.style.Theme_PokemonPoisonType
            "psychic" -> return R.style.Theme_PokemonPsychicType
            "rock" -> return R.style.Theme_PokemonRockType
            "steel" -> return R.style.Theme_PokemonSteelType
            "water" -> return R.style.Theme_PokemonWaterType
        }
        return R.style.Theme_PokemonUndefinedType
    }
}

