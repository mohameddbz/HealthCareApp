package com.example.projecttdm.ui.common

import android.content.Context
import android.widget.Button

class ButtonComponent(context: Context) : Button(context) {
    init {
        text = "Click Me"
    }
}