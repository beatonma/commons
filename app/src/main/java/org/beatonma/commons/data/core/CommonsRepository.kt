package org.beatonma.commons.data.core

import android.content.Context
import androidx.lifecycle.LiveData
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.IoResult
import org.beatonma.commons.data.core.room.CommonsDatabase
import org.beatonma.commons.data.core.room.entities.*
import org.beatonma.commons.data.resultLiveData
import org.beatonma.lib.util.kotlin.extensions.dump
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "CommonsRepo"

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
                profiles.forEach { profile ->
                    insertParty(profile.party)
                    profile.constituency?.let { insertConstituency(it) }
                }

                insertProfiles(profiles)
                insertFeaturedPeople(
                    profiles.map { profile -> FeaturedMember(profile.parliamentdotuk) }
                )
            }
        }
    )

    fun observeMember(parliamentdotuk: Int): LiveData<IoResult<CompleteMember>> = resultLiveData(
        databaseQuery = { memberDao.getCompleteMember(parliamentdotuk) },
        networkCall = { commonsRemoteDataSource.getMember(parliamentdotuk) },
        saveCallResult = { member ->
            member.dump("API: ")

            memberDao.run {
                insertParty(member.profile.party)
                member.profile.constituency?.let { insertConstituency(it) }
                insertProfile(member.profile)

                insertPhysicalAddresses(
                    member.addresses.physical.map { it.copy(personId = parliamentdotuk) }
                )
                insertWebAddresses(
                    member.addresses.web.map { it.copy(personId = parliamentdotuk) }
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

                insertCommitteeMemberships(member.committees.map { membership ->
                    CommitteeMembership(
                        membership.parliamentdotuk,
                        memberId = parliamentdotuk,
                        name = membership.name,
                        start = membership.start,
                        end = membership.end
                    )
                })

                member.committees.forEach { committee ->
                    insertCommitteeChairships(committee.chairs.map { chair ->
                        chair.copy(committeeId = committee.parliamentdotuk, memberId = parliamentdotuk)
                    })
                }

                insertHouseMemberships(member.houses.map { house ->
                    house.copy(memberId = parliamentdotuk)
                })

                insertFinancialInterests(member.financialInterests.map { it.copy(memberId = parliamentdotuk) })
                insertExperiences(member.experiences.map { it.copy(memberId = parliamentdotuk) })
                insertTopicsOfInterest(member.topicsOfInterest.map { it.copy(memberId = parliamentdotuk) })

                insertConstituencies(member.constituencies.map { it.constituency })
                insertElections(member.constituencies.map { it.election })
                insertMemberForConstituencies(member.constituencies.map {
                    MemberForConstituency(
                        memberId = parliamentdotuk,
                        constituencyId = it.constituency.parliamentdotuk,
                        electionId = it.election.parliamentdotuk,
                        start = it.start,
                        end = it.end)
                })
            }
        }
    )
}
