package me.kythera.deathswap.custom

import kotlin.random.Random

sealed interface Mode {
    data object FIXED : Mode
    data object RANDOM : Mode
}

sealed interface ConfigData<T : Mode> {
    val mode: T
    fun getNextSwapTicks(): Long
}

data class FixedConfigData(
    val time: Long
) : ConfigData<Mode.FIXED> {
    override val mode = Mode.FIXED
    override fun getNextSwapTicks(): Long = time
}

data class RandomConfigData(
    val min: Long,
    val max: Long
) : ConfigData<Mode.RANDOM> {
    override val mode = Mode.RANDOM
    override fun getNextSwapTicks(): Long = Random.nextLong(min, max + 1)
}
