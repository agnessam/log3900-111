package com.example.colorimagemobile.services.drawing.toolsAttribute

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.classes.toolsCommand.PrimaryColorCommand
import com.example.colorimagemobile.classes.toolsCommand.SecondaryColorCommand
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.utils.Constants

// Service to change colors from existing shapes
// Not to be confused with color service, for the color picker
object SelectionColorService {
    private var primaryColor: String = Constants.DRAWING.PRIMARY_COLOR
    private var secondaryColor: String = Constants.DRAWING.SECONDARY_COLOR

    private var primaryColorCommand: PrimaryColorCommand? = null
    private var secondaryColorCommand: SecondaryColorCommand? = null

    private val updateCurrentPrimaryColor: MutableLiveData<String> = MutableLiveData()
    private val updateCurrentSecondaryColor: MutableLiveData<String> = MutableLiveData()

    fun updatePrimaryColor(newColor: String) {
        updateCurrentPrimaryColor.value = newColor
        primaryColor = newColor
    }

    fun updateSecondaryColor(newColor: String) {
        updateCurrentSecondaryColor.value = newColor
        secondaryColor = newColor
    }

    fun getCurrentPrimaryColor() : LiveData<String> {
        return updateCurrentPrimaryColor
    }

    fun getCurrentSecondaryColor() : LiveData<String> {
        return updateCurrentSecondaryColor
    }

    fun setPrimaryColor(newColor: String) {
        if (SelectionService.selectedShapeIndex != -1) {
            updatePrimaryColor(newColor)
            var selectedShapeId = DrawingObjectManager.getUuid(SelectionService.selectedShapeIndex) ?: return
            primaryColorCommand = PrimaryColorCommand(selectedShapeId, newColor)
            primaryColorCommand!!.execute()
        }
    }

    fun setSecondaryColor(newColor: String) {
        if (SelectionService.selectedShapeIndex != -1) {
            updateSecondaryColor(newColor)
            var selectedShapeId = DrawingObjectManager.getUuid(SelectionService.selectedShapeIndex) ?: return
            secondaryColorCommand = SecondaryColorCommand(selectedShapeId, newColor)
            secondaryColorCommand!!.execute()
        }
    }

    fun getPrimaryColorAsString(): String {
        return primaryColor
    }

    fun getSecondaryColorAsString(): String {
        return secondaryColor
    }

    fun getPrimaryColorAsInt(): Int {
        return ColorService.rgbaToInt(primaryColor)
    }

    fun getSecondaryColorAsInt(): Int{
        return ColorService.rgbaToInt(secondaryColor)
    }

    fun swapColors() {
        val tempPrimary = primaryColor
        primaryColor = secondaryColor
        secondaryColor = tempPrimary
        setPrimaryColor(primaryColor)
        setSecondaryColor(secondaryColor)
    }
}