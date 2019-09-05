package com.briciovh.statemanagerpoc

import android.content.Context
import kotlin.properties.Delegates

class MainPresenter(val view: MainView, val context: Context) {

    val predefinedObject = DataObject("", false, 0)

    var currentObject = DataObject("Initial ID", false, 50)
    lateinit var changedObject: DataObject

    var currentState by Delegates.observable<ObjectState>(
        ObjectState.SynchronizedState(predefinedObject), { property, oldValue, newValue ->
            renderViewState(newValue, oldValue)
        }
    )

    private fun renderViewState(newState: ObjectState, oldState: ObjectState) {
        when (newState) {
            is ObjectState.SynchronizedState -> {
                view.refreshObjectInfo(newState.currentObject)
                view.enableSaveButton(false)
            }
            is ObjectState.ChangedState -> view.enableSaveButton(true)
            is ObjectState.FailedState -> view.enableRetryButton(true)
            is ObjectState.LoadingState -> view.showLoading(true)
        }
        when (oldState) {
            is ObjectState.LoadingState -> view.showLoading(false)
        }
    }

    fun onCreate() {
        view.refreshObjectInfo(currentObject)
        changedObject =
            DataObject(currentObject.objectID, currentObject.objectBoolean, currentObject.intValue)
    }

    fun onIDChanged(newId: String) {
        changedObject.objectID = newId
        currentState = currentState.consumeAction(ObjectAction.ObjectChanged(changedObject))
    }

    fun onSwitchChanged(checked: Boolean) {
        changedObject.objectBoolean = checked
        currentState = currentState.consumeAction(ObjectAction.ObjectChanged(changedObject))
    }

    fun onIntValueChanged(newInteger: Int) {
        changedObject.intValue = newInteger
        currentState = currentState.consumeAction(ObjectAction.ObjectChanged(changedObject))
    }
}