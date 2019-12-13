package i.numan.multisimselection

import android.content.Context

object Resources {
    private const val SIM0 = 0
    private const val SIM1 = 1
    fun getSIM0(context: Context): Int {
        return SIM0
    }

    fun getSIM1(context: Context): Int {
        return SIM1
    }
}