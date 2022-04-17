package vn.chungha.authenticator.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import jp.lecre.uchinokoalbum.utils.SingleLiveEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import vn.chungha.authenticator.BuildConfig
import vn.chungha.authenticator.utils.MessageEvent
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel(open val app: Application) : AndroidViewModel(app) {

	val onError = SingleLiveEvent<Throwable>()
	val isLoading = MutableLiveData(false)
	val isUpdateSuccess = MutableLiveData(false)

	protected val _messageChannel = Channel<MessageEvent>(Channel.BUFFERED)
	val messageChannel = _messageChannel.receiveAsFlow()

	val codeState = SingleLiveEvent<Int>()

	var jobCall: Job? = null

	protected fun launchJob(
		context: CoroutineContext = EmptyCoroutineContext,
		start: CoroutineStart = CoroutineStart.DEFAULT,
		block: suspend CoroutineScope.() -> Unit
	): Job = viewModelScope.launch(context + createErrorHandler(), start, block)

	protected fun launchLoadingJob(
		context: CoroutineContext = EmptyCoroutineContext,
		start: CoroutineStart = CoroutineStart.DEFAULT,
		block: suspend CoroutineScope.() -> Unit
	): Job = viewModelScope.launch(context + createErrorHandler(), start) {
		isLoading.postValue(true)
		try {
			block()
		} finally {
			isLoading.postValue(false)
		}
	}

	private fun createErrorHandler() = CoroutineExceptionHandler { _, throwable ->
		if (BuildConfig.DEBUG) {
			throwable.printStackTrace()
		}
		if (throwable !is CancellationException) {
			onError.postCall(throwable)
		}
	}

	override fun onCleared() {
		super.onCleared()
		jobCall?.cancel()
	}
	companion object {
		const val SUBSCRIBE_STOP_TIMEOUT = 5000L
	}
}