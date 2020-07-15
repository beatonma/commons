package org.beatonma.commons.network.retrofit

/**
 * snommorg.org API JSON key values. Use constants to avoid errors caused by typos.
 */
object Contract {
    const val PARLIAMENTDOTUK = org.beatonma.commons.data.PARLIAMENTDOTUK

    // Generic
    const val NAME = "name"
    const val TITLE = "title"
    const val DESCRIPTION = "description"
    const val URL = "url"

    // Time
    const val DATE = "date"
    const val START = "start"
    const val END = "end"

    /* Political entities */
    const val PARTY = "party"
    const val CONSTITUENCY = "constituency"
    const val HOUSE = "house"
    const val MP = "mp"
    const val ELECTION = "election"
    const val ELECTION_TYPE = "election_type"

    // Constituency
    const val BOUNDARY = "boundary"
    const val RESULTS = "results"
    const val ELECTORATE = "electorate"
    const val TURNOUT = "turnout"
    const val TURNOUT_FRACTION = "turnout_fraction"
    const val RESULT = "result"
    const val MAJORITY = "majority"
    const val CANDIDATES = "candidates"
    const val PARTY_NAME = "party_name"
    const val ORDER = "order"
    const val KML = "kml"
    const val AREA = "area"
    const val BOUNDARY_LENGTH = "boundary_length"
    const val CENTER_LATITUDE = "center_latitude"
    const val CENTER_LONGITUDE = "center_longitude"


    // Divisions
    const val DIVISION = "division"
    const val VOTE_TYPE = "vote_type"
    const val VOTES = "votes"
    const val VOTE = "vote"
    const val PASSED = "passed"
    const val AYES = "ayes"
    const val NOES = "noes"
    const val ABSTENTIONS = "abstentions"
    const val DID_NOT_VOTE = "did_not_vote"
    const val NON_ELIGIBLE = "non_eligible"
    const val SUSPENDED_OR_EXPELLED = "suspended_or_expelled"
    const val ERRORS = "errors"
    const val DEFERRED_VOTE = "deferred_vote"
    const val WHIPPED_VOTE = "whipped_vote"

    // Member profile
    const val ACTIVE = "active"
    const val IS_MP = "is_mp"
    const val IS_LORD = "is_lord"
    const val AGE = "age"
    const val DATE_OF_BIRTH = "date_of_birth"
    const val DATE_OF_DEATH = "date_of_death"
    const val GENDER = "gender"
    const val PLACE_OF_BIRTH = "place_of_birth"
    const val PORTRAIT = "portrait"
    const val CURRENT_POST = "current_post"
    const val TOWN = "town"
    const val COUNTRY = "country"
    const val SUBJECT = "subject"

    // Member posts
    const val GOVERNMENTAL = "governmental"
    const val PARLIAMENTARY = "parliamentary"
    const val OPPOSITION = "opposition"

    // Member committees
    const val CHAIR = "chair"

    // Member career stuff
    const val ADDRESS = "address"
    const val POSTS = "posts"
    const val COMMITTEES = "committees"
    const val HOUSES = "houses"
    const val INTERESTS = "interests"
    const val EXPERIENCES = "experiences"
    const val SUBJECTS = "subjects"
    const val CONSTITUENCIES = "constituencies"
    const val PARTIES = "parties"

    // Member experience
    const val CATEGORY = "category"
    const val ORGANISATION = "organisation"

    // Member financial interest
    const val CREATED = "created"
    const val AMENDED = "amended"
    const val DELETED = "deleted"
    const val REGISTERED_LATE = "registered_late"

    // Addresses
    const val POSTCODE = "postcode"
    const val PHONE = "phone"
    const val FAX = "fax"
    const val EMAIL = "email"
    const val PHYSICAL = "physical"
    const val WEB = "web"

    // Bills
    const val ACT_NAME = "act_name"
    const val LABEL = "label"
    const val HOMEPAGE = "homepage"
    const val BALLOT_NUMBER = "ballot_number"
    const val BILL_CHAPTER = "bill_chapter"
    const val IS_PRIVATE = "is_private"
    const val IS_MONEY_BILL = "is_money_bill"
    const val PUBLIC_INVOLVEMENT_ALLOWED = "public_involvement_allowed"
    const val PUBLICATIONS = "publications"
    const val SESSION = "session"
    const val TYPE = "type"
    const val SPONSORS = "sponsors"
    const val STAGES = "stages"
    const val SITTINGS = "sittings"
    const val FORMAL = "formal"
    const val PROVISIONAL = "provisional"

    /* Social */
    const val SNOMMOC_TOKEN = "token"
    const val GOOGLE_TOKEN = "gtoken"
    const val USERNAME = "username"
    const val COMMENTS = "comments"
    const val TEXT = "text"
    const val CREATED_ON = "created_on"
    const val MODIFIED_ON = "modified_on"
}

