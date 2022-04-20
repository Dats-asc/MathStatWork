import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sqrt

class Calculation(
    val rawData: List<Float>
) {

    val n = rawData.count()

    val xMin by lazy {
        var min = Float.MAX_VALUE
        rawData.forEach { n ->
            if (n < min)
                min = n
        }
        min
    }

    val xMax by lazy {
        var max = Float.MIN_VALUE
        rawData.forEach { n ->
            if (n > max)
                max = n
        }
        max
    }

    val gapsCount = (1 + 3.322 * log10(n.toFloat())).toInt()

    val gapSize = round((xMax - xMin) / gapsCount)

    val tableData by lazy {
        val data = mutableListOf<GapRowData>()
        for (i in 0 until gapsCount) {
            data.add(GapRowData())
        }
        data
    }

    var xVolna: Float? = null

    var dispersion: Float? = null

    var gamma = 0.95f

    var l1 :Float? = null

    var l2:Float? = null

    var XSqrL1 = 45.7f
    var XSqrL2 = 16f

    var t: Float = 0f

    var sigmaSqr_: Pair<Float, Float>? = null

    var x_: Pair<Float, Float>? = null

    var M0: Float? = null
    var Me: Float? = null

    var A3: Float? = null

    var Ek: Float? = null

    init {
        setGaps()
        setNiColumn()
        setXMiddle()
        setxVolna()
        setxMidMinusX()
        setxMiddleMinusXVolnaSqr()
        setNNakopl()
        setDispertion()
        setT()
        setSigmaSqr_()
        setX_()
        setL1L2()
        setM0AndMe()
        setA3()
        setEk()
    }


    private fun setGaps() {
        var left = xMin
        tableData.forEach { gapRowData ->
            gapRowData.gap = round(left) to round(left + gapSize)
            left += gapSize
        }
    }

    private fun setNiColumn() {
        var count = 0
        tableData.forEach { gapRowData ->
            gapRowData.n_i = 0
            for (i in 0 until n) {
                if (rawData[i] >= gapRowData.gap.first && rawData[i] < gapRowData.gap.second) {
                    gapRowData.n_i++
                    count++
                }
            }
        }
        if (count != n) {
            tableData[tableData.lastIndex].n_i++
        }
    }

    private fun setXMiddle() {
        tableData.forEach { gapRowData ->
            gapRowData.xMiddle = round(gapRowData.gap.first + (gapRowData.gap.second - gapRowData.gap.first) / 2)
        }
    }

    private fun setxVolna() {
        var result = 0f
        tableData.forEach { gapRow ->
            result += gapRow.xMiddle * gapRow.n_i
        }
        xVolna = round(result / n)
    }

    private fun setxMidMinusX() {
        tableData.forEach { gapRow ->
            gapRow.xMidMinusX_ = round(gapRow.xMiddle - xVolna!!)
        }
    }

    private fun setxMiddleMinusXVolnaSqr() {
        tableData.forEach { gapRow ->
            gapRow.xMiddleMinusXVolnaSqr = round(gapRow.xMidMinusX_ * gapRow.xMidMinusX_)
        }
    }

    private fun setNNakopl() {
        var nNakopl = 0
        tableData.forEach { gapRow ->
            nNakopl += gapRow.n_i
            gapRow.nNakopl = nNakopl
        }
    }

    private fun setDispertion() {
        var result = 0f
        tableData.forEach { gapRow ->
            result += gapRow.xMiddleMinusXVolnaSqr * gapRow.n_i
        }
        dispersion = round(result / 30)
    }

    private fun setSigmaSqr_() {
        val left = round(((n - 1) * dispersion!!) / XSqrL1)
        val right = round(((n - 1) * dispersion!!) / XSqrL2)
        sigmaSqr_ = left to right

    }

    private fun setX_() {
        var left = (xVolna!! - (t * sqrt(dispersion!!)) / sqrt(n.toFloat()))
        var right = (xVolna!! + (t * sqrt(dispersion!!)) / sqrt(n.toFloat()))
        x_ = left to right
    }

    private fun setM0AndMe() {
        val x0 = tableData.first().gap.second
        val nm = tableData[tableData.lastIndex].n_i
        var max = Int.MIN_VALUE
        var m = 0
        for (i in 0 until tableData.size) {
            if (tableData[i].n_i > max) {
                max = tableData[i].n_i
                m = i
            }
        }
        if (m == 0)
            m++
        if (m == tableData.size - 1)
            m--

        with(tableData) {
            M0 =
                x0 + ((this[m].n_i - this[m - 1].n_i) / ((this[m].n_i + this[m - 1].n_i) * (this[m].n_i - this[m + 1].n_i))) * gapSize
            Me = x0 + (((0.5 * n - this[m - 1].nNakopl) * gapSize) / this[m].n_i).toFloat()
        }
    }

    private fun setA3() {
        var m3 = 0f
        var sigma = 0f
        tableData.forEach { gapRow ->
            m3 += ((gapRow.xMiddle - xVolna!!).pow(3) - gapRow.n_i) / n
            sigma += ((gapRow.xMiddle - xVolna!!)).pow(3) * gapRow.n_i
        }
        A3 = m3 / (sigma / n)
    }

    private fun setEk() {
        var m4 = 0f
        var sigma = 0f
        tableData.forEach { gapRow ->
            m4 += ((gapRow.xMiddle - xVolna!!).pow(4) - gapRow.n_i) / n
            sigma += ((gapRow.xMiddle - xVolna!!)).pow(4) * gapRow.n_i
        }
        Ek = (m4 / sigma / n) - 3
        val a = 0
    }

    private fun setL1L2(){
        l1 = (1 - gamma) / 2f
        l2 = (1 + gamma) / 2f
    }

    private fun setT() {
        when (n) {
            5 -> t = 2.78f
            6 -> t = 2.57f
            7 -> t = 2.45f
            8 -> t = 2.37f
            9 -> t = 2.31f
            10 -> t = 2.26f
            11 -> t = 2.23f
            12 -> t = 2.20f
            13 -> t = 2.18f
            14 -> t = 2.16f
            15 -> t = 2.15f
            16 -> t = 2.13f
            17 -> t = 2.12f
            18 -> t = 2.11f
            19 -> t = 2.10f
            20 -> t = 2.093f
            in 20 until 25 -> 2.093f
            in 25 until 30 -> 2.064f
            in 30 until 35 -> 2.045f
            in 40 until 45 -> 2.023f
            in 45 until 50 -> 2.016f
            in 50 until 60 -> 2.009f
            in 60 until 70 -> 2.001f
            in 70 until 80 -> 1.996f
            in 80 until 90 -> 1.001f
            in 90 until 100 -> 1.987f
            in 100 until 120 -> 1.984f
            120 -> 1.980f
            else -> 1.960f
        }
    }

    private fun round(value: Float) = "%.2f".format(value).toFloat()
}

class GapRowData() {
    var gap: Pair<Float, Float> = 0f to 0f
    var n_i: Int = 0
    var xMiddle: Float = 0f
    var xMidMinusX_: Float = 0f
    var xMiddleMinusXVolnaSqr = 0f
    var nNakopl = 0
}
