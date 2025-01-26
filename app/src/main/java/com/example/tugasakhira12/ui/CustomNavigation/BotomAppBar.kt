package com.example.tugasakhira12.ui.CustomNavigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tugasakhira12.R

@Composable
fun CustomBottomAppBar(
    onHomeClick: () -> Unit,
    onSiswaClick: () -> Unit,
    onInstrukturClick: () -> Unit,
    onKursusClick: () -> Unit,
    onPendaftaranClick: () -> Unit,
    selectedItem: Int = 0,
    modifier: Modifier = Modifier
) {
    Row(
        Modifier
            .fillMaxWidth().background(color = colorResource(id = R.color.biruFoot))
                .padding(20.dp)
            .clip(shape = RoundedCornerShape(20.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        BottomBarIcon(
            imageResource = R.drawable.home,
            isSelected = selectedItem == 0
        ) { onHomeClick() }

        BottomBarIcon(
            imageResource = R.drawable.student,
            isSelected = selectedItem == 1
        ) { onSiswaClick() }

        BottomBarIcon(
            imageResource = R.drawable.teacher,
            isSelected = selectedItem == 2
        ) { onInstrukturClick() }

        BottomBarIcon(
            imageResource = R.drawable.kurss,
            isSelected = selectedItem == 3
        ) { onKursusClick() }

        BottomBarIcon(
            imageResource = R.drawable.pendf,
            isSelected = selectedItem == 4
        ) { onPendaftaranClick() }

    }
}

@Composable
fun BottomBarIcon(
    imageResource: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(4.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = "",
            modifier = Modifier.size(30.dp),

        )

    }
}

@Composable
fun CustomBottomAppBarPreview() {
    var selectedItem by remember { mutableStateOf(0) }

    CustomBottomAppBar(
        onHomeClick = { selectedItem = 0 },
        onSiswaClick = { selectedItem = 1 },
        onInstrukturClick = { selectedItem = 2},
        onKursusClick = { selectedItem = 3 },
        onPendaftaranClick = { selectedItem = 4},
        selectedItem = selectedItem
    )
}

