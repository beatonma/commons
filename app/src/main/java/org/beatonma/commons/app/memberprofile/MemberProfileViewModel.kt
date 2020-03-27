package org.beatonma.commons.app.memberprofile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.beatonma.commons.CommonsApplication
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.datarendering.formatDate
import org.beatonma.commons.data.IoResult
import org.beatonma.commons.data.core.CommonsRepository
import org.beatonma.commons.data.core.Member
import org.beatonma.commons.data.core.room.entities.CommitteeMembership
import org.beatonma.commons.data.core.room.entities.Post
import org.beatonma.commons.data.core.room.entities.Town
import org.beatonma.commons.data.core.room.entities.WebAddress
import org.beatonma.lib.util.kotlin.extensions.autotag
import org.beatonma.lib.util.kotlin.extensions.stringCompat
import java.util.*
import javax.inject.Inject


class MemberProfileViewModel @Inject constructor(
    private val repository: CommonsRepository,
    private val application: CommonsApplication
) : AndroidViewModel(application) {

    // Data
    lateinit var member: LiveData<IoResult<Member>>
    lateinit var weblinks: LiveData<List<WebAddress>>
    lateinit var posts: LiveData<List<Post>>
    lateinit var committeeMemberships: LiveData<List<CommitteeMembership>>

    val snippets: MutableLiveData<List<Snippet>> = MutableLiveData()

    private val observer = Observer<Any> { generateSnippets() }
    private val calendar: Calendar = Calendar.getInstance()


    fun forMember(parliamentdotuk: Int) {
        member = repository.observeMember(parliamentdotuk)
        member.observeForever(observer)

        weblinks = repository.observeWebAddresses(parliamentdotuk)
        weblinks.observeForever(observer)

        posts = repository.observePosts(parliamentdotuk)
        posts.observeForever(observer)

        committeeMemberships = repository.observeCommitteeMemberships(parliamentdotuk)
        committeeMemberships.observeForever(observer)
    }

    override fun onCleared() {
        arrayOf(
            member,
            weblinks,
            posts
        ).forEach { it.removeObserver(observer) }
        super.onCleared()
    }


    private fun generateSnippets() {
        val member = member.value?.data ?: return

        viewModelScope.launch {
            Log.i(autotag, "Shuffling snippets")
            snippets.value = listOfNotNull(
                *memberSnippets(member),
                *webAddressSnippets(weblinks.value),
                *postSnippets(posts.value),
                *committeeMembershipSnippets(committeeMemberships.value)
            ).shuffled()
        }
    }

    private fun memberSnippets(member: Member?): Array<Snippet> {
        member ?: return arrayOf()
        return listOfNotNull(
            birthSnippet(member)
        ).toTypedArray()
    }

    private fun birthSnippet(member: Member): Snippet? {
        val dob: String? = member.profile.dateOfBirth?.let { formatDate(it) }
        val place: Town? = member.profile.placeOfBirth
        val age: Int? = member.profile.age

        application.run {
            return when {
                dob != null && place != null && age != null -> Snippet(
                    stringCompat(R.string.snippet_birth_date, dob),
                    stringCompat(R.string.snippet_birth_place, place.town, place.country),
                    subtitle = stringCompat(R.string.snippet_birth_age, age))

                else -> null
            }
        }
    }

    private fun webAddressSnippets(addresses: List<WebAddress>?): Array<Snippet> {
        addresses ?: return arrayOf()
        return addresses.map { it }
            .mapNotNull { webAddressSnippet(it) }
            .toTypedArray()
    }

    private fun webAddressSnippet(address: WebAddress): Snippet? {
        return Snippet(
            address.description,
            address.url,
            clickActionText = stringCompat(R.string.action_open_url),
            onclick = { context ->
                context.startActivity(
                    Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(address.url)
                    })
            }
        )
    }

    private fun postSnippets(posts: List<Post>?): Array<Snippet> {
        posts ?: return arrayOf()
        return posts.map { post ->
            val postType = stringCompat(when (post.postType) {
                Post.PostType.GOVERNMENTAL -> R.string.snippet_post_type_governmental
                Post.PostType.PARLIAMENTARY -> R.string.snippet_post_type_parliamentary
                Post.PostType.OPPOSITION -> R.string.snippet_post_type_opposition
            })

            val dateRange = dateRange(post.start, post.end)

            Snippet(
                post.name,
                postType,
                subcontent = dateRange
            )
        }.toTypedArray()
    }

    private fun committeeMembershipSnippets(memberships: List<CommitteeMembership>?): Array<Snippet> {
        memberships ?: return arrayOf()
        return memberships.map { membership ->
            Snippet(
                stringCompat(R.string.snippet_member_of, membership.name),
                dateRange(membership.start, membership.end) ?: ""
            )
        }.toTypedArray()
    }

    private inline fun stringCompat(@StringRes resId: Int, vararg formatArgs: Any): String =
        application.stringCompat(resId, *formatArgs)

    private fun date(yyyy_mm__dd: String) = formatDate(yyyy_mm__dd, calendar)

    private fun dateRange(start: String?, end: String?): String? = when {
        start == null -> null
        end == null -> stringCompat(R.string.snippet_date_since, date(start))
        else -> stringCompat(R.string.snippet_date_range, date(start), date(end))
    }
}

/**
 * Container for small chunks of information to display in
 */
data class Snippet(
    val title: String,
    val content: String,
    val subtitle: String? = null,
    val subcontent: String? = null,
    val clickActionText: String? = null, // If onclick is set this text will be displayed on the button
    val onclick: ((Context) -> Unit)? = null
)
