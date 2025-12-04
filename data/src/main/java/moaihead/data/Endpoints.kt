package moaihead.data


/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
data object Endpoints {


    data object FromPhoneToWear {
        const val REQUEST_SYNC = "/moai/request_sync"
    }


    data object FromWearToPhone  {
        const val INSERT_MOOD_ENTRY = "/moai/mood_entry"
    }

}