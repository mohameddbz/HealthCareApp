import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.utils.LocaleHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LanguageViewModel : ViewModel() {
    private val _language = MutableStateFlow("en") // Default to English
    val language: StateFlow<String> = _language

    fun changeLanguage(context: Context, newLanguage: String) {
        viewModelScope.launch {
            LocaleHelper.setLocale(context, newLanguage)
            _language.value = newLanguage
        }
    }
}
