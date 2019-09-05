package com.briciovh.statemanagerpoc

import android.util.Log

interface ObjectState {
    fun consumeAction(action: ObjectAction): ObjectState

    class SynchronizedState(val synchronizedObject: DataObject) : ObjectState {
        override fun consumeAction(action: ObjectAction): ObjectState {
            return when (action) {
                is ObjectAction.ObjectChanged -> ChangedState(synchronizedObject)
                else -> throw IllegalStateException("Invalid $action")
            }

        }

    }

    class ChangedState(val currentObject: DataObject) : ObjectState {
        override fun consumeAction(action: ObjectAction): ObjectState {
            return when (action) {
                is ObjectAction.ObjectChanged -> ChangedState(currentObject)
                is ObjectAction.ChangesDiscarded -> SynchronizedState(currentObject)
                is ObjectAction.SaveClicked -> LoadingState(currentObject)
                else -> throw IllegalStateException("Invalid $action")
            }
        }

    }

    class LoadingState(val currentObject: DataObject) : ObjectState {
        override fun consumeAction(action: ObjectAction): ObjectState {
            return when (action) {
                is ObjectAction.ServerSuccess -> SynchronizedState(currentObject)
                is ObjectAction.ServerError -> FailedState(currentObject)
                else -> throw IllegalStateException("Invalid $action")
            }
        }

    }

    class FailedState(val currentObject: DataObject) : ObjectState {
        override fun consumeAction(action: ObjectAction): ObjectState {
            return when (action) {
                is ObjectAction.RetryClicked -> LoadingState(currentObject)
                else -> throw IllegalStateException("Invalid $action")
            }
        }

    }
}