package com.studentos.app

import android.app.Application
import com.studentos.app.data.Graph

class StudentOSApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}
