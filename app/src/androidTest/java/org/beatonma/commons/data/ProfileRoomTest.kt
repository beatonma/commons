package org.beatonma.commons.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.beatonma.commons.data.core.*
import org.beatonma.commons.political.organisation.Snp
import org.beatonma.lib.testing.kotlin.extensions.assertions.assertEquals
import org.beatonma.lib.util.kotlin.extensions.log
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Suite
import java.io.IOException

/**
 * Tests for Room database I/O
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    ProfileRoomTest::class,
    PeopleWithForeignKeyRoomTest::class
)
class PeopleRoomTestSuite

abstract class BasePeopleRoomTest {
    private lateinit var db: CommonsDatabase
    lateinit var peopleDao: PeopleDao

    val personID = 106
    val profile by lazy {
        Profile(
            personID = personID, parliamentaryID = 107,
            name = "Joe Bloggs",
            constituency = "Moray",
            party = Snp()
        )
    }

    val contactInfo by lazy {
        ContactInfo(
            personID,
            id = 16,
            email = "blah@parliament.uk",
            twitter = "@blahMP",
            facebook = "blahMP",
            weblink = "blahMP.co.uk",
            phoneConstituency = "01234 567 890",
            phoneParliamentary = "09876 543 210"
        )
    }
    val career by lazy {
        Career(
            personID,
            id = 17,
            parliamentary = "some parliamentary position",
            committee = "some committee",
            other = "Chef|Clown"
        )
    }
    val interests by lazy {
        Interests(
            personID,
            id = 18,
            political = "some political interest",
            countries = "Albania, Botswana, Cambodia"
        )
    }

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            CommonsDatabase::class.java
        ).build()
        peopleDao = db.peopleDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        log("db closed")
        db.close()
    }
}


class ProfileRoomTest : BasePeopleRoomTest() {
    @Test
    fun writeProfile() {
        val personID = 101
        val parliamentaryID = 307
        val profile = Profile(
            personID = personID, parliamentaryID = parliamentaryID,
            name = "Joe Bloggs",
            constituency = "Moray",
            party = Snp()
        )
        log("writeProfile")

        runBlocking {
            peopleDao.insertProfile(profile)
            peopleDao.getProfile(personID).run {
                parliamentaryID.assertEquals(parliamentaryID)
                name.assertEquals("Joe Bloggs")
                constituency.assertEquals("Moray")
                party.id.assertEquals("Scottish National Party")
            }
        }
    }

    @Test
    fun writeFullPerson() {
        runBlocking {
            peopleDao.insertPerson(
                Person(
                    profile = profile,
                    contactInfo = contactInfo,
                    career = career,
                    interests = interests
                )
            )
            peopleDao.getPerson(personID).let { person ->
                person.profile.name.assertEquals("Joe Bloggs")
                person.career.other.assertEquals("Chef|Clown")
                person.interests.countries.assertEquals("Albania, Botswana, Cambodia")
                person.contactInfo.phoneParliamentary.assertEquals("09876 543 210")
            }
        }
    }
}

/**
 * Tests that require a ForeignKey to a Profile.
 * A simple profile is created before running the tests.
 */
class PeopleWithForeignKeyRoomTest : BasePeopleRoomTest() {
    @Before
    fun createProfile() {
        runBlocking { peopleDao.insertProfile(profile) }
    }

    @Test
    fun writeCareer() {
        runBlocking {
            peopleDao.insertCareer(career)
            peopleDao.getCareer(personID).run {
                parliamentary.assertEquals("some parliamentary position")
                committee.assertEquals("some committee")
                other.assertEquals("Chef|Clown")
            }
        }
    }

    @Test
    fun writeInterests() {
        runBlocking {
            peopleDao.insertInterests(interests)
            peopleDao.getInterests(personID).run {
                political.assertEquals("some political interest")
                countries.assertEquals("Albania, Botswana, Cambodia")
            }
        }
    }

    @Test
    fun writeContactInfo() {
        runBlocking {
            peopleDao.insertContactInfo(contactInfo)
            peopleDao.getContactInfo(personID).run {
                email.assertEquals("blah@parliament.uk")
                twitter.assertEquals("@blahMP")
                facebook.assertEquals("blahMP")
                weblink.assertEquals("blahMP.co.uk")
                phoneConstituency.assertEquals("01234 567 890")
                phoneParliamentary.assertEquals("09876 543 210")
            }
        }
    }
}
