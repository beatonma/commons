package org.beatonma.commons.app.ui.compose.components

/**
 * Copyright (C) 2020 Wasabeef
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Lightly modified from https://github.com/wasabeef/composable-images.
 */

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.FrameManager
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageAsset
import androidx.compose.ui.graphics.asImageAsset
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.platform.ContextAmbient
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GlideImage(
    @DrawableRes drawableResId: Int,
    modifier: Modifier = Modifier.fillMaxWidth(),
    options: RequestOptions = RequestOptions(),
) {
    GlideImagePrivate(model = drawableResId, modifier = modifier, options = options)
}

@Composable
fun GlideImage(
    source: String?,
    modifier: Modifier = Modifier.fillMaxWidth(),
    options: RequestOptions = RequestOptions(),
) {
    GlideImagePrivate(model = source, modifier = modifier, options = options)
}

@Composable
private fun GlideImagePrivate(
    model: Any?,
    modifier: Modifier = Modifier.fillMaxWidth(),
    options: RequestOptions = RequestOptions(),
) {
    WithConstraints(modifier) {
        val context = ContextAmbient.current

        val width =
            if (constraints.maxWidth > 0 && constraints.maxWidth < Int.MAX_VALUE) constraints.maxWidth
            else SIZE_ORIGINAL
        val height =
            if (constraints.maxHeight > 0 && constraints.maxHeight < Int.MAX_VALUE) constraints.maxHeight
            else SIZE_ORIGINAL

        val image = remember { mutableStateOf<ImageAsset?>(null) }
        val drawable = remember { mutableStateOf<Drawable?>(null) }

        onCommit(model) {
            val target: CustomTarget<Bitmap> = object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                    image.value = null
                    drawable.value = placeholder
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    FrameManager.ensureStarted()
                    image.value = resource.asImageAsset()
                }
            }
            val job = CoroutineScope(Dispatchers.IO).launch {
                Glide.with(context)
                    .asBitmap()
                    .load(model)
                    .override(width, height)
                    .apply(options)
                    .into(target)
            }

            onDispose {
                image.value = null
                drawable.value = null
                if (context is Activity && !context.isDestroyed) {
                    Glide.with(context).clear(target)
                }
                job.cancel()
            }
        }

        if (image.value != null) {
            Image(asset = image.value!!, modifier = modifier)
        }
        else if (drawable.value != null) {
            Canvas(modifier = modifier) {
                drawIntoCanvas { canvas -> drawable.value!!.draw(canvas.nativeCanvas) }
            }
        }
    }
}
