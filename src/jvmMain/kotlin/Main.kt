// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.io.BufferedReader
import java.io.File

@Composable
@Preview
fun App() {

    val rawData = readRawDataFile()
    val calculation = Calculation(rawData)

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Column {
                Text("Xmin = ${calculation.xMin}", modifier = Modifier.padding(8.dp))
            }
            Column {
                Text("Xmin = ${calculation.xMax}", modifier = Modifier.padding(8.dp))
            }
            Column {
                Text("X~ = ${calculation.xVolna}", modifier = Modifier.padding(8.dp))
            }
            Column {
                Text("Dispertion = ${calculation.dispersion}", modifier = Modifier.padding(8.dp))
            }
            Column {
                Text("Gamma = ${calculation.gamma}", modifier = Modifier.padding(8.dp))
            }
            Column {
                Text("l1 = ${calculation.l1}", modifier = Modifier.padding(8.dp))
            }
            Column {
                Text("l2 = ${calculation.l2}", modifier = Modifier.padding(8.dp))
            }
            Column {
                Text("M0 = ${calculation.M0}", modifier = Modifier.padding(8.dp))
            }
            Column {
                Text("Me = ${calculation.Me}", modifier = Modifier.padding(8.dp))
            }
            Column {
                Text("A3 = ${calculation.A3}", modifier = Modifier.padding(8.dp))
            }
            Column {
                Text("Ek = ${calculation.Ek}", modifier = Modifier.padding(8.dp))
            }
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TableScreen(calculation)
            }

            Column(
                modifier = Modifier.fillMaxSize().padding(500.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {

                    val canvasWidth = 900f
                    val canvasHeight = 900f

                    var max = Int.MIN_VALUE
                    for (i in 0 until calculation.tableData.size) {
                        if (calculation.tableData[i].n_i > max) {
                            max = calculation.tableData[i].n_i
                        }
                    }

                    drawLine(
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = 0f, y = -1000f),
                        color = Color.Black,
                        strokeWidth = 3F
                    )
                    drawLine(
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = 1000f, y = 0f),
                        color = Color.Black,
                        strokeWidth = 3F
                    )

                    var rectHeight = canvasHeight * (calculation.tableData[0].n_i / max.toFloat())
                    var rectWidth = (canvasWidth) / calculation.tableData.size
                    var rectStartX= 0f
                    var rectStartY = -rectHeight
                    for(i in 0 until calculation.tableData.size){
                        drawRect(
                            color = Color.Red,
                            topLeft = Offset(x = rectStartX, y = rectStartY),
                            size = Size(width = rectWidth, height = rectHeight),
                        )
                        if (i == calculation.tableData.size - 1){
                            break
                        }
                        rectHeight = canvasHeight * (calculation.tableData[i+1].n_i / max.toFloat())
                        rectStartY = -rectHeight
                        rectStartX += rectWidth
                    }
                }
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

fun readRawDataFile(): List<Float> {
    val rawData = mutableListOf<Float>()
    val dataSetFilePath = "/Users/rustamhajrullin/Downloads/data.txt"

    val bufferedReader: BufferedReader = File(dataSetFilePath).bufferedReader()
    val inputString = bufferedReader.use { it.readText() }
    val lines = inputString.split("\n")

    try {
        lines.forEach { line ->
            rawData.add(line.split(",")[0].toFloat())
        }
    } catch (e: java.lang.Exception) {
        println("File parse exc.\t${e.message.toString()}")
    }

    return rawData
}
