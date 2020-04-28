package org.beatonma.commons.app.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import org.beatonma.commons.app.dagger.Injectable
import javax.inject.Inject


abstract class BaseViewmodelFragment : Fragment(), Injectable {
    @Inject
    protected lateinit var viewmodelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}
