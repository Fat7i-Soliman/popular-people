package com.example.popularpeople.models

data class PersonDetails(var birthday:String="",
                         var known_for_department:String="",
                         var id :Int,
                         var name:String="",
                         var gender: Int,
                         var biography:String="",
                         var place_of_birth:String="",
                         var profile_path:String?=""
                         )
