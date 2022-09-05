package com.astro.test.aqil.features.dashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astro.BuildConfig
import com.astro.test.aqil.features.dashboard.data.repository.impl.NewsRepositoryImpl
import com.astro.test.aqil.features.dashboard.di.moshi
import com.astro.test.aqil.features.dashboard.data.dto.ProfilesDto
import com.astro.test.aqil.features.dashboard.data.dto.ResponseDto
import com.astro.test.aqil.features.dashboard.utils.NetworkHelper
import com.astro.test.aqil.features.dashboard.utils.Resource
import com.astro.test.aqil.features.dashboard.utils.ResourcesHelper
import kotlinx.coroutines.launch
import okio.IOException
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.InterruptedIOException
import java.net.*

class ProfilesViewModel: ViewModel(), KoinComponent {
    private val networkHelper by inject<NetworkHelper>()
    private val newsRepository by inject<NewsRepositoryImpl>()
    private val resourceHelper by inject<ResourcesHelper>()

    //Status balikan server
    private val _profile  = MutableLiveData<Resource<List<ProfilesDto>>>()
    //public untuk di observe di Fragment,LiveData
    val profile: LiveData<Resource<List<ProfilesDto>>>
        get() = _profile

    fun getProfile () {
        viewModelScope.launch {
            //loading
            _profile.postValue(Resource.loading(null))
            //jika terkoneksi internet
            if (networkHelper.isNetworkConnected()) {
                try {
                    newsRepository.getProfiles().let {
                        //2xx,3xx response code
                        _profile.postValue(Resource.loading(null))
                        if (it.isSuccessful) {
                            _profile.postValue(Resource.success(it.body()))
                        } else {
                            val errorBody = it.errorBody()?.string()
                            if(!errorBody.isNullOrEmpty()){
                                try {
                                    val jsonAdapter = moshi.adapter(ResponseDto::class.java)
                                    val moshiBean = jsonAdapter.fromJson(errorBody)
                                    _profile.postValue(Resource.error(moshiBean!!.message!!, null))
                                }catch (e :java.lang.Exception){
                                    _profile.postValue(Resource.error("ERROR", null))
                                }
                            }
                            else _profile.postValue(Resource.error("ERROR", null))
                        }
                    }
                } catch (e: IOException) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                    _profile.value = Resource.error(resourceHelper.cannotConnectToServer, null)
                } catch (e: SocketTimeoutException) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                    _profile.value = Resource.error(resourceHelper.timeOutConnetion, null)
                } catch (e: ConnectException) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                    _profile.value = Resource.error(resourceHelper.cannotConnectToServer, null)
                } catch (e: UnknownHostException) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                    _profile.value = Resource.error(resourceHelper.cannotConnectToServer, null)
                } catch (e: InterruptedIOException) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                    _profile.value = Resource.error(resourceHelper.timeOutConnetion, null)
                } catch (e: NoRouteToHostException) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                    _profile.value = Resource.error(resourceHelper.cannotConnectToServer, null)
                } catch (e: SocketException) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                    _profile.value = Resource.error(resourceHelper.cannotConnectToServer, null)
                } catch (e:IllegalStateException) {
                    //inernal error
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                    _profile.postValue(Resource.error(resourceHelper.errorSystem, null))
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                    _profile.postValue(Resource.error(resourceHelper.errorSystem, null))
                } catch (e: Throwable) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                    _profile.postValue(Resource.error(resourceHelper.errorSystem, null))
                }
            } else {
                _profile.value = Resource.error(resourceHelper.noInternetConnection, null)
            }
        }
    }
}