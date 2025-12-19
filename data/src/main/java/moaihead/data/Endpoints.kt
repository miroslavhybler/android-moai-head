@file:Suppress("RedundantVisibilityModifier")

package moaihead.data

import androidx.annotation.StringDef


/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
public data object Endpoints {


    @StringDef(
        FromPhoneToWear.REQUEST_WEAR_SYNC,
        FromPhoneToWear.REQUEST_METADATA_SYNC_RESPONSE,
        FromPhoneToWear.REQUEST_MOOD_AND_NOTES_RESPONSE,
    )
    public annotation class FromPhone public constructor()


    @StringDef(
        FromWearToPhone.INSERT_MOOD_ENTRY,
        FromWearToPhone.REQUEST_METADATA_SYNC,
        FromWearToPhone.REQUEST_MOOD_AND_NOTES,
    )
    public annotation class FromWear public constructor()


    data object FromPhoneToWear {
        const val REQUEST_WEAR_SYNC = "/moai/request_sync"
        const val REQUEST_METADATA_SYNC_RESPONSE: String = "/mood/metadata/sync"
        const val REQUEST_MOOD_AND_NOTES_RESPONSE = "/mood/notes"

    }


    data object FromWearToPhone {
        const val INSERT_MOOD_ENTRY = "/mood/add"
        const val REQUEST_METADATA_SYNC = "/mood/metadata/sync"
        const val REQUEST_MOOD_AND_NOTES = "/mood/notes"
    }

}