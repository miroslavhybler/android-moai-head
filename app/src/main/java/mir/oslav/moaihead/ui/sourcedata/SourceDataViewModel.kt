package mir.oslav.moaihead.ui.sourcedata

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import moaihead.data.DataSourceRepository
import javax.inject.Inject


/**
 * @author Miroslav Hýbler <br>
 * created on 24.11.2025
 */
@HiltViewModel
class SourceDataViewModel @Inject constructor(
    val repo: DataSourceRepository,
) : ViewModel() {
}