import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Shapes
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import javax.swing.text.TableView
import javax.swing.text.TableView.TableCell

@Composable
@Preview
fun TableScreen(calculation: Calculation) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .border(1.dp, Color.Black)
    ) {
        Column(
            modifier = Modifier.defaultMinSize(50.dp).padding(32.dp)
        ) {
            Text("Gap")
        }
        Column(
            modifier = Modifier.defaultMinSize(50.dp).padding(32.dp)
        ) {
            Text("Ni")
        }
        Column(
            modifier = Modifier.defaultMinSize(50.dp).padding(32.dp)
        ) {
            Text("Nсер..")
        }
        Column(
            modifier = Modifier.defaultMinSize(50.dp).padding(32.dp)
        ) {
            Text("Nсер.-X~")
        }
        Column(
            modifier = Modifier.defaultMinSize(50.dp).padding(32.dp)
        ) {
            Text("(Nсер.-x~)^2")
        }
        Column(
            modifier = Modifier.defaultMinSize(50.dp).padding(32.dp)
        ) {
            Text("Nнак.")
        }
    }
    calculation.tableData.forEach { gapRowData ->
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.defaultMinSize(50.dp).padding(32.dp)
            ) {
                Text("${gapRowData.gap.first}-${gapRowData.gap.second}")
            }
            Column(
                modifier = Modifier.defaultMinSize(50.dp).padding(32.dp)
            ) {
                Text("${gapRowData.n_i}")
            }
            Column(
                modifier = Modifier.defaultMinSize(50.dp).padding(32.dp)
            ) {
                Text("${gapRowData.xMiddle}")
            }
            Column(
                modifier = Modifier.defaultMinSize(50.dp).padding(32.dp)
            ) {
                Text("${gapRowData.xMidMinusX_}")
            }
            Column(
                modifier = Modifier.defaultMinSize(50.dp).padding(32.dp)
            ) {
                Text("${gapRowData.xMiddleMinusXVolnaSqr}")
            }
            Column(
                modifier = Modifier.defaultMinSize(50.dp).padding(32.dp)
            ) {
                Text("${gapRowData.nNakopl}")
            }
        }
    }
}