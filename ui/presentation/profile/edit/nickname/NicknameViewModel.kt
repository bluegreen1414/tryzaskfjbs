package com.kapirti.social_chat_food_video.ui.presentation.profile.edit.nickname

import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class NicknameViewModel @Inject constructor(
    logService: LogService
): KapirtiViewModel(logService) {

    private val _nicknameState = MutableStateFlow<NicknameState>(NicknameState.Idle)
    val nicknameState: StateFlow<NicknameState> = _nicknameState

    fun checkNicknameAvailability(nickname: String) {
        launchCatching {
            _nicknameState.value = NicknameState.Loading
            val isAvailable = NicknameHelper.isNicknameAvailable(nickname)
            if (isAvailable) {
                _nicknameState.value = NicknameState.Available
            } else {
                val suggestions = NicknameHelper.suggestNicknames(nickname)
                _nicknameState.value = NicknameState.Taken(suggestions)
            }
        }
    }

    fun saveNickname(nickname: String, uid: String) {
        launchCatching {
            val isSuccess = NicknameHelper.saveNickname(nickname, uid)
            _nicknameState.value = if (isSuccess) NicknameState.Saved else NicknameState.Error
        }
    }
}

sealed class NicknameState {
    object Idle : NicknameState()
    object Loading : NicknameState()
    object Available : NicknameState()
    data class Taken(val suggestions: List<String>) : NicknameState()
    object Saved : NicknameState()
    object Error : NicknameState()
}
