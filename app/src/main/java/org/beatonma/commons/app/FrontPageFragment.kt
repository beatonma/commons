package org.beatonma.commons.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.R
import org.beatonma.commons.app.featured.FeaturedContent
import org.beatonma.commons.app.featured.FeaturedContentViewModel
import org.beatonma.commons.data.core.room.entities.member.FeaturedMemberProfile
import org.beatonma.commons.kotlin.extensions.bundle
import org.beatonma.commons.kotlin.extensions.navigateTo
import org.beatonma.commons.repo.IoResultList
import org.beatonma.commons.repo.result.LoadingResult
import org.beatonma.commons.repo.result.isLoading
import org.beatonma.commons.repo.result.isSuccess
import org.beatonma.commons.theme.compose.theme.SystemBars
import org.beatonma.commons.theme.compose.theme.systemui.ProvideDisplayInsets
import org.beatonma.commons.theme.compose.theme.systemui.SystemUiController
import org.beatonma.commons.theme.compose.theme.systemui.SystemUiControllerAmbient
import org.beatonma.commons.theme.compose.theme.systemui.statusBarsHeight

@AndroidEntryPoint
class FrontPageFragment : Fragment() {

    private val viewmodel: FeaturedContentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = composeView {
        withSystemUi(window = requireActivity().window) {
            val result by viewmodel.featuredPeople.collectAsState(LoadingResult())
            Column {
                Spacer(modifier = Modifier.statusBarsHeight())
                FeaturedPeopleResult(result)
            }
        }
    }

    @Composable
    fun FeaturedPeopleResult(result: IoResultList<FeaturedMemberProfile>) =
        when {
            result.isSuccess && result.data != null -> {
                FeaturedContent(
                    people = result.data!!,
                    personOnClick = { profile ->
                        requireView().navigateTo(
                            R.id.action_frontPageFragment_to_memberProfileFragment,
                            profile.bundle()
                        )
                    }
                )
            }

            result.isLoading -> Loading()
            else -> Error()
        }
}

fun Fragment.composeView(content: @Composable () -> Unit) = ComposeView(requireContext()).apply {
    setContent {
        content()
    }
}

@Composable
fun Loading(
    modifier: Modifier = Modifier,
) {
    Text("Loading", style = MaterialTheme.typography.h1)
}

@Composable
fun Error(
    modifier: Modifier = Modifier,
) {
    Text("Error", style = MaterialTheme.typography.h1)
}

@Composable
fun ComposeView.setContentWithSystemUi(
    window: Window,
    systemBarColor: Color = MaterialTheme.colors.SystemBars(),
    content: @Composable () -> Unit,
) {
    setContent {
        val systemUiController = remember { SystemUiController(window) }
        Providers(SystemUiControllerAmbient provides systemUiController) {
            SystemUiControllerAmbient.current.setSystemBarsColor(systemBarColor)
            content()
        }
    }
}

@Composable
fun withSystemUi(
    window: Window,
    systemBarColor: Color = MaterialTheme.colors.SystemBars(),
    content: @Composable () -> Unit,
) {
    val systemUiController = remember { SystemUiController(window) }

    ProvideDisplayInsets {
        Providers(SystemUiControllerAmbient provides systemUiController) {
            SystemUiControllerAmbient.current.setSystemBarsColor(systemBarColor)
            content()
        }
    }
}
