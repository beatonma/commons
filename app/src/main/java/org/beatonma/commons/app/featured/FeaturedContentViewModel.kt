package org.beatonma.commons.app.featured

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import org.beatonma.commons.app
import org.beatonma.commons.app.signin.BaseUserAccountViewModel
import org.beatonma.commons.repo.repository.SnommocRepository
import org.beatonma.commons.repo.repository.UserRepository
import org.beatonma.commons.repo.repository.ZeitgeistRepository
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.snommoc.models.MessageOfTheDay

class FeaturedContentViewModel @ViewModelInject constructor(
    snommocRepository: SnommocRepository,
    zeitgeistRepository: ZeitgeistRepository,
    userRepository: UserRepository,
    @ApplicationContext application: Context,
) : BaseUserAccountViewModel(userRepository, application.app) {

    val zeitgeist = zeitgeistRepository.getZeitgeist()
    val motd: LiveData<IoResult<List<MessageOfTheDay>>> = snommocRepository.getMotd().asLiveData()
}
