package com.example.popularpeople.models

data class Root(var page:Int ,var total_pages:Int,var results:MutableList<Person>)


data class Person(var id:Int,
                  var name:String="",
                  var profile_path:String?="")