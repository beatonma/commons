@file:JvmName("SvgActivity")

package org.beatonma.commons.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.beatonma.commons.R

class SvgActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_svg)
    }
}
