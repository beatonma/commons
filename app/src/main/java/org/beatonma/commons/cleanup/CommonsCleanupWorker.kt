package org.beatonma.commons.cleanup

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.beatonma.commons.data.core.PersistencePolicy
import org.beatonma.commons.data.core.room.dao.MemberCleanupDao

private const val TAG = "CommonsCleanup"
class CommonsCleanupWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val memberCleanupDao: MemberCleanupDao
): CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        cleanupMemberProfiles(memberCleanupDao)

        return Result.success()
    }

    private suspend fun cleanupMemberProfiles(dao: MemberCleanupDao) {
        val profiles = dao.getAllMemberProfilesSync()

        val shouldBeDeleted = profiles.filter { profile ->
            profile.millisSinceLastAccess() > PersistencePolicy.DEFAULT_PERSISTENCE_PERIOD
        }

        Log.d(TAG, "Removing ${shouldBeDeleted.size} unused member profiles (${profiles.size - shouldBeDeleted.size} still in use)")

        dao.deleteProfiles(shouldBeDeleted)
    }
}
