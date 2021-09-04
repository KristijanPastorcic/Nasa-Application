package hr.algebra.nasaapplication.model

data class Item(
    var _id: Long?, // does not work without _!!!
    val title: String,
    val explanation: String,
    val picturePath: String?,
    val date: String,
    var read: Boolean
)