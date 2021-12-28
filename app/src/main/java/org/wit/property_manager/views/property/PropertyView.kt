package org.wit.property_manager.views.property

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.property_manager.R
import org.wit.property_manager.databinding.ActivityPropertyBinding
import org.wit.property_manager.models.PropertyModel
import timber.log.Timber.i

class PropertyView : AppCompatActivity() {

    private lateinit var binding: ActivityPropertyBinding
    private lateinit var presenter: PropertyPresenter
    var property = PropertyModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        presenter = PropertyPresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cacheProperty(binding.propertyTitle.text.toString(), binding.description.text.toString())
            presenter.doSelectImage()
        }

        binding.propertyLocation.setOnClickListener {
            presenter.cacheProperty(binding.propertyTitle.text.toString(), binding.description.text.toString())
            presenter.doSetLocation()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_property, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        if (presenter.edit){
            deleteMenu.setVisible(true)
        }
        else{
            deleteMenu.setVisible(false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save -> {
                if (binding.propertyTitle.text.toString().isEmpty()) {
                    Snackbar.make(binding.root, R.string.enter_property_title, Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    presenter.doAddOrSave(binding.propertyTitle.text.toString(), binding.description.text.toString())
                }
            }
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }

        }
        return super.onOptionsItemSelected(item)
    }
    fun showProperty(property: PropertyModel) {
        binding.propertyTitle.setText(property.title)
        binding.description.setText(property.description)

        Picasso.get()
            .load(property.image)
            .into(binding.propertyImage)
        if (property.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_property_image)
        }

    }

    fun updateImage(image: Uri){
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.propertyImage)
        binding.chooseImage.setText(R.string.change_property_image)
    }

}