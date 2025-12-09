package moaihead.data


/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
data object Endpoints {


    data object FromPhoneToWear {
        const val REQUEST_WEAR_SYNC = "/moai/request_sync"
        const val REQUEST_METADATA_SYNC_RESPONSE: String = "/mood/metadata/sync"
    }


    data object FromWearToPhone {
        const val INSERT_MOOD_ENTRY = "/mood/add"
        const val REQUEST_METADATA_SYNC = "/mood/metadata/sync"
    }

}