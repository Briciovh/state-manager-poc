package com.briciovh.statemanagerpoc

interface MainView {
    fun enableSaveButton(enable: Boolean)
    fun enableRetryButton(enable: Boolean)
    fun showLoading(show: Boolean)
    fun refreshObjectInfo(currentObject: DataObject)
}