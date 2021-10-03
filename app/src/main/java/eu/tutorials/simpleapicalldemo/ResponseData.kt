package eu.tutorials.simpleapicalldemo
//Todo 2: Create a model for the data been requested
data class ResponseData (

   val message : String,
     val user_id : Int,
   val name : String,
    val email : String,
     val mobile : Int,
    val profile_details : ProfileDetails,
   val data_list : List<DataList>
)
data class ProfileDetails (

     val is_profile_completed : Boolean,
    val rating : Double,

)


data class DataList (

 val id : Int,
  val value : String
)