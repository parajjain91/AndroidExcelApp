package com.varunbehl.myinventory.datamodel


import com.google.gson.annotations.SerializedName

data class MyExcelResponse(
    @SerializedName("columns")
    var columns: Columns? = null,
    @SerializedName("rows")
    var rows: List<Row?>? = null
) {
    data class Columns(
        @SerializedName("costprice")
        var costprice: List<Int?>? = null,
        @SerializedName("itemcount")
        var itemcount: List<Int?>? = null,
        @SerializedName("itemname")
        var itemname: List<String?>? = null,
        @SerializedName("itemtype")
        var itemtype: List<String?>? = null,
        @SerializedName("sellingprice")
        var sellingprice: List<Int?>? = null,
        @SerializedName("srno")
        var srno: List<Int?>? = null
    )

    data class Row(
        @SerializedName("costprice")
        var costprice: Int? = null, // 200
        @SerializedName("itemcount")
        var itemcount: Int? = null, // 4
        @SerializedName("itemname")
        var itemname: String? = null, // BirthDay Theme Set
        @SerializedName("itemtype")
        var itemtype: String? = null, // BirthDay Combo
        @SerializedName("sellingprice")
        var sellingprice: Int? = null, // 400
        @SerializedName("srno")
        var srno: Int? = null // 1
    )
}