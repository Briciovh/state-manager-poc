package com.briciovh.statemanagerpoc

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        presenter = MainPresenter(this, this)
        presenter.onCreate()

        id_input.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                presenter.onIDChanged(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Not implemented
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Not implemented
            }

        })

        integer_input.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                p0?.toString()?.toInt()?.let { presenter.onIntValueChanged(it) }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Not implemented
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Not implemented
            }

        })

        only_switch.setOnCheckedChangeListener { compoundButton, isChecked ->
            presenter.onSwitchChanged(isChecked)
        }

        fab.setOnClickListener { view ->
            presenter.onSaveClicked()
        }

        enableSaveButton(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun enableSaveButton(enable: Boolean) {
        if (enable) {
            fab.setImageResource(android.R.drawable.ic_menu_save)
            fab.show()
        } else {
            fab.hide()
        }
    }

    override fun enableRetryButton(enable: Boolean) {
        if (enable) {
            fab.setImageResource(android.R.drawable.ic_menu_revert)
            fab.show()
        } else {
            fab.hide()
        }
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            loading_layout.visibility = VISIBLE
        } else {
            loading_layout.visibility = GONE
        }
    }

    override fun refreshObjectInfo(currentObject: DataObject) {
        id_input.editText?.setText(currentObject.objectID)
        only_switch.isChecked = currentObject.objectBoolean
        integer_input.editText?.setText(currentObject.intValue.toString())
    }

    override fun onBackPressed() {
        if(fab.isVisible) {
            AlertDialog.Builder(this)
                .setTitle("Changes not saved")
                .setMessage("Do you want to discard changes?")
                .setPositiveButton("Discard") { dialogInterface, i ->
                    presenter.onChangesDiscarded()
                    dialogInterface.dismiss()
                }
                .setNegativeButton("Back to edition") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .create().show()
        }
        else{
            super.onBackPressed()
        }
    }
}
