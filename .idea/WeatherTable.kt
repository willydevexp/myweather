@Parcelize
@Entity
data class LocationTable(
    @PrimaryKey() val id: Int,
    val dt: Int,
    val tempMax: Double,
    val tempMin: Double,
    val humidity: Int,
    val pressure: Int,
    val speed: Double,
    val description: String,
    val icon: String
) : Parcelable