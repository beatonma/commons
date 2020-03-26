package org.beatonma.commons.data.core

import android.content.Context
import androidx.lifecycle.LiveData
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.IoResult
import org.beatonma.commons.data.core.room.CommonsDatabase
import org.beatonma.commons.data.core.room.entities.FeaturedMember
import org.beatonma.commons.data.core.room.entities.FeaturedMemberProfile
import org.beatonma.commons.data.core.room.entities.Post
import org.beatonma.commons.data.core.room.entities.WebAddress
import org.beatonma.commons.data.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommonsRepository @Inject constructor(
    val context: Context,
    private val commonsRemoteDataSource: CommonsRemoteDataSource,
    commonsDatabase: CommonsDatabase
) {
    private val memberDao = commonsDatabase.memberDao()

    fun observeFeaturedPeople(): LiveData<IoResult<List<FeaturedMemberProfile>>> = resultLiveData(
        databaseQuery = { memberDao.getFeaturedProfiles() },
        networkCall = { commonsRemoteDataSource.getFeaturedPeople() },
        saveCallResult = { profiles ->
            memberDao.apply {
                profiles.forEach {
                    insertParty(it.party)
                    insertConstituency(it.constituency)
                }

                insertProfiles(profiles)
                insertFeaturedPeople(
                    profiles.map { profile -> FeaturedMember(profile.parliamentdotuk) }
                )
            }
        }
    )

    fun observeMember(parliamentdotuk: Int): LiveData<IoResult<Member>> = resultLiveData(
        databaseQuery = { memberDao.getMember(parliamentdotuk) },
        networkCall = { commonsRemoteDataSource.getMember(parliamentdotuk) },
        saveCallResult = { member ->
            memberDao.apply {
                insertParty(member.profile.party)
                insertConstituency(member.profile.constituency)
                insertProfile(member.profile)

                insertPhysicalAddresses(
                    member.addresses.physical?.map { it.copy(personId = parliamentdotuk) }
                )
                insertWebAddresses(
                    member.addresses.web?.map { it.copy(personId = parliamentdotuk) }
                )

                insertPosts(member.posts.governmental.map { post ->
                    post.copy(memberId = parliamentdotuk, postType = Post.PostType.GOVERNMENTAL)
                })
                insertPosts(member.posts.parliamentary.map { post ->
                    post.copy(memberId = parliamentdotuk, postType = Post.PostType.PARLIAMENTARY)
                })
                insertPosts(member.posts.opposition.map { post ->
                    post.copy(memberId = parliamentdotuk, postType = Post.PostType.OPPOSITION)
                })
            }

        }
    )

    fun observeWebAddresses(parliamentdotuk: Int): LiveData<List<WebAddress>> = memberDao.getWebAddresses(parliamentdotuk)

    fun observePosts(parliamentdotuk: Int): LiveData<List<Post>> = memberDao.getPosts(parliamentdotuk)
}
