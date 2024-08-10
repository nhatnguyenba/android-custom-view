package com.nhatnguyenba.custom_view

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.nhatnguyenba.android_custom_view.R
import com.nhatnguyenba.custom_view.ui.custom.BarChart

class MainActivity : ComponentActivity() {
    private val chartView: BarChart by lazy {
        findViewById(R.id.bar_chart)
    }

    private val listItems = mutableListOf<BarChart.Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listItems.add(BarChart.Item("Michael", 10f))
        listItems.add(BarChart.Item("Emily", 15f))
        listItems.add(BarChart.Item("James", 5f))
        listItems.add(BarChart.Item("Olivia", 20f))

        chartView.setItems(listItems)
    }
}