package org.beatonma.commons.app.memberprofile.compose

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import org.beatonma.commons.app
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.repo.repository.MemberRepository

class ComposeMemberProfileViewModel
@ViewModelInject constructor(
    private val repository: MemberRepository,
    @ApplicationContext context: Context,
): AndroidViewModel(context.app) {

    fun forMember(parliamentID: ParliamentID) =
        repository.getMember(parliamentID)
}
