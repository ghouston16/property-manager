package org.wit.property_manager.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class PropertyModel(var id: Long = 0, var title: String = "",
                         var description: String = "",
                            var image: Uri = Uri.EMPTY): Parcelable
