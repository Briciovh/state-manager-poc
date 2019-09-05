package com.briciovh.statemanagerpoc

sealed class ObjectAction {
    class ObjectChanged(changedObject: DataObject) : ObjectAction()
    class ChangesDiscarded(originalObject: DataObject) : ObjectAction()
    class SaveClicked(changedObject: DataObject) : ObjectAction()
    class RetryClicked(changedObject: DataObject) : ObjectAction()
    class ServerSuccess(updatedObject: DataObject) : ObjectAction()
    class ServerError(changedObject: DataObject) : ObjectAction()
}