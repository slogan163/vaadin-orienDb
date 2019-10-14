package com.gmail.evgeniy.entity

//@AllOpen
data class Patient(  var rid: String = "#00:00",
                     var firstName: String = "",
                     var lastName: String = "",
                     var midName: String? = null,
                     var title: String? = null,
                     var email: String? = null,
                     var notes: String? = null) {

}
