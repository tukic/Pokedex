package hr.sofascore.pokedex.model.shared

import android.content.Context
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.util.LanguageHelper
import java.io.Serializable

data class PokemonType (
    val pokemon: List<PokemonTypeDetail>,
    val damage_relations: DamageRelations,
    val name: String,
    val names: List<LanguageDescription>,
    val moves: List<Result>
): Serializable {
    fun getName(context: Context): String {
        val language = LanguageHelper.getPreferredLanguage(context)
        return names.find { it.language.name == language }?.name ?: this.name
    }
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

data class PokemonTypeDetail (
    val pokemon: Result
): Serializable

data class DamageRelations (
    val double_damage_to: List<PokemonTypeDescription>,
    val double_damage_from: List<PokemonTypeDescription>,
    val half_damage_to: List<PokemonTypeDescription>,
    val half_damage_from: List<PokemonTypeDescription>,
    val no_damage_to: List<PokemonTypeDescription>,
    val no_damage_from: List<PokemonTypeDescription>
): Serializable

data class TypeList(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<Result>
)