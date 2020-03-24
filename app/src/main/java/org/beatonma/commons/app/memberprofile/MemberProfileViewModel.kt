package org.beatonma.commons.app.memberprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.beatonma.commons.data.IoResult
import org.beatonma.commons.data.core.CommonsRepository
import org.beatonma.commons.data.core.room.entities.MemberProfile
import javax.inject.Inject


class MemberProfileViewModel @Inject constructor(
    private val repository: CommonsRepository
) : ViewModel() {
    lateinit var member: LiveData<IoResult<MemberProfile>>

    fun forMember(parliamentdotuk: Int) {
        member = repository.observeMember(parliamentdotuk)
    }
}
